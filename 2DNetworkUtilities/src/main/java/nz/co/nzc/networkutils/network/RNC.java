package nz.co.nzc.networkutils.network;

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
