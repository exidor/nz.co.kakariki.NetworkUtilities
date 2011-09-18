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
	
	public String toString(){
		return "P:"+this.name;
	}

	
}
