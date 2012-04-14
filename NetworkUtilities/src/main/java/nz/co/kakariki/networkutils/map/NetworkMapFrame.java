package nz.co.kakariki.networkutils.map;
/*
 * This file is part of 2DNetworkUtilities.
 *
 * 2DNetworkUtilities is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 3 of the 
 * License, or (at your option) any later version.
 *
 * 2DNetworkUtilities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import nz.co.kakariki.networkutils.map.NetworkMapLayer.MapFeatureType;
import nz.co.kakariki.networkutils.map.style.DisplayStyle.StyleType;
import nz.co.kakariki.networkutils.network.Cell;
import nz.co.kakariki.networkutils.network.NodeB;
import nz.co.kakariki.networkutils.network.Sector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;


public class NetworkMapFrame {

	
	public static String DEF_KML = "output/NeighbourList.kml";
	public static String DEF_NL = "output/NeighbourList.csv";
	
	public static final MapFeatureType FEATURETYPE = MapFeatureType.Sector;
    private ExtendedJMapFrame mapframe;
    
    private StyleFactory stylefactory = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactory2 filterfactory = CommonFactoryFinder.getFilterFactory2(null);
    
	//private String geoname;
	//private GeomType geotype;

	//private FeatureCollection featurecollection;
    
	private NetworkMapLayer maplayer;
	private JLabel label;

	private String lastop = "";
	private SimpleFeature selectedsector;
	private List<SimpleFeature> selectedneighbour = new ArrayList<>();
	
	public NetworkMapFrame(NetworkMapLayer maplayer){
		setMapLayer(maplayer);
		//setFeatureSource(FEATURETYPE);//sector for now but (user?) selectable
		
		//build window
		mapframe = new ExtendedJMapFrame(maplayer.getMap());
		
		//main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapframe, messagepane);
		
        mapframe.enableToolBar(true);
        mapframe.enableStatusBar(true);
        mapframe.enableMessagePane(true);        
        
        
        JToolBar toolbar = mapframe.getToolBar();
        JButton source = new JButton("Source");
        toolbar.addSeparator();
        toolbar.add(source);
        
        JButton target = new JButton("Target");
        toolbar.addSeparator();
        toolbar.add(target);
        
        JButton delete = new JButton("Delete");
        toolbar.addSeparator();
        toolbar.add(delete);
        
        JButton commit = new JButton("Commit");
        toolbar.addSeparator();
        toolbar.add(commit);
        
        
        JButton output = new JButton("Output");
        toolbar.addSeparator(new Dimension(50,1));
        toolbar.add(output);

        source.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	//mapframe.setMessage("Neighbour list cleared. Select new Source");
                                setSelectedSector(selectSource(ev));
                                if(getSelectedSector()!=null){
                                	getSelectedNeighbour().clear();
                                	List<SimpleFeature> ss = new ArrayList<SimpleFeature>();
                                	ss.add(getSelectedSector());
                                	getMapLayer().updateSelectionStylePair(MapFeatureType.Sector, ss, StyleType.Select);
                                }
                            }
                        });
            }
            
        });

        target.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	if(getSelectedSector()!=null){
                            		addSelectedNeighbour(selectTarget(ev));
                            		if(getSelectedNeighbour().size()>0){
                            			getMapLayer().updateSelectionStylePair(MapFeatureType.Sector, getSelectedNeighbour(), StyleType.Highlight);
                            		}
                            	}
                            }
                        });
            }
        });

        commit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		System.out.println("Commit");
        		
        		//TODO fix funky last op selection
            	if((lastop.compareTo("selectsource")==0 || lastop.compareTo("selecttarget")==0 ) && getSelectedNeighbour()!=null){
            		addNeighbours();
            		getMapLayer().updateFeatureStylePair(MapFeatureType.Neighbour, StyleType.Normal, getMapLayer().getPLMN());
            	}
            	else if(lastop.compareTo("selectneighbour")==0 && getSelectedNeighbour()!=null){ 
            		deleteNeighbours();
            		getMapLayer().updateFeatureStylePair(MapFeatureType.Neighbour, StyleType.Normal, getMapLayer().getPLMN());
            	}
            	
            	mapframe.repaint();
        	};
        	
        	
        });

        output.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	KMLGenerator kml = new KMLGenerator();
                System.out.println("Output to "+DEF_KML);
                KMLGenerator.output(DEF_KML, kml.showNetwork(getMapLayer().getPLMN()));
                
                NeighbourListGenerator csv = new NeighbourListGenerator();
                System.out.println("Output to "+DEF_NL);
                NeighbourListGenerator.output(DEF_NL, csv.showNetwork(getMapLayer().getPLMN()));
            }
        });

        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	if(selectedsector!=null){
                            	//mapframe.setMessage("Select Target Sectors");
                                selectNeighbour(ev);
                            	}
                            }
                        });
            }
        });
        
       
    }
	//---------------------------------------------------------------------------------
	
	//TODO make this more efficient
	private void deleteNeighbours() {
		for (SimpleFeature nbr : getSelectedNeighbour()) {
			String nbrid = nbr.getIdentifier().toString();
			String part1 = nbrid.substring(0, nbrid.indexOf(":"));
			String part2 = nbrid.substring(nbrid.indexOf(":")+1);

			System.out.println(nbrid+" ["+part1+"]["+part2+"]");

			for (Cell cell : getMapLayer().getPLMN().getCells()) {
				String cd = String.valueOf(cell.getID());
				// System.out.println("sec:"+sd);
				if (part1.compareTo(cd) == 0) {
					for (Cell neighbour : getMapLayer().getPLMN().getCells()) {
						String nd = String.valueOf(neighbour.getID());
						// System.out.println("nbr:"+nd);
						if (part2.compareTo(nd) == 0) {
							System.out.println("SEC:"+part1+" >X< NBR:"+part2);
							cell.deleteNeighbour(neighbour);
						}
					}
				}

			}
		}
	}

	private void addNeighbours(){
		String sf = getSelectedSector().getIdentifier().toString();
    	//match feature to sector and feature to neighbour
    	for (Sector sector : getMapLayer().getPLMN().getSectors()){
    		String sd = String.valueOf(((NodeB)sector.getParent()).getID()+sector.getID());
    		//System.out.println("sec:"+sd);
    		if (sf.compareTo(sd)==0){
    			System.out.println("BEFORE:\n"+sector.displayNeighbours());
    			sector.clearNeighbourList();
    			//System.out.print("SEC:"+sf);
    			for(SimpleFeature nbr : getSelectedNeighbour()){
    				String nf = nbr.getIdentifier().toString();
    				//nesting getting deep TODO something about it
    				for(Sector neighbour : getMapLayer().getPLMN().getSectors()){
    					String nd = String.valueOf(((NodeB)neighbour.getParent()).getID()+neighbour.getID());
    					//System.out.println("nbr:"+nd);
    					if (nf.compareTo(nd)==0){
    						System.out.println("SEC:"+sf+" <-> NBR:"+nf);
    						sector.addNeighbourSector(neighbour);
    					}
    				}
    			}
    			System.out.println("AFTER:\n"+sector.displayNeighbours());
    		}
    	}
	}
	
	 /**
     * This method is called by our feature selection tool when
     * the user has clicked on the map.
     *
     * @param pos map (world) coordinates of the mouse cursor
     */
    public SimpleFeature selectSource(MapMouseEvent ev) {

    	SimpleFeature sf = null;
    	
    	MapFeatureType selection = MapFeatureType.Sector;
        //System.out.println("Mouse click at: " + ev.getMapPosition());

        Point screenPos = ev.getPoint();
        Rectangle screenRect = new Rectangle(screenPos.x-2, screenPos.y-2, 5, 5);

        AffineTransform screenToWorld = this.mapframe.getMapPane().getScreenToWorldTransform();
        Rectangle2D worldrect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldrect,mapframe.getMapContext().getCoordinateReferenceSystem());

        Filter filter = filterfactory.intersects(
        		filterfactory.property(selection.getGeoName().toLowerCase()), 
        		filterfactory.literal(bbox)
        		);
        
        try {
       
        	SimpleFeatureCollection selected = (SimpleFeatureCollection) getFeatureCollection(selection).subCollection(filter);

            String message = "";
            
        	if (selected.size()>1){
        		message = "Multiple Sectors selected, "+selected.size()+". Please reselect";
        	}
        	else if (selected.size()<1){
        		message = "No Sector selected";
        	}
        	else {
        		//selectedneighbour.clear();//ATTN the is probably a better place to clear the local NL
        		sf = selected.features().next();
        		message = "Source:"+sf.getIdentifier();
        		
        	}
        	mapframe.setMessage(message);
                

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        lastop = "selectsource";
        return sf;
    }
    
    /**
     * This method is called by our feature selection tool when
     * the user has clicked on the map.
     *
     * @param pos map (world) coordinates of the mouse cursor
     */
    public SimpleFeature selectTarget(MapMouseEvent ev) {

    	SimpleFeature lsf = null;
    	MapFeatureType selection = MapFeatureType.Sector;
    	
        System.out.println("Mouse click at: " + ev.getMapPosition());

        /*
         * Construct a 5x5 pixel rectangle centred on the mouse click position
         */
        Point screenPos = ev.getPoint();
        Rectangle screenRect = new Rectangle(screenPos.x-2, screenPos.y-2, 5, 5);
        
        /*
         * Transform the screen rectangle into bounding box in the coordinate
         * reference system of our map context. Note: we are using a naive method
         * here but GeoTools also offers other, more accurate methods.
         */
        AffineTransform screenToWorld = mapframe.getMapPane().getScreenToWorldTransform();
        Rectangle2D worldrect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldrect,mapframe.getMapContext().getCoordinateReferenceSystem());

        
        System.out.println("BB:"+bbox);
        /*
         * Create a Filter to select features that intersect with
         * the bounding box
         */
        Filter filter = filterfactory.intersects(
        		filterfactory.property(selection.getGeoName().toLowerCase()), 
        		filterfactory.literal(bbox)
        		);
        
        /*
         * Use the filter to identify the selected features
         */
        try {
        	SimpleFeatureCollection selected = (SimpleFeatureCollection) getFeatureCollection(selection).subCollection(filter);
            
            String message = "";
            
        	if (selected.size()>1){
        		message = "Multiple Neighbours selected, "+selected.size()+". Please reselect";
        	}
        	else if (selected.size()<1){
        		message = "No Neighbour selected";
        	}
        	else {
        		//selectedneighbour.clear();
        		lsf = selected.features().next();
        		message = "Target: "+selected.features().next().getIdentifier();
        		
        	}
        	
        	String msg = "Source:"+getSelectedSector().getIdentifier()+" - Target{";
        	for(SimpleFeature sf : getSelectedNeighbour()){
        		msg += sf.getID()+",";
        	}
        	msg += lsf.getID();
        	//msg = msg.substring(0,msg.length()-1);
        	mapframe.setMessage(msg+"}");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        lastop = "selecttarget";
        return lsf;
        
    }
       
    /**
     */
    public void selectNeighbour(MapMouseEvent ev) {

    	MapFeatureType selection = MapFeatureType.Neighbour;
    	
        System.out.println("Mouse click on nbr at: " + ev.getMapPosition());
        Point screenPos = ev.getPoint();
        Rectangle screenRect = new Rectangle(screenPos.x-2, screenPos.y-2, 5, 5);
        
        AffineTransform screenToWorld = mapframe.getMapPane().getScreenToWorldTransform();
        Rectangle2D worldrect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldrect,mapframe.getMapContext().getCoordinateReferenceSystem());

        Filter filter = filterfactory.intersects(
        		filterfactory.property(selection.getGeoName().toLowerCase()), 
        		filterfactory.literal(bbox)
        		);

        try {
        	SimpleFeatureCollection selected = (SimpleFeatureCollection) getFeatureCollection(selection).subCollection(filter);
        	//Set<FeatureId> ids = new HashSet<FeatureId>();
        	
            String message = "";
            
        	if (selected.size()>0){
        		message = "Multiple Neighbours lines selected, "+selected.size();
        		SimpleFeatureIterator iter = selected.features();
        		try {
        			getSelectedNeighbour().clear();//ATTN do this here?
                    while (iter.hasNext()) {
                    	SimpleFeature sf = iter.next();
                    	getSelectedNeighbour().add(sf);
                        //ids.add(sf.getIdentifier());
                    }

                } finally {
                    iter.close();
                }
        	}
        	else {
        		message = "No Neighbour selected";
        	}

        	String msg = "Neighbours: [";
        	for(SimpleFeature sf : getSelectedNeighbour()){
        		msg += sf.getID()+",";
        	}
        	msg = msg.substring(0,msg.length()-1);
        	mapframe.setMessage(msg+"]");
            

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        lastop = "selectneighbour";
        return;
    }
    
    
    //TODO loop fspl layer list not just get(0)
   private FeatureCollection getFeatureCollection(MapFeatureType featuretype){
	   return getMapLayer().getFeatureStylePairList().get(featuretype).get(0).getFeatureCollection();
   }

    
    //-------------------------------------------------------------------------------------------
	
	
    public ExtendedJMapFrame getFrame() {
		return mapframe;
	}

	public void setFrame(ExtendedJMapFrame frame) {
		this.mapframe = frame;
	}

	public NetworkMapLayer getMapLayer() {
		return maplayer;
	}

	public void setMapLayer(NetworkMapLayer maplayer) {
		this.maplayer = maplayer;
	}

	public SimpleFeature getSelectedSector() {
		return selectedsector;
	}

	public void setSelectedSector(SimpleFeature selectedsector) {
		this.selectedsector = selectedsector;
	}

	public List<SimpleFeature> getSelectedNeighbour() {
		return selectedneighbour;
	}

	public void setSelectedNeighbour(List<SimpleFeature> selectedneighbour) {
		this.selectedneighbour = selectedneighbour;
	}
	
	public void addSelectedNeighbour(SimpleFeature neighbour) {
		this.selectedneighbour.add(neighbour);
	}

    
}