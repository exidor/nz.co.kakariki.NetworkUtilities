package nz.co.nzc.networkutils.map;
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
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import nz.co.nzc.networkutils.network.Cell;
import nz.co.nzc.networkutils.network.NodeB;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.Sector;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.geotools.swing.styling.JSimpleStyleDialog.GeomType;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;


/* 
  *** SR-ORG:7160: WKT ***
  GEOGCS["GCS_WGS_1984", 
	  DATUM["D_WGS_1984", 
	    SPHEROID["WGS_1984", 6378137.0, 298.257223563]], 
	  PRIMEM["Greenwich", 0.0], 
	  UNIT["degree", 0.017453292519943295], 
	  AXIS["Longitude", EAST], 
	  AXIS["Latitude", NORTH]]
  
  *** EPSG:27200 ***
  PROJCS["NZGD49 / New Zealand Map Grid",
    GEOGCS["NZGD49",
        DATUM["New_Zealand_Geodetic_Datum_1949",
            SPHEROID["International 1924",6378388,297,
                AUTHORITY["EPSG","7022"]],
            TOWGS84[59.47,-5.04,187.44,0.47,-0.1,1.024,-4.5993],
            AUTHORITY["EPSG","6272"]],
        PRIMEM["Greenwich",0,
            AUTHORITY["EPSG","8901"]],
        UNIT["degree",0.01745329251994328,
            AUTHORITY["EPSG","9122"]],
        AUTHORITY["EPSG","4272"]],
    UNIT["metre",1,
        AUTHORITY["EPSG","9001"]],
    PROJECTION["New_Zealand_Map_Grid"],
    PARAMETER["latitude_of_origin",-41],
    PARAMETER["central_meridian",173],
    PARAMETER["false_easting",2510000],
    PARAMETER["false_northing",6023150],
    AUTHORITY["EPSG","27200"],
    AXIS["Easting",EAST],
    AXIS["Northing",NORTH]]
    
    *** EPSG:3395 ***
    PROJCS["WGS 84 / World Mercator",
    GEOGCS["WGS 84",
        DATUM["WGS_1984",
            SPHEROID["WGS 84",6378137,298.257223563,
                AUTHORITY["EPSG","7030"]],
            AUTHORITY["EPSG","6326"]],
        PRIMEM["Greenwich",0,
            AUTHORITY["EPSG","8901"]],
        UNIT["degree",0.01745329251994328,
            AUTHORITY["EPSG","9122"]],
        AUTHORITY["EPSG","4326"]],
    UNIT["metre",1,
        AUTHORITY["EPSG","9001"]],
    PROJECTION["Mercator_1SP"],
    PARAMETER["central_meridian",0],
    PARAMETER["scale_factor",1],
    PARAMETER["false_easting",0],
    PARAMETER["false_northing",0],
    AUTHORITY["EPSG","3395"],
    AXIS["Easting",EAST],
    AXIS["Northing",NORTH]]
    
    *** EPSG:4326 ***
    GEOGCS["WGS 84",
    DATUM["WGS_1984",
        SPHEROID["WGS 84",6378137,298.257223563,
            AUTHORITY["EPSG","7030"]],
        AUTHORITY["EPSG","6326"]],
    PRIMEM["Greenwich",0,
        AUTHORITY["EPSG","8901"]],
    UNIT["degree",0.01745329251994328,
        AUTHORITY["EPSG","9122"]],
    AUTHORITY["EPSG","4326"]]    


    *** EPSG:2193 ***
    PROJCS["NZGD2000 / New Zealand Transverse Mercator 2000",
    GEOGCS["NZGD2000",
        DATUM["New_Zealand_Geodetic_Datum_2000",
            SPHEROID["GRS 1980",6378137,298.257222101,
                AUTHORITY["EPSG","7019"]],
            TOWGS84[0,0,0,0,0,0,0],
            AUTHORITY["EPSG","6167"]],
        PRIMEM["Greenwich",0,
            AUTHORITY["EPSG","8901"]],
        UNIT["degree",0.01745329251994328,
            AUTHORITY["EPSG","9122"]],
        AUTHORITY["EPSG","4167"]],
    UNIT["metre",1,
        AUTHORITY["EPSG","9001"]],
    PROJECTION["Transverse_Mercator"],
    PARAMETER["latitude_of_origin",0],
    PARAMETER["central_meridian",173],
    PARAMETER["scale_factor",0.9996],
    PARAMETER["false_easting",1600000],
    PARAMETER["false_northing",10000000],
    AUTHORITY["EPSG","2193"],
    AXIS["Easting",EAST],
    AXIS["Northing",NORTH]]
 */
public class CustomMapLayer {
	
	public static final int SRID = 27200;//3395;
	
    private static final Color LINE_COLOUR = Color.BLUE;
    private static final Color FILL_COLOUR = Color.CYAN;
    private static final Color SELECTED_COLOUR = Color.YELLOW;
    private static final float OPACITY = 1.0f;
    private static final float LINE_WIDTH = 1.0f;
    private static final float POINT_SIZE = 10.0f;

	public static final String coast  = "map/Coastline_region.shp";//"map/10m_coastline.shp"; 
	public static final String land   = "map/10m_land.shp"; 
	public static final String ocean  = "map/10m_ocean.shp"; 
	public static final String island = "map/10m_minor_islands.shp"; 
	public static final String road   = "map/10m_roads.shp"; 
	public static final String rail   = "map/10m_railroads.shp"; 
	public static final String urban  = "map/10m_urban_areas.shp"; 
	
	//private SimpleFeatureType NODEB_TYPE, SECTOR_TYPE, CELL_TYPE, NEIGHBOUR_TYPE;
	
	public enum MapFeatureType {
		NodeB(0,"Point","id:Integer,nameString",GeomType.POINT),
		Sector(1,"LineString","node:Integer",GeomType.LINE),
		Cell(2,"Point","cellid:Integer,name:String,azimuth:Integer,type:String",GeomType.POINT),
		Neighbour(3,"LineString","soure:Integer,target:Integer",GeomType.LINE);
		
		private int index;
		private String shape;
		private String fields;
		private GeomType geomtype;
		
		MapFeatureType(int index,String shape,String fields,GeomType geomtype){
			this.index = index;
			this.shape = shape;
			this.fields = fields;
			this.geomtype = geomtype;
		}
		
		public int getIndex(){return this.index;}
		public String getShape(){return this.shape;}
		public String getFields(){return this.fields;}
		public GeomType getGeomType(){return this.geomtype;}
		
		public String getGeoName(){return this+this.getShape();}
		
		public SimpleFeatureType getFeatureType(){
			try {
				return DataUtilities.createType(getGeoName(),getGeoName().toLowerCase()+":"+getShape()+":srid="+SRID+","+getFields());
			} catch (SchemaException se) {
				System.err.println("Can't build Feature Type using proposed schema"+se);
				System.exit(1);
			}
			return null;
		}
	}
	
    //private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    //private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
	private MapContext map;

    private SimpleFeatureSource featureSource;
	
	//private String geometryAttributeName;
    //private GeomType geometryType;
    
    private Map<MapFeatureType, FeatureStylePair> fspl;
    private PLMN plmn;

	public CustomMapLayer (String maptitle,PLMN plmn){
		setPLMN(plmn);
		map = new DefaultMapContext();
		//map.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		map.setTitle(maptitle);
		
		/*
		try {
			//this.init();
			this.staticLayers();
		} catch (IOException e) {//|SchemaException e){
			System.err.println("Cannot initialise map layer ::"+e);
			System.exit(1);
		}
		*/
		
		fspl = new HashMap<>();
		
		fspl.put(MapFeatureType.NodeB,     new FeatureStylePair(mapNodeBList(plmn),     NodeBDisplayStyle.getPointStyle()));
		fspl.put(MapFeatureType.Sector,    new FeatureStylePair(mapSectorList(plmn),    NodeBDisplayStyle.getLineStyle()));
		fspl.put(MapFeatureType.Cell,      new FeatureStylePair(mapCellList(plmn),      SectorDisplayStyle.getPointStyle()));
		fspl.put(MapFeatureType.Neighbour, new FeatureStylePair(mapNeighbourList(plmn), SectorDisplayStyle.getLineStyle()));


		TreeSet<MapFeatureType> keys = new TreeSet<>(fspl.keySet());
		for (MapFeatureType key : keys) { 
		   FeatureStylePair fsp = fspl.get(key);
		   MapLayer ml = new MapLayer(fsp.getFeatureCollection(),fsp.getStyle());
		   map.addLayer(key.getIndex(), ml);
		}
		
	}
	
	public void updateFeatureStylePair(MapFeatureType key, PLMN plmn){
		//put replaces entry with the same id
		FeatureStylePair fsp = null;
		switch (key) {
		case NodeB: 
			fsp = new FeatureStylePair(mapNodeBList(plmn),NodeBDisplayStyle.getPointStyle());break;
		case Sector:
			fsp = new FeatureStylePair(mapSectorList(plmn),NodeBDisplayStyle.getLineStyle());break;
		case Cell: 
			fsp = new FeatureStylePair(mapCellList(plmn),SectorDisplayStyle.getPointStyle());break;
		case Neighbour: 
			fsp = new FeatureStylePair(mapNeighbourList(plmn),SectorDisplayStyle.getLineStyle());break;
		}
		fspl.put(key, fsp); 
		updateMap(key, fsp);
		
		
	}
	private void updateMap(MapFeatureType key, FeatureStylePair fsp){
		map.removeLayer(key.getIndex());
		MapLayer ml = new MapLayer(fsp.getFeatureCollection(),fsp.getStyle());
		map.addLayer(key.getIndex(), ml);
		
	}

	
	@SuppressWarnings("rawtypes")
	public void staticLayers() throws IOException {

		FileDataStore store1 = FileDataStoreFinder.getDataStore(new File(coast));
		//FileDataStore store2 = FileDataStoreFinder.getDataStore(new File(island));
		
		FeatureSource source1 = store1.getFeatureSource();
		//FeatureSource source2 = store2.getFeatureSource();
		
		System.out.println(source1.getInfo()+"::"+source1.getSchema());

		// Create a map context and add shapefiles to it
		map.addLayer(source1, null);
		//map.addLayer(source2, null);

	}
	

	public SimpleFeatureCollection mapCellList(PLMN plmn){
		
		 /*  FeatureCollection into which we will put each Feature matching a cell record
         */
       SimpleFeatureCollection collection = FeatureCollections.newCollection();
       
       /* GeometryFactory to create the geometry attribute of each feature ie Point.
        */
       GeometryFactory geofactory = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.JTS_SRID,SRID));

       SimpleFeatureBuilder cellbuilder = new SimpleFeatureBuilder(MapFeatureType.Cell.getFeatureType());
  
       for(Cell cell : plmn.getCells()){
       	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
       	//for some reason using raw coordinates reverses LAT and LNG
       	Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
       	cellbuilder.add(p);
       	cellbuilder.add(cell.getID());
       	cellbuilder.add(cell.getName());
       	cellbuilder.add(((Sector)cell.getParent()).getAzimuth());
       	cellbuilder.add(cell.getCellType().toString());
       	
       	SimpleFeature feature = cellbuilder.buildFeature(String.valueOf(cell.getID()));
           collection.add(feature);
       	
       }
       return collection;
	}
	
	public SimpleFeatureCollection mapNodeBList(PLMN plmn){
		
		 /*  FeatureCollection into which we will put each Feature matching a cell record
        */
      SimpleFeatureCollection collection = FeatureCollections.newCollection();
      
      /* GeometryFactory to create the geometry attribute of each feature ie Point.
       */
      GeometryFactory geofactory = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.JTS_SRID,SRID));

      SimpleFeatureBuilder nodebbuilder = new SimpleFeatureBuilder(MapFeatureType.NodeB.getFeatureType());
 
      for(NodeB nodeb : plmn.getNodeBs()){
      	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
      	//for some reason using raw coordinates reverses LAT and LNG
    	Point p = geofactory.createPoint(nodeb.getLocation().getCoordinate());
      	nodebbuilder.add(p);
      	nodebbuilder.add(nodeb.getID());
      	nodebbuilder.add(nodeb.getName());
      	
      	SimpleFeature feature = nodebbuilder.buildFeature(String.valueOf(nodeb.getID()));
          collection.add(feature);
      	
      }
      return collection;
	}
	
	public SimpleFeatureCollection mapNeighbourList(PLMN plmn){
		
		 /*  FeatureCollection into which we will put each Feature matching a cell record
       */
     SimpleFeatureCollection collection = FeatureCollections.newCollection();
     
     /* another way to define feature types
     SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
     builder.setName("Location");
     builder.setCRS(DefaultGeographicCRS.WGS84);
     
     builder.add("location", LineString.class);
     builder.length(15).add("source", Integer.class);
     builder.length(15).add("target", Integer.class);
     */
     
     /* GeometryFactory to create the geometry attribute of each feature ie Point.
      */
     GeometryFactory geofactory = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.JTS_SRID,SRID));     
     
     SimpleFeatureBuilder neighbourbuilder = new SimpleFeatureBuilder(MapFeatureType.Neighbour.getFeatureType());

     
     //if cant-get-map-bounds type errors occur, check you are generating actual lines/nbrs exist
     for(Cell cell : plmn.getCells()){
    	 for(Cell nbr : cell.getNeighbourList()){
	      	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
	      	//for some reason using raw coordinates reverses LAT and LNG
   		
	    	Coordinate[] cc = {
	    			((Sector)cell.getParent()).getSectorCoordinate(),
	    			((Sector)nbr.getParent()).getSectorCoordinate()};
	  
	    	LineString ls = geofactory.createLineString(cc);
	    	
	      	neighbourbuilder.add(ls);
	      	neighbourbuilder.add(cell.getID());
	      	neighbourbuilder.add(nbr.getID());
	      	
	      	SimpleFeature feature = neighbourbuilder.buildFeature(String.valueOf(cell.getID()+":"+nbr.getID()));
	        collection.add(feature);
   	  	}
     	
     }
     return collection;
	}
	
	public SimpleFeatureCollection mapSectorList(PLMN plmn){

		SimpleFeatureCollection collection = FeatureCollections.newCollection();

		GeometryFactory geofactory = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.JTS_SRID,SRID));

		SimpleFeatureBuilder neighbourbuilder = new SimpleFeatureBuilder(MapFeatureType.Sector.getFeatureType());

		for(Sector sector : plmn.getSectors()){   		
	    	Coordinate[] cc = {
	    			((NodeB)sector.getParent()).getLocation().getCoordinate(),
	    			sector.getSectorCoordinate()};
	  
	    	LineString ls = geofactory.createLineString(cc);
	    	
	      	neighbourbuilder.add(ls);
	      	neighbourbuilder.add(sector.getID());
	      	
	      	SimpleFeature feature = neighbourbuilder.buildFeature(String.valueOf(((NodeB)sector.getParent()).getID()+sector.getID()));
	        collection.add(feature);
   	  
     	
		}
		return collection;
	}
	
	public MapContext getMap() {
		return map;
	}

	public void setMap(MapContext map) {
		this.map = map;
	}
	
	public Map<MapFeatureType, FeatureStylePair> getFeatureStylePairList() {
		return fspl;
	}

	public void setFeatureStylePairList(Map<MapFeatureType, FeatureStylePair> fspl) {
		this.fspl = fspl;
	}
	
	public PLMN getPLMN() {
		return plmn;
	}

	public void setPLMN(PLMN plmn) {
		this.plmn = plmn;
	}

	/**
	 * Custom pair class matching collections of a particular type with an associated display style
	 * @author Joseph Ramsay
	 *
	 */
	@SuppressWarnings("rawtypes")
	public class FeatureStylePair {
		FeatureCollection featurecollection;
		Style style;
		FeatureStylePair(FeatureCollection featurecollection, Style style){
			this.featurecollection = featurecollection;
			this.style = style;
		}
		public FeatureCollection getFeatureCollection(){return featurecollection;}
		public Style getStyle(){return style;}
		
		
	}

}
