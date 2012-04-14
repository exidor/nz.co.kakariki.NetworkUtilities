package nz.co.kakariki.networkutils.gui;
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
import nz.co.kakariki.networkutils.map.NetworkMapFrame;
import nz.co.kakariki.networkutils.map.NetworkMapLayer;
import nz.co.kakariki.networkutils.nbrmgt.NetworkManager;
import nz.co.kakariki.networkutils.network.PLMN;

public class Main {


	private PLMN plmn;
	
	private NetworkMapLayer maplayer;
	private NetworkMapFrame mapframe;
	
	public Main (){
		//configure site data
		setPLMN(readSiteData());
		readNeighbourList();
		//NetworkManager.neighbourStrategy(plmn);
		
		maplayer = new NetworkMapLayer("Neighbour List Editor", plmn);
		
		mapframe = new NetworkMapFrame(maplayer);

        /**
         * Finally, we display the map frame. When it is closed
         * this application will exit.
         */
        mapframe.getFrame().setSize(800, 600);
        mapframe.getFrame().setVisible(true);
	}
	
	public PLMN readSiteData(){
		final String filename = "conf/pncs.csv";
		PLMN plmn = NetworkManager.readNetwork(filename);	
		//NetworkManager.neighbourStrategy(plmn,Strategy.Peak);
		return plmn;		
	}
	
	public void readNeighbourList(){
		final String filename = "conf/pnnl.csv";
		NetworkManager.applyNeighbourList(plmn, NetworkManager.readNeighbourList(filename));	
	}
	
	
	public PLMN getPLMN() {
		return plmn;
	}

	public void setPLMN(PLMN plmn) {
		this.plmn = plmn;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		Main m = new Main();
		// Now display the map
		//JMapFrame.showMap(m.getMapLayer().getMap());
	}
}
