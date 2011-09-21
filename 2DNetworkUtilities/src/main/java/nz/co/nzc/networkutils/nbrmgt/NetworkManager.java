package nz.co.nzc.networkutils.nbrmgt;

import java.util.List;

import nz.co.nzc.networkutils.network.Cell;
import nz.co.nzc.networkutils.network.Cell.CellType;
import nz.co.nzc.networkutils.network.IDUtilities;
import nz.co.nzc.networkutils.network.NodeB;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.RNC;
import nz.co.nzc.networkutils.network.Sector;
import nz.co.nzc.networkutils.network.Sector.Strategy;
import nz.co.nzc.networkutils.reader.DataArray;
import nz.co.nzc.networkutils.reader.Parser;
import nz.co.nzc.networkutils.reader.ParserConfig;

public class NetworkManager {

	public static final int MAX_NBR_SIZE = 32;
	
	public static PLMN readNetwork(String filename) {
		int ri = 0;
		String rn = "";
		Strategy strategy = Strategy.Normal;
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
				
				int ci = Integer.parseInt(row.get(0));
				int ni = IDUtilities.nodebid(ci);
				int si = IDUtilities.sectorid(ci);

				// look through exsting NodeB list to find match

				
				
				
				NodeB nodeb = null;
				for (NodeB n : rnc.getNodeBs()) {
					if (ni == n.getID()) {
						nodeb = n;
						break;
					}
				}

				if (nodeb == null) {
					nodeb = new NodeB(ni);
					nodeb.getLocation().setLongitude(
							Double.parseDouble(row.get(1)));
					nodeb.getLocation().setLatitude(
							Double.parseDouble(row.get(2)));
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
					sector = new Sector(si);
					sector.setAzimuth(Integer.parseInt(row.get(3)));
					sector.setStrategy(strategy);
					nodeb.addSector(sector);
				}

				Cell cell = new Cell(Integer.parseInt(row.get(0)));
				cell.setCellType(CellType.valueOf(row.get(4)));
				sector.addCell(cell);
				// int cellid = row.get(0);
			}
		}
		return plmn;
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
