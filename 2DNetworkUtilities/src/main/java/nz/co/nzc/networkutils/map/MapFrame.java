package nz.co.nzc.networkutils.map;

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

import nz.co.nzc.networkutils.network.NodeB;
import nz.co.nzc.networkutils.network.Sector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.styling.JSimpleStyleDialog.GeomType;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;


public class MapFrame {

	
	public static final String FEATURETYPE = "Sector";
    private JCustomMapFrame mapframe;
    
    private StyleFactory stylefactory = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactory2 filterfactory = CommonFactoryFinder.getFilterFactory2(null);
    
	private String geoname;
	private GeomType geotype;

	private FeatureCollection featurecollection;
    
	private MapLayer maplayer;
	private JLabel label;

	private SimpleFeature selectedsector;
	private List<SimpleFeature> selectedneighbour = new ArrayList<>();
	
	public MapFrame(MapLayer maplayer){
		setMapLayer(maplayer);
		setFeatureSource(FEATURETYPE);//cell for now but (user?) selectable
		
		//build window
		mapframe = new JCustomMapFrame(maplayer.getMap());
		
		//main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapframe, messagepane);
		
		
        mapframe.enableToolBar(true);
        mapframe.enableStatusBar(true);
        mapframe.enableMessagePane(true);        
        
        
        JToolBar toolbar = mapframe.getToolBar();
        JButton source = new JButton("Source");
        toolbar.addSeparator();
        toolbar.add(source);

        source.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	//mapframe.setMessage("Neighbour list cleared. Select new Source");
                                selectedneighbour.clear();
                                selectSource(ev);
                            }
                        });
            }
        });
        
        JButton target = new JButton("Target");
        toolbar.addSeparator();
        toolbar.add(target);

        target.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	if(selectedsector!=null){
                            	//mapframe.setMessage("Select Target Sectors");
                                selectTarget(ev);
                            	}
                            }
                        });
            }
        });
        
        JButton commit = new JButton("Commit");
        toolbar.addSeparator();
        toolbar.add(commit);

        commit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		System.out.println("Commit");
            	String sf = selectedsector.getIdentifier().toString();
            	for (Sector sector : getMapLayer().getPLMN().getSectors()){
            		String sd = String.valueOf(((NodeB)sector.getParent()).getID()+sector.getID());
            		//System.out.println("sec:"+sd);
            		if (sf.compareTo(sd)==0){
            			//System.out.print("SEC:"+sf);
            			for(SimpleFeature nbr : selectedneighbour){
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
            		}
            		
            	}
            	mapframe.repaint();
        	};
        	
        	
        });
        
        JButton cancel = new JButton("Cancel");
        toolbar.addSeparator();
        toolbar.add(cancel);

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapframe.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                            	//mapframe.setMessage("Neighbour list cleared");
                                selectedneighbour.clear();
                            }
                        });
            }
        });
        
       
    }
	//---------------------------------------------------------------------------------
	
	 /**
     * This method is called by our feature selection tool when
     * the user has clicked on the map.
     *
     * @param pos map (world) coordinates of the mouse cursor
     */
    public void selectSource(MapMouseEvent ev) {

        //System.out.println("Mouse click at: " + ev.getMapPosition());

        Point screenPos = ev.getPoint();
        Rectangle screenRect = new Rectangle(screenPos.x-2, screenPos.y-2, 5, 5);

        AffineTransform screenToWorld = this.mapframe.getMapPane().getScreenToWorldTransform();
        Rectangle2D worldrect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldrect,mapframe.getMapContext().getCoordinateReferenceSystem());

        Filter filter = filterfactory.intersects(
        		filterfactory.property(geoname), 
        		filterfactory.literal(bbox)
        		);
        
        try {
       
        	SimpleFeatureCollection selected = (SimpleFeatureCollection) featurecollection.subCollection(filter);

        	System.out.println(featurecollection.size()+" filtered to -> "+selected.size());
            
            String message = "";
            
        	if (selected.size()>1){
        		message = "Multiple Sectors selected, "+selected.size()+". Please reselect";
        	}
        	else if (selected.size()<1){
        		message = "No Sector selected";
        	}
        	else {
        		selectedsector = selected.features().next();
        		message = "Source:"+selectedsector.getIdentifier();
        		
        	}
        	mapframe.setMessage(message);
                

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
    
    /**
     * This method is called by our feature selection tool when
     * the user has clicked on the map.
     *
     * @param pos map (world) coordinates of the mouse cursor
     */
    public void selectTarget(MapMouseEvent ev) {

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
        		filterfactory.property(geoname), 
        		filterfactory.literal(bbox)
        		);
        
        /*
         * Use the filter to identify the selected features
         */
        try {
        	SimpleFeatureCollection selected = (SimpleFeatureCollection) featurecollection.subCollection(filter);

        	System.out.println(featurecollection.size()+" filtered to -> "+selected.size());
            
            String message = "";
            
        	if (selected.size()>1){
        		message = "Multiple Neighbours selected, "+selected.size()+". Please reselect";
        	}
        	else if (selected.size()<1){
        		message = "No Neighbour selected";
        	}
        	else {
        		selectedneighbour.add(selected.features().next());
        		message = "Target: "+selected.features().next().getIdentifier();
        		
        	}
        	
        	String msg = "Source:"+selectedsector.getIdentifier()+" - Target{";
        	for(SimpleFeature sf : selectedneighbour){
        		msg += sf.getIdentifier()+",";
        	}
        	msg = msg.substring(0,msg.length()-1);
        	mapframe.setMessage(msg+"}");
                

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
    
    /**
     * Retrieve information about the feature geometry
     */
    private void setFeatureSource(String featuretype) {
    	featurecollection = getMapLayer().getFeatureStylePairList().get(featuretype).getFeatureCollection();
    	
    	Class<?> binding = null;
 
    	GeometryDescriptor geodesc = featurecollection.getSchema().getGeometryDescriptor();
    	geoname = geodesc.getLocalName();
    	binding = geodesc.getType().getBinding();
    		
        if (Polygon.class.isAssignableFrom(binding) ||
                MultiPolygon.class.isAssignableFrom(binding)) {
            geotype = GeomType.POLYGON;

        } else if (LineString.class.isAssignableFrom(binding) ||
                MultiLineString.class.isAssignableFrom(binding)) {

            geotype = GeomType.LINE;

        } else {
            geotype = GeomType.POINT;
        }

    }
    
    //-------------------------------------------------------------------------------------------
	
	
    public JCustomMapFrame getFrame() {
		return mapframe;
	}

	public void setFrame(JCustomMapFrame frame) {
		this.mapframe = frame;
	}

	public MapLayer getMapLayer() {
		return maplayer;
	}

	public void setMapLayer(MapLayer maplayer) {
		this.maplayer = maplayer;
	}

    
}
