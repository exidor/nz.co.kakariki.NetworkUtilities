package nz.co.kakariki.networkutils.nbrmgt;
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
import static nz.co.kakariki.networkutils.map.NetworkMapLayer.SRID;

import java.util.List;

import nz.co.kakariki.networkutils.network.Cell;
import nz.co.kakariki.networkutils.network.IDUtilities;
import nz.co.kakariki.networkutils.network.Neighbour;
import nz.co.kakariki.networkutils.network.NodeB;
import nz.co.kakariki.networkutils.network.PLMN;
import nz.co.kakariki.networkutils.network.RNC;
import nz.co.kakariki.networkutils.network.Sector;
import nz.co.kakariki.networkutils.network.Cell.CellType;
import nz.co.kakariki.networkutils.network.Neighbour.NeighbourPair;
import nz.co.kakariki.networkutils.network.Sector.Strategy;
import nz.co.kakariki.networkutils.reader.DataArray;
import nz.co.kakariki.networkutils.reader.Parser;
import nz.co.kakariki.networkutils.reader.ParserConfig;

public class NetworkManager {

	public static final int MAX_NBR_SIZE = 32;
	
	public static PLMN readNetwork(String filename) {
		int ri = 0;
		String rn = "";
		Strategy strategy = Strategy.Normal;
		int projection = SRID;
		PLMN plmn = new PLMN(1, "2Degrees");
		//RNC rnc = new RNC(1);
		//plmn.addRNC(rnc);

		RNC rnc = null;
		Parser p = new ParserConfig(filename);
		List<DataArray> lda = p.getDAPList();
		for (DataArray da : lda) {
			//rnc.setName(((DataArrayConfig)da).getRegion());
			for (List<String> row : da.getData()) {
				if("Region".compareTo(row.get(0))==0) {
					rn = row.get(1);
					
					Boolean exists = false;
					for (RNC r : plmn.getRNCs()){
						if(rn == rnc.getName()){
							rnc = r;
							exists = true;
							break;
						}
					}
					
					if (!exists) {
						rnc = new RNC(++ri,rn);
						plmn.addRNC(rnc);
					}
					continue;
				}
				if("Strategy".compareTo(row.get(0))==0) {
					strategy = Strategy.valueOf(row.get(1));
					continue;
				}
				if("Projection".compareTo(row.get(0))==0) {
					projection = Integer.parseInt(row.get(1));
					continue;
				}
				
				int ci = Integer.parseInt(row.get(0));
				int ni = IDUtilities.nodebid(ci);
				int si = IDUtilities.sectorid(ci);
				
				String name = row.get(1);

				// look through exsting NodeB list to find match

				
				
				
				NodeB nodeb = null;
				for (NodeB n : rnc.getNodeBs()) {
					if (ni == n.getID()) {
						nodeb = n;
						break;
					}
				}

				if (nodeb == null) {
					nodeb = new NodeB(ni,name);
					nodeb.getLocation().setLongitude(
							Double.parseDouble(row.get(2)));
					nodeb.getLocation().setLatitude(
							Double.parseDouble(row.get(3)));
					rnc.addNodeB(nodeb);
				}

				Sector sector = null;
				for (Sector s : nodeb.getSectors()) {
					if (si == s.getID()) {
						sector = s;
						break;
					}
				}

				if (sector == null) {
					sector = new Sector(si,name+"_"+String.valueOf(si));
					sector.setAzimuth(Integer.parseInt(row.get(4)));
					sector.setStrategy(strategy);
					nodeb.addSector(sector);
				}

				Cell cell = new Cell(Integer.parseInt(row.get(0)),name+"_"+String.valueOf(si)+"_"+row.get(5));
				cell.setCellType(CellType.valueOf(row.get(5)));
				sector.addCell(cell);
				// int cellid = row.get(0);
			}
		}
		return plmn;
	}	
	
	/**
	 * Reads a cellid<->cellid neighbour list assuming cellids have been created already
	 * from an external source or with the readConfig step
	 */
	public static Neighbour readNeighbourList(String filename){
		Parser p = new ParserConfig(filename);
		List<DataArray> lda = p.getDAPList();
		
		Neighbour neighbour = new Neighbour();
		
		for (DataArray da : lda) {
			for (List<String> row : da.getData()) {
				int part1 = Integer.parseInt(row.get(0));
				int part2 = Integer.parseInt(row.get(1));

				neighbour.addNeighbour(part1, part2);
			}
		}
		
		return neighbour;
	}
	
	/**
	 * Assumes network is not necessarily neighbour populated deletes shouldn't be necessary
	 * @param plmn
	 * @param neighbour
	 * @return
	 */
	public static void applyNeighbourList(PLMN plmn, Neighbour neighbour){
		//for each defined relationship
		for(NeighbourPair np : neighbour.getNeighbourList()){
			for(Cell cell : plmn.getCells()){
				//make sure the cell exists
				if(np.getSource()==cell.getID()){
					for(Cell nbr : plmn.getCells()){
						//make sure the nbr cell exists
						if(np.getTarget()==nbr.getID()){
							if(!cell.getNeighbourList().contains(nbr))cell.addNeighbourCell(nbr);
							if(!nbr.getNeighbourList().contains(cell))nbr.addNeighbourCell(cell);
						}
					}
				}
			}
		}
	}
	
	public static void neighbourStrategy(PLMN plmn, Strategy strategy){
		for (RNC rnc : plmn.getRNCs()){
			for (NodeB n1 : rnc.getNodeBs()){
				for (NodeB n2 : rnc.getNodeBs()){
					neighbourAll(n1,n2,strategy);
				}
			}
		}
	}	
	
	public static void neighbourStrategy(PLMN plmn){
		for (RNC rnc : plmn.getRNCs()){
			for (NodeB n1 : rnc.getNodeBs()){
				for(Sector s1 : n1.getSectors()){
					for (NodeB n2 : rnc.getNodeBs()){
						for(Sector s2 : n2.getSectors()){
							s1.addNeighbourSector(s2);
						}
					}
					for(Cell c1 : s1.getCells()){
						c1.truncateNeighbourList(MAX_NBR_SIZE);
					}
				}
			}
		}
	}
	
	public static void neighbourAll(PLMN plmn){
		neighbourStrategy(plmn,Strategy.Normal);
	}	
	
	
	public static void neighbourAll(NodeB n1, NodeB n2,Strategy strategy) {
		
		for (Sector s1 : n1.getSectors()){
			s1.setStrategy(strategy);
			for (Sector s2 : n2.getSectors()){
				s1.addNeighbourSector(s2);
				//s2.addNeighbourSector(s1);//reverse will be picked up in the loop
			}
		}
	}
	
	public static void displayAll(PLMN plmn){
		System.out.println(plmn);
		for (RNC rnc : plmn.getRNCs()){
			System.out.println(rnc);
			for (NodeB nodeb : rnc.getNodeBs()){
				System.out.println(nodeb);
				for (Sector sector : nodeb.getSectors()){
					System.out.println(sector);
					for (Cell cell : sector.getCells()){
						System.out.println(cell);
						int nc = 0;
						for(Cell nbr : cell.getNeighbourList()){
							System.out.println("N"+(++nc)+":N1:"+cell+" <-> N2:"+nbr);
						}
					}
				}
			}
		}
	}
}
