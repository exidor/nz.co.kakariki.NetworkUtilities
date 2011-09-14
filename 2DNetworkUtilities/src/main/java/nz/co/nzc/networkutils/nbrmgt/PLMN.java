package nz.co.nzc.networkutils.nbrmgt;

import java.util.ArrayList;
import java.util.List;

public class PLMN {
	public String name;
	public List<RNC> rncs;
	
	
	public PLMN(String name){
		this.name = name;
		
		this.rncs = new ArrayList<>();
	}
	
	public void addRNC(RNC rnc){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		rncs.add(rnc);
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
