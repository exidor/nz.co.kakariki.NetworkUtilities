package nz.co.nzc.networkutils.network;

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
