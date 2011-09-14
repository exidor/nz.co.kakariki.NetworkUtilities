package nz.co.nzc.networkutils.nbrmgt;

import java.util.ArrayList;
import java.util.List;

import com.google.api.gbase.client.Location;

public class RNC {
	public Location location;

	public String name,address;
	public List<NodeB> nodebs;
	
	public RNC(String address){this("ABC",address);}
	public RNC(String name,String address){
		this.name = name;
		this.address = address;
		this.location = new Location(address);
		
		this.nodebs = new ArrayList<>();
	}
	
	public void addNodeB(NodeB nodeb){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		nodebs.add(nodeb);
	}
	
	public NodeB getNodeB(int nodebid){
		for(NodeB nodeb : nodebs){
			if (nodebid == nodeb.getNodeBID()) return nodeb;
		}
		return null;
	}
	
	public List<NodeB> getNodeBs(){
		return nodebs;
	}
	
	public String toString(){
		return "R:"+this.name+"/"+this.address;
	}
}
