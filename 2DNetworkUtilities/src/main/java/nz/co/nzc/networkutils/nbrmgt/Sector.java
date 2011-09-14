package nz.co.nzc.networkutils.nbrmgt;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import nz.co.nzc.networkutils.nbrmgt.Cell.CellType;

public class Sector {

	private int secid;
	public String name;
	public List<Cell> cells;
	
	public NodeB parent;
	
	public Strategy strategy = Strategy.Normal;

	//can be declared at node b but implemented at sector level 
	public enum Strategy {Normal,Peak,Heavy}
	
	//Self|eXternal+Gsm|Umts+9oo|18oo|21oo+[Frequency]1|2|3+Candidate
	
	private EnumSet<CellType> sg9c  = EnumSet.of(CellType.G9,CellType.G18,CellType.U21F1,CellType.U21F2,CellType.U21F3);
	private EnumSet<CellType> sg18c = EnumSet.of(CellType.G9);
	private EnumSet<CellType> su21f1c = EnumSet.of(CellType.G9,CellType.U21F1,CellType.U21F2,CellType.U21F3);
	private EnumSet<CellType> su21f2c = EnumSet.of(CellType.G9,CellType.U21F1,CellType.U21F2,CellType.U21F3);
	private EnumSet<CellType> su21f3c = EnumSet.of(CellType.G9,CellType.U21F1,CellType.U21F2,CellType.U21F3);
	
	private EnumSet<CellType> xg9c  = EnumSet.of(CellType.G9,CellType.U21F1,CellType.U21F2,CellType.U21F3);
	private EnumSet<CellType> xg18c = EnumSet.of(CellType.G18);
	private EnumSet<CellType> xu21f1c = EnumSet.of(CellType.U21F1);
	private EnumSet<CellType> xu21f2c = EnumSet.of(CellType.U21F2);
	private EnumSet<CellType> xu21f3c = EnumSet.of(CellType.U21F3);
	
	public Sector(int secid){
		this.setSecID(secid);		
		this.cells = new ArrayList<>();
	}
	
	
	public void addCell(Cell cell){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		cells.add(cell);
		cell.setParent(this);//giving the cell a direct nodeb parent enforces initialisation ordering
	}
	
	public Cell getCell(int cellid){
		for(Cell cell : cells){
			if (cellid == cell.getCellID()) return cell;
		}
		return null;
	}
	
	public List<Cell> getCells(){
		return cells;
	}
	
	public NodeB getParent() {
		return parent;
	}

	public void setParent(NodeB nodeb) {
		this.parent = nodeb;
	}

	public int getSecID() {
		return secid;
	}

	public void setSecID(int secid) {
		this.secid = secid;
	}
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * based on strategy and cell location [self/intra/inter] neighbour up matching 
	 * physical cells
	 * @param nbr
	 */
	public void addNeighbourSector(Sector secnbr){
		
		switch (getParent().strategy){
		case Normal:
			addNeighbourSectorNormal(secnbr);break;
		case Peak:
			addNeighbourSectorPeak(secnbr);break;
		case Heavy:
			//addNeighbourSectorHeavy(secnbr);break;
			addNeighbourSectorPeak(secnbr);break;
		}
	}
	
	//everything to everything
	public void addNeighbourSectorNormal(Sector secnbr){
		for(Cell cell : this.getCells()){
			for(Cell nbr : secnbr.cells){
				if(cell!=nbr)cell.addNeighbourCell(nbr);					
			}
		}
	}
	
	
	public void addNeighbourSectorPeak(Sector secnbr){
		boolean self = this.parent==secnbr.parent;
		for(Cell cell : this.getCells()){
			for(Cell nbr : secnbr.cells){
				if(cell!=nbr){
					switch (cell.celltype){
					case G9:
						if((self && sg9c.contains(nbr.celltype)) || xg9c.contains(nbr.celltype)) 
							cell.addNeighbourCell(nbr);
						break;
					case G18:
						if((self && sg18c.contains(nbr.celltype)) || xg18c.contains(nbr.celltype)) 
							cell.addNeighbourCell(nbr);
						break;
					case U9:
						break;
					case U21F1:
						if((self && su21f1c.contains(nbr.celltype)) || xu21f1c.contains(nbr.celltype)) 
							cell.addNeighbourCell(nbr);
						break;
					case U21F2:
						if((self && su21f2c.contains(nbr.celltype)) || xu21f2c.contains(nbr.celltype)) 
							cell.addNeighbourCell(nbr);
						break;
					case U21F3:
						if((self && su21f3c.contains(nbr.celltype)) || xu21f3c.contains(nbr.celltype)) 
							cell.addNeighbourCell(nbr);
						break;
					}
				}
			}
		}
	}
	
	public String toString(){
		return "S:"+String.valueOf(this.secid);
	}
}
