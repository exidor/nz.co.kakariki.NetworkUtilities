package nz.co.nzc.networkutils.network;

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
