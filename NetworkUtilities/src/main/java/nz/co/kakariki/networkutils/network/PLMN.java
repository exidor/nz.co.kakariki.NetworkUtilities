package nz.co.kakariki.networkutils.network;
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
import java.util.ArrayList;
import java.util.List;

public class PLMN extends Network {

	public List<RNC> rncs;
	
	public PLMN(int id,String name){
		super(id,name);
		this.rncs = new ArrayList<>();
	}
	
	public void addRNC(RNC rnc){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		rncs.add(rnc);
		rnc.setParent(this);
	}
	
	public RNC getRNC(String name){
		for(RNC rnc : rncs){
			if (name == rnc.name) return rnc;
		}
		return null;
	}
	
	public List<RNC> getRNCs(){
		return rncs;
	}
	
	
	//convenience subordinate getters 
	
	public List<NodeB> getNodeBs() {
		List<NodeB> nodebs = new ArrayList<>();
		for (RNC rnc : this.getRNCs()) {
			nodebs.addAll(rnc.getNodeBs());
		}
		return nodebs;
	}

	public List<Sector> getSectors() {
		List<Sector> sectors = new ArrayList<>();
		for (RNC rnc : this.getRNCs()) {
			for (NodeB nodeb : rnc.getNodeBs()) {
				sectors.addAll(nodeb.getSectors());
			}
		}
		return sectors;
	}

	public List<Cell> getCells() {
		List<Cell> cells = new ArrayList<>();
		for (RNC rnc : this.getRNCs()) {
			for (NodeB nodeb : rnc.getNodeBs()) {
				for (Sector sector : nodeb.getSectors()) {
					cells.addAll(sector.getCells());
				}
			}
		}
		return cells;
	}
	
	
	
	public String toString(){
		return "P:"+this.name;
	}

	
}
