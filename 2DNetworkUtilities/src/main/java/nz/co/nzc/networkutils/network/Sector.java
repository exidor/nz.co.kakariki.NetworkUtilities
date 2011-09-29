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
import java.util.EnumSet;
import java.util.List;

import nz.co.nzc.networkutils.network.Cell.CellType;

import com.vividsolutions.jts.geom.Coordinate;
public class Sector extends Network {

	public static final String DEF_NM = "SectorName";
	public static final String DEF_ADDR = "SectorAddress";
	
	public List<Cell> cells;
	
	public NodeB parent;
	
	public int azimuth;
	
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
	
	public Sector(int id,String name){
		super(id,name);		
		this.cells = new ArrayList<>();
	}
	
	public Sector(int id){
		this(id,DEF_NM+id);
	}
	
	public void addCell(Cell cell){
		//if(cells.size()<6) 3x F1+F2+F3+G9+G18 etc
		cells.add(cell);
		cell.setParent(this);//giving the cell a direct nodeb parent enforces initialisation ordering
	}
	
	public Cell getCell(int cellid){
		for(Cell cell : cells){
			if (cellid == cell.getID()) return cell;
		}
		return null;
	}
	
	public List<Cell> getCells(){
		return cells;
	}
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public int getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}
	
	public Coordinate getSectorCoordinate(){
		return((NodeB)this.getParent()).getLocation().projectedCoordinate(this.getAzimuth());
		//return((NodeB)this.getParent()).getLocation().getCoordinate();
	}

	/**
	 * based on strategy and cell location [self/intra/inter] neighbour up matching 
	 * physical cells
	 * @param nbr
	 */
	public void addNeighbourSector(Sector secnbr){
		
		switch (this.getStrategy()){
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
		//boolean self = this.getParent()==secnbr.getParent();//same nodeb
		boolean self = this==secnbr;//self is true if secnbr arg is itself
		for(Cell cell : this.getCells()){
			for(Cell nbr : secnbr.getCells()){
				if(cell!=nbr){
					CellType nct = nbr.getCellType();
					switch (cell.getCellType()){
					case G9:
						if((self && sg9c.contains(nct)) || xg9c.contains(nct)) 
							cell.addNeighbourCell(nbr);
						break;
					case G18:
						if((self && sg18c.contains(nct)) || xg18c.contains(nct)) 
							cell.addNeighbourCell(nbr);
						break;
					case U9:
						break;
					case U21F1:
						if((self && su21f1c.contains(nct)) || xu21f1c.contains(nct)) 
							cell.addNeighbourCell(nbr);
						break;
					case U21F2:
						if((self && su21f2c.contains(nct)) || xu21f2c.contains(nct)) 
							cell.addNeighbourCell(nbr);
						break;
					case U21F3:
						if((self && su21f3c.contains(nct)) || xu21f3c.contains(nct)) 
							cell.addNeighbourCell(nbr);
						break;
					}
				}
			}
		}
	}
	
	public void deleteNeighbour(Sector secnbr){
		//boolean self = this==secnbr;//self is true if secnbr arg is itself
		for(Cell cell : this.getCells()){
			for(Cell nbr : secnbr.getCells()){
				if(cell.getNeighbourList().contains(nbr)) cell.deleteNeighbour(nbr);
			}
		}
	}
	
	//convenience method
	public void clearNeighbourList(){
		for (Cell cell : cells){
			cell.clearNeighbourList();
		}
	}
	
	public String toString(){
		return "S:"+this.getID()+"/"+this.getStrategy();
	}

	/**
	 * convenience method to debug neighbour list changes
	 * @return
	 */
	public String displayNeighbours() {
		String nbrstr = "";
		for(Cell cell : cells){
			nbrstr += cell+",<->,";
			for (Cell nbr : cell.getNeighbourList()){
				nbrstr += nbr+",";
			}
			nbrstr += "\n";
		}
		return nbrstr;
		
	}
}
