package nz.co.nzc.networkutils.nbrmgt;

import nz.co.nzc.networkutils.network.Cell;
import nz.co.nzc.networkutils.network.Cell.CellType;
import nz.co.nzc.networkutils.network.NodeB;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.RNC;
import nz.co.nzc.networkutils.network.Sector;


public class NetworkTestBuilder {

	public NetworkTestBuilder(){
		//populate(name);
	}
	
	public static PLMN generateNewTestPLMN(){
		PLMN P1 = new PLMN(1,"2D");
		RNC R1 = new RNC(10,"ABC");
				
		NodeB N1 = new NodeB(2100,"SiteAlpha");
		N1.setAddress("123 Alpha Street, Wellington");
		N1.getLocation().setLatitude(-45.9d);
		N1.getLocation().setLongitude(167.0d);
		
		NodeB N2 = new NodeB(2200,"SiteBeta");
		N2.setAddress("234 Beta Street, Wellington");
		N2.getLocation().setLatitude(-45.2d);
		N2.getLocation().setLongitude(167.4d);
		
		NodeB N3 = new NodeB(2300,"SiteGamma");
		N3.setAddress("345 Gamma Street, Wellington");
		N3.getLocation().setLatitude(-45.4d);
		N3.getLocation().setLongitude(167.1d);
		
		Sector S1N1 = new Sector(21001,"");
		Sector S2N1 = new Sector(21002,"");
		Sector S3N1 = new Sector(21003,"");
		
		Sector S1N2 = new Sector(22001,"");
		Sector S2N2 = new Sector(22002,"");
		Sector S3N2 = new Sector(22003,"");
		
		Sector S1N3 = new Sector(23001,"");
		Sector S2N3 = new Sector(23002,"");
		Sector S3N3 = new Sector(23003,"");
	
		//-----------------------------
		Cell N1S1UF1 = new Cell(21007,"");N1S1UF1.setCellType(CellType.U21F1);
		Cell N1S1UF2 = new Cell(41007,"");N1S1UF2.setCellType(CellType.U21F2);
		Cell N1S1UF3 = new Cell(61007,"");N1S1UF3.setCellType(CellType.U21F3);
		Cell N1S1G9 = new Cell(21001,"");N1S1G9.setCellType(CellType.G9);
		
		Cell N1S2UF1 = new Cell(21008,"");N1S2UF1.setCellType(CellType.U21F1);
		Cell N1S2UF2 = new Cell(41008,"");N1S2UF2.setCellType(CellType.U21F2);
		Cell N1S2UF3 = new Cell(61008,"");N1S2UF3.setCellType(CellType.U21F3);
		Cell N1S2G9 = new Cell(21003,"");N1S2G9.setCellType(CellType.G9);
		
		Cell N1S3UF1 = new Cell(21009,"");N1S3UF1.setCellType(CellType.U21F1);
		Cell N1S3UF2 = new Cell(41009,"");N1S3UF2.setCellType(CellType.U21F2);
		Cell N1S3UF3 = new Cell(41009,"");N1S3UF3.setCellType(CellType.U21F3);
		Cell N1S3G9 = new Cell(21005,"");N1S3G9.setCellType(CellType.G9);
		
		//---------------------------
		Cell N2S1UF1 = new Cell(22007,"");N2S1UF1.setCellType(CellType.U21F1);
		Cell N2S1UF2 = new Cell(42007,"");N2S1UF2.setCellType(CellType.U21F2);
		Cell N2S1UF3 = new Cell(62007,"");N2S1UF3.setCellType(CellType.U21F3);
		Cell N2S1G9 = new Cell(22001,"");N2S1G9.setCellType(CellType.G9);
		
		Cell N2S2UF1 = new Cell(22008,"");N2S2UF1.setCellType(CellType.U21F1);
		Cell N2S2UF2 = new Cell(42008,"");N2S2UF2.setCellType(CellType.U21F2);
		Cell N2S2UF3 = new Cell(62008,"");N2S2UF3.setCellType(CellType.U21F3);
		Cell N2S2G9 = new Cell(22003,"");N2S2G9.setCellType(CellType.G9);
		
		Cell N2S3UF1 = new Cell(22009,"");N2S3UF1.setCellType(CellType.U21F1);
		Cell N2S3UF2 = new Cell(42009,"");N2S3UF2.setCellType(CellType.U21F2);
		Cell N2S3UF3 = new Cell(62009,"");N2S3UF3.setCellType(CellType.U21F3);
		Cell N2S3G9 = new Cell(22005,"");N2S3G9.setCellType(CellType.G9);
		
		//---------------------------
		Cell N3S1UF1 = new Cell(23007,"");N3S1UF1.setCellType(CellType.U21F1);
		Cell N3S1UF2 = new Cell(43007,"");N3S1UF2.setCellType(CellType.U21F2);
		Cell N3S1UF3 = new Cell(63007,"");N3S1UF3.setCellType(CellType.U21F3);
		Cell N3S1G9 = new Cell(23001,"");N3S1G9.setCellType(CellType.G9);
		
		Cell N3S2UF1 = new Cell(23008,"");N3S2UF1.setCellType(CellType.U21F1);
		Cell N3S2UF2 = new Cell(43008,"");N3S2UF2.setCellType(CellType.U21F2);
		Cell N3S2UF3 = new Cell(63008,"");N3S2UF3.setCellType(CellType.U21F3);
		Cell N3S2G9 = new Cell(23003,"");N3S2G9.setCellType(CellType.G9);
		
		Cell N3S3UF1 = new Cell(23009,"");N3S3UF1.setCellType(CellType.U21F1);
		Cell N3S3UF2 = new Cell(43009,"");N3S3UF2.setCellType(CellType.U21F2);
		Cell N3S3UF3 = new Cell(63009,"");N3S3UF3.setCellType(CellType.U21F3);
		Cell N3S3G9 = new Cell(23005,"");N3S3G9.setCellType(CellType.G9);
		
		
		S1N1.addCell(N1S1UF1);S1N1.addCell(N1S1UF2);S1N1.addCell(N1S1UF3);S1N1.addCell(N1S1G9);
		S2N1.addCell(N1S2UF1);S2N1.addCell(N1S2UF2);S2N1.addCell(N1S2UF3);S2N1.addCell(N1S2G9);
		S3N1.addCell(N1S3UF1);S3N1.addCell(N1S3UF2);S3N1.addCell(N1S3UF3);S3N1.addCell(N1S3G9);
		
		S1N2.addCell(N2S1UF1);S1N2.addCell(N2S1UF2);S1N2.addCell(N2S1UF3);S1N2.addCell(N2S1G9);
		S2N2.addCell(N2S2UF1);S2N2.addCell(N2S2UF2);S2N2.addCell(N2S2UF3);S2N2.addCell(N2S2G9);
		S3N2.addCell(N2S3UF1);S3N2.addCell(N2S3UF2);S3N2.addCell(N2S3UF3);S3N2.addCell(N2S3G9);
		
		S1N3.addCell(N3S1UF1);S1N3.addCell(N3S1UF2);S1N3.addCell(N3S1UF3);S1N3.addCell(N3S1G9);
		S2N3.addCell(N3S2UF1);S2N3.addCell(N3S2UF2);S2N3.addCell(N3S2UF3);S2N3.addCell(N3S2G9);
		S3N3.addCell(N3S3UF1);S3N3.addCell(N3S3UF2);S3N3.addCell(N3S3UF3);S3N3.addCell(N3S3G9);
		
		N1.addSector(S1N1);
		N1.addSector(S2N1);
		N1.addSector(S3N1);
		
		N2.addSector(S1N2);
		N2.addSector(S2N2);
		N2.addSector(S3N2);
		
		N3.addSector(S1N3);
		N3.addSector(S2N3);
		N3.addSector(S3N3);
		
		R1.addNodeB(N1);
		R1.addNodeB(N2);
		R1.addNodeB(N3);
		
		P1.addRNC(R1);
		
		return P1;
		
	}
	
	
	


}