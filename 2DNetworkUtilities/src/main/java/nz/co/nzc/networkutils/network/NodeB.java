package nz.co.nzc.networkutils.network;
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

import nz.co.nzc.networkutils.network.Sector.Strategy;

public class NodeB extends Network {
	
	public static final String DEF_NM = "NodeBName";
	public static final String DEF_ADDR = "NodeBAddress";
		
	
	public Location location;

	public String address;
	public List<Sector> sectors;
	
	public Strategy strategy = Strategy.Normal;	
	
	
	public NodeB(int id,String name){
		super(id,name);
		this.sectors = new ArrayList<>();
		this.location = new Location(address);
	}
	
	public NodeB(int id){
		this(id,DEF_NM+id);
	}
	
	
	public void addSector(Sector sector){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		sectors.add(sector);
		sector.setParent(this);
	}
	
	public Sector getSector(int id){
		for(Sector sector : sectors){
			if (id == sector.getID()) return sector;
		}
		return null;
	}
	
	public List<Sector> getSectors(){
		return sectors;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getID() {
		return id;
	}

	public void setNodeBID(int nodebid) {
		this.id = nodebid;
	}
/*	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
*/	
	public String toString(){
		return "N:"+this.getID()+"/"+this.getName()+"/"+this.getAddress();
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress(){
		return this.address;
	}
}
