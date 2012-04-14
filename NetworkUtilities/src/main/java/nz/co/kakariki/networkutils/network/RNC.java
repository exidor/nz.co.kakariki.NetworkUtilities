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

//import com.google.api.gbase.client.Location;

public class RNC extends Network {
	
	public static final String DEF_NM = "RNCName";
	public static final String DEF_ADDR = "RNCAddress";
	
	public Location location;

	public String address;
	public List<NodeB> nodebs;
	
	public RNC(int id,String name){
		super(id,name);
		this.nodebs = new ArrayList<>();
		this.location = new Location(address);
	}
	
	public RNC(int id) {
		this(id,DEF_NM+id);
	}

	public void addNodeB(NodeB nodeb){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		nodebs.add(nodeb);
		nodeb.setParent(this);
	}
	
	public NodeB getNodeB(int id){
		for(NodeB nodeb : nodebs){
			if (id == nodeb.getID()) return nodeb;
		}
		return null;
	}
	
	public List<NodeB> getNodeBs(){
		return nodebs;
	}
	
	public String toString(){
		return "R:"+this.id+"/"+this.name+"/"+this.address;
	}
	
}
