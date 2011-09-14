package nz.co.nzc.networkutils.nbrmgt;

import java.util.ArrayList;
import java.util.List;

import nz.co.nzc.networkutils.nbrmgt.Sector.Strategy;

import com.google.api.gbase.client.Location;

public class NodeB {
	public Location location;

	private int nodebid;
	public String name,address;
	public List<Sector> sectors;
	
	public Strategy strategy = Strategy.Normal;	
	
	public NodeB(int nodebid, String name, String address){
		this.setNodeBID(nodebid);
		this.name = name;
		this.address = address;
		this.location = new Location(address);
		
		this.sectors = new ArrayList<>();
	}
	
	
	public void addSector(Sector sector){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		sectors.add(sector);
		sector.setParent(this);
	}
	
	public Sector getSector(int secid){
		for(Sector sector : sectors){
			if (secid == sector.getSecID()) return sector;
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

	public int getNodeBID() {
		return nodebid;
	}

	public void setNodeBID(int nodebid) {
		this.nodebid = nodebid;
	}
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public String toString(){
		return "N:"+this.nodebid+"/"+this.name+"/"+this.address;
	}
}
