package nz.co.nzc.networkutils.map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

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
public class MapLayer {
	private static final int SRID = 3395;
	
    private static final Color LINE_COLOUR = Color.BLUE;
    private static final Color FILL_COLOUR = Color.CYAN;
    private static final Color SELECTED_COLOUR = Color.YELLOW;
    private static final float OPACITY = 1.0f;
    private static final float LINE_WIDTH = 1.0f;
    private static final float POINT_SIZE = 10.0f;

	public static final String coast  = "map/10m_coastline.shp"; 
	public static final String land   = "map/10m_land.shp"; 
	public static final String ocean  = "map/10m_ocean.shp"; 
	public static final String island = "map/10m_minor_islands.shp"; 
	public static final String road   = "map/10m_roads.shp"; 
	public static final String rail   = "map/10m_railroads.shp"; 
	public static final String urban  = "map/10m_urban_areas.shp"; 
	
	private SimpleFeatureType NODEB_TYPE, SECTOR_TYPE, CELL_TYPE, NEIGHBOUR_TYPE;
	
	
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
	private MapContext map;
	//private JMapFrame mapframe;
    private SimpleFeatureSource featureSource;
	
	//private String geometryAttributeName;
    //private GeomType geometryType;
    
    private Map<String, FeatureStylePair> fspl;
    private PLMN plmn;

	public MapLayer (String maptitle,PLMN plmn){
		setPLMN(plmn);
		map = new DefaultMapContext();
		//map.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		map.setTitle(maptitle);
		
		try {
			this.init();
			//this.staticLayers();
		} catch (SchemaException e){//| IOException e) {
			System.err.println("Cannot initialise map layer ::"+e);
			System.exit(1);
		}
		
		
		//add layers to map
		 //p = new Pair();
		fspl = new HashMap<>();
		
		fspl.put("NodeB",new FeatureStylePair(mapNodeBList(plmn),          NodeBDisplayStyle.getPointStyle()));
		fspl.put("Sector", new FeatureStylePair(mapSectorList(plmn),       NodeBDisplayStyle.getLineStyle()));
		fspl.put("Cell", new FeatureStylePair(mapCellList(plmn),           SectorDisplayStyle.getPointStyle()));
		fspl.put("Neighbour", new FeatureStylePair(mapNeighbourList(plmn), SectorDisplayStyle.getLineStyle()));
		
		for(FeatureStylePair fsp : fspl.values()){
			map.addLayer(fsp.getFeatureCollection(),fsp.getStyle());
		}		
		
	}
	
	/**
	 * 
	 * SRID=4326 : WGS84
	 * @throws SchemaException
	 */
	private void init() throws SchemaException{
		
			NODEB_TYPE = DataUtilities.createType(
					"NodeBPoint",
			        "nodebpoint:Point:srid="+SRID+"," + // Geometry, Point(Coordinate)
			        "id:Integer," +               // NodeB ID
			        "name:String"                 // NodeB Name
				);
			
			CELL_TYPE = DataUtilities.createType(
					"CellPoint",
		            "cellpoint:Point:srid="+SRID+"," + // Geometry, Point(Coordinate)
		            "cellid:Integer," +           // Cell ID
		            "name:String," +              // Cell Name
		            "azimuth:Integer," +          // Antennae Azimuth
		            "type:String"                 // Cell Tech/Freq
				);
			
			SECTOR_TYPE = DataUtilities.createType(
					"SectorLine",
			        "sectorline:LineString:srid="+SRID+"," + // Geometry, Point(Coordinate)
			        "node:Integer"                     // Sector ID
				);
			//4326
			NEIGHBOUR_TYPE = DataUtilities.createType(
					"NeighbourLine",
			        "neighbourline:LineString:srid="+SRID+"," + // Geometry, Point(Coordinate)
			        "soure:Integer," +                 // Cell ID
			        "target:Integer"                   // Cell ID
				);
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

       SimpleFeatureBuilder cellbuilder = new SimpleFeatureBuilder(CELL_TYPE);
  
       for(Cell cell : plmn.getCells()){
       	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
       	//for some reason using raw coordinates reverses LAT and LNG
       	Point p = geofactory.createPoint(
       			((Sector)cell.getParent()).getSectorCoordinate());
       			//new Coordinate(
       			//((Sector)cell.getParent()).getSectorCoordinate().x,
       			//((Sector)cell.getParent()).getSectorCoordinate().y
       			//));
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

      SimpleFeatureBuilder nodebbuilder = new SimpleFeatureBuilder(NODEB_TYPE);
 
      for(NodeB nodeb : plmn.getNodeBs()){
      	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
      	//for some reason using raw coordinates reverses LAT and LNG
    	Point p = geofactory.createPoint(
    			nodeb.getLocation().getCoordinate());
      			//new Coordinate(
      			//nodeb.getLocation().getCoordinate().x,
      			//nodeb.getLocation().getCoordinate().y
      			//));
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

     //SimpleFeatureBuilder neighbourbuilder = new SimpleFeatureBuilder(builder.buildFeatureType());
     
     
     SimpleFeatureBuilder neighbourbuilder = new SimpleFeatureBuilder(NEIGHBOUR_TYPE);

     
     //if cant-get-map-bounds type errors occur, check you are generating actual lines/nbrs exist
     for(Cell cell : plmn.getCells()){
    	 for(Cell nbr : cell.getNeighbourList()){
	      	//Point p = geofactory.createPoint(((Sector)cell.getParent()).getSectorCoordinate());
	      	//for some reason using raw coordinates reverses LAT and LNG
   		
	    	Coordinate[] cc = {
	    			((Sector)cell.getParent()).getSectorCoordinate(),
	    			((Sector)nbr.getParent()).getSectorCoordinate()};
	    			//new Coordinate(
	    			//		((Sector)cell.getParent()).getSectorCoordinate().x,
	    			//		((Sector)cell.getParent()).getSectorCoordinate().y
	    			//		),
	    			//new Coordinate(
	    	    	//		((Sector)nbr.getParent()).getSectorCoordinate().x,
	    	    	//		((Sector)nbr.getParent()).getSectorCoordinate().y
	    	    	//		)};
	  
	    	LineString ls = geofactory.createLineString(cc);
	    	
	      	neighbourbuilder.add(ls);
	      	neighbourbuilder.add(cell.getID());
	      	neighbourbuilder.add(nbr.getID());
	      	
	      	SimpleFeature feature = neighbourbuilder.buildFeature(String.valueOf(cell.getID()+nbr.getID()));
	        collection.add(feature);
   	  	}
     	
     }
     return collection;
	}
	
	public SimpleFeatureCollection mapSectorList(PLMN plmn){

		SimpleFeatureCollection collection = FeatureCollections.newCollection();

		GeometryFactory geofactory = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.JTS_SRID,SRID));

		SimpleFeatureBuilder neighbourbuilder = new SimpleFeatureBuilder(SECTOR_TYPE);

		for(Sector sector : plmn.getSectors()){   		
	    	Coordinate[] cc = {
	    			((NodeB)sector.getParent()).getLocation().getCoordinate(),
	    			sector.getSectorCoordinate()};
	    			//new Coordinate(
	    			//		((NodeB)sector.getParent()).getLocation().getCoordinate().x,
	    			//		((NodeB)sector.getParent()).getLocation().getCoordinate().y
	    			//		),
	    			//new Coordinate(
	    	    	//		sector.getSectorCoordinate().x,
	    	    	//		sector.getSectorCoordinate().y
	    	    	//		)};
	  
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
	
	public Map<String, FeatureStylePair> getFeatureStylePairList() {
		return fspl;
	}

	public void setFeatureStylePairList(Map<String, FeatureStylePair> fspl) {
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
