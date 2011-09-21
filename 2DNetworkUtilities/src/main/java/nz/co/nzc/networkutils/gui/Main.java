package nz.co.nzc.networkutils.gui;

import java.io.File;
import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapFrame;

public class Main {

	public static final String coast = "map/10m_coastline.shp"; 
	
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.render();
	}
	public void render() throws IOException {
	
		// display a data store file chooser dialog for shapefiles
		//File file = JFileDataStoreChooser.showOpenFile("shp", null);
		//if (file == null) {
		//	return;
		//}

		FileDataStore store = FileDataStoreFinder.getDataStore(new File(coast));
		FeatureSource featureSource = store.getFeatureSource();

		// Create a map context and add our shapefile to it
		MapContext map = new DefaultMapContext();
		map.setTitle("Neighbour Selector");
		map.addLayer(featureSource, null);

		// Now display the map
		JMapFrame.showMap(map);
	}
}
