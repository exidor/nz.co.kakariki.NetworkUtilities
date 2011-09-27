package nz.co.nzc.networkutils.gui;

import nz.co.nzc.networkutils.map.MapFrame;
import nz.co.nzc.networkutils.map.MapLayer;
import nz.co.nzc.networkutils.nbrmgt.NetworkManager;
import nz.co.nzc.networkutils.network.PLMN;

public class Main {


	private PLMN plmn;
	private MapLayer maplayer;
	private MapFrame mapframe;
	
	public Main (){
		//configure site data
		plmn = readSiteData();
		NetworkManager.neighbourStrategy(plmn);
		
		maplayer = new MapLayer("Neighbour List Editor", plmn);
		
		mapframe = new MapFrame(maplayer);

        /**
         * Finally, we display the map frame. When it is closed
         * this application will exit.
         */
        mapframe.getFrame().setSize(800, 600);
        mapframe.getFrame().setVisible(true);
	}
	
	public PLMN readSiteData(){
		final String filename = "conf/sample1.csv";
		PLMN plmn = NetworkManager.readNetwork(filename);	
		//NetworkManager.neighbourStrategy(plmn,Strategy.Peak);
		return plmn;		
	}
	/*	
	public MapLayer getMapLayer() {
		return maplayer;
	}

	public void setMapLayer(MapLayer maplayer) {
		this.maplayer = maplayer;
	}
	*/
	public static void main(String[] args) throws Exception {
		
		Main m = new Main();
		// Now display the map
		//JMapFrame.showMap(m.getMapLayer().getMap());
	}
}
