package nz.co.nzc.networkutils.map;


import static nz.co.nzc.networkutils.map.CustomMapLayer.SRID;
import nz.co.nzc.networkutils.map.CustomMapLayer.MapFeatureType;
import nz.co.nzc.networkutils.network.Cell;
import nz.co.nzc.networkutils.network.NodeB;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.Sector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public class FeatureBuilder {

	
	public static SimpleFeatureCollection mapCellList(PLMN plmn){
		
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
	
	public static SimpleFeatureCollection mapNodeBList(PLMN plmn){
		
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
	
	public static SimpleFeatureCollection mapNeighbourList(PLMN plmn){
		
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
	
	public static SimpleFeatureCollection mapSectorList(PLMN plmn){

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
	
}
