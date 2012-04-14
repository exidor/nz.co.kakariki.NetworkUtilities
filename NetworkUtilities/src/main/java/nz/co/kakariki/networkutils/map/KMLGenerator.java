package nz.co.kakariki.networkutils.map;
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
import java.util.EnumSet;

import nz.co.kakariki.networkutils.network.Cell;
import nz.co.kakariki.networkutils.network.NodeB;
import nz.co.kakariki.networkutils.network.PLMN;
import nz.co.kakariki.networkutils.network.RNC;
import nz.co.kakariki.networkutils.network.Sector;
import nz.co.kakariki.networkutils.network.Cell.CellType;
import nz.co.kakariki.networkutils.reader.Writer;

import com.vividsolutions.jts.geom.Coordinate;


//

/**
 * Temporary class for generating KML files, prefer API when available with maven or direct dl.
 * 1. Seperate TAG generation from extraction functions
 * 2. Employ layer functionality using folder TAGs
 * @author Joseph Ramsay
 *
 */
public class KMLGenerator {

	/**
	 * geenric page header text
	 * @return
	 */
	public static String header(){
		return "<?xml version='1.0' encoding='UTF-8'?>\n" +
			   "<kml xmlns='http://www.opengis.net/kml/2.2'>\n" +
			   "<Document>\n" +
			   "<name>NodeB</name>\n" +
			   "<open>1</open>\n" +
			   "<description>Neighbour location test map</description>\n" +
			   "<Style id='downArrowIcon'>\n" +
			   "<IconStyle>\n" +
			   "<Icon>\n" +
			   "<href>http://google-maps-icons.googlecode.com/files/watertower.png</href>\n" +
			   "</Icon>\n" +
			   "</IconStyle>\n" +
			   "</Style>\n";// +
			   //linestyle();
	}
	/**
	 * generic page footer text
	 * @return
	 */
	public static String footer(){
	    return "</Document>\n" +
	    	   "</kml>\n";
	}
	
	public static String folderHeader(String name, String desc){
		return "<Folder>\n" +
			   "<name>"+name+"</name>\n" +
			   "<description>"+desc+"</description>\n";
	}
	
	public static String folderFooter(){
		return "</Folder>";
	}
	
	//--------------------------------------------------------------------------
	
	
	/**
	 * placemark text for neighbour lines
	 * @param name
	 * @param desc
	 * @param nbrlist
	 * @return
	 */
	private static String placemark(String name, String desc, String nbrlist, CellType ct){
		return "<Placemark>\n" +
			   "<name>"+name+"</name>\n" +
			   "<description>"+desc+"</description>\n" +
			   stylemap(ct) +
			   linestring(nbrlist) +
			   "</Placemark>\n";
	}
	
	/**
	 * placemark text for nodeb/sector points
	 * @param name
	 * @param desc
	 * @param lng
	 * @param lat
	 * @return
	 */
	private static String placemark(String name, String desc, double lng, double lat){
		return "<Placemark>\n" +
			   "<name>"+name+"</name>\n" +
			   "<description>"+desc+"</description>\n" +
			   point(lat,lng) +
			   "</Placemark>\n";
		}
	
	private static String point(double lng, double lat){
		return "<Point>\n" +
			   "<coordinates>"+lng+", "+lat+", 0</coordinates>\n" +
			   "</Point>\n";
	}
	private static String linestring(String nbrlist){
		return "<LineString>\n" +
			   "<tessellate>1</tessellate>\n" +
			   "<coordinates>"+nbrlist+"</coordinates>\n" +
			   "</LineString>\n";
	}
	

	private static String stylemap(CellType celltype){
		CellColour cc = CellColour.valueOf(celltype.toString());
		return "<Style id='"+cc+"highlight'>\n" +
			   "<LineStyle>\n" +
			   "<color>"+cc.getColour()+"</color>\n" +
			   "<width>"+cc.getWidth()+"</width>\n" +
			   "</LineStyle>\n" +
			   "</Style>\n";
	}
	/*
	private static String linestyle(){
		String stylesection = "";
		for (CellColour cc : EnumSet.allOf(CellColour.class)){
			stylesection += "<Style id='"+cc+"highlight'>\n";
			stylesection += "<LineStyle>\n";
			stylesection += "<color>"+cc.getColour()+"</color>\n";
			stylesection += "<width>"+cc.getWidth()+"</width>\n";
			stylesection += "</LineStyle>\n";
			stylesection += "</Style>\n";
			stylesection += "<Style id='"+cc+"normal'>\n";
			stylesection += "<LineStyle>\n";
			stylesection += "<color>D4D4D4</color>\n";
			stylesection += "<width>1</width>\n";
			stylesection += "</LineStyle>\n";
			stylesection += "</Style>\n";
		}
		
		stylesection += "<StyleMap>";
		for(CellColour cc : EnumSet.allOf(CellColour.class)){
			stylesection += "<Pair>";
			stylesection += "<key>highlight</key>";
			stylesection += "<styleUrl>#"+cc+"highlight</styleUrl>";
			stylesection += "</Pair>";
			stylesection += "<Pair>";
			stylesection += "<key>normal</key>";
			stylesection += "<styleUrl>#"+cc+"normal</styleUrl>";
			stylesection += "</Pair>";
		}
		stylesection += "</StyleMap>";
		
		return stylesection;
	}
	*/

	
	public static String line(){
		String retval = "";
		return retval;
	}
	
	
	//---------------------------------------------------------------------------------------------
	
	public String showNetwork(PLMN plmn){
		String text = header();
		for (RNC rnc : plmn.getRNCs()){
			text += folderHeader("R"+rnc.getID(),rnc.getName());
			for (NodeB nb : rnc.getNodeBs()){
				text += folderHeader("N"+nb.getID(),nb.getName());
				text += nodeb(nb);
				text += folderFooter()+"<!-- NDB -->\n";
			}
			text += folderFooter()+"<!-- RNC -->\n";
		}
		return text+footer();
	}
	
	public String getKML(RNC rnc){
		String retval = header();
		for (NodeB nb : rnc.getNodeBs()){
			retval += nodeb(nb);
		}
		return retval+footer();
		
	}
	
	public static void output(String filename,String output){
		Writer kmlfile = new Writer(filename);
		kmlfile.writeString(output);
	}
	
	public enum CellColour {
		//AA(Alpha)BBGGRR
		G9("770000FF",10),G18("770088FF",8),U9("7700FF00",6),
		U21F1("77FF0000",4),U21F2("77FF4747",4),U21F3("77FF8C8C",4);
		String colour;
		int width;
		CellColour(String colour, int width){
			this.colour = colour;
			this.width = width;
		}
		String getColour(){return colour;}
		int getWidth(){return width;}
	}
	
	/** Temp class for storing parsed neighbour 
	 * data for display
	 * @author Joseph Ramsay
	 *
	 */
	public class NeighbourInfo {
		
		CellColour cellcolour = null;
		String pointlist = "";
		
	}
	
	
	public String nodeb(NodeB nodeb){
		String text = "";
		Coordinate nc = nodeb.getLocation().getCoordinate();
		text += folderHeader("N" + nodeb.getID(), nodeb.getName());
		text += placemark(nodeb.getName(), nodeb.getAddress(), nc.y, nc.x);
		for (Sector sec : nodeb.getSectors()){
			text += sector(sec);
		}
		text += folderFooter()+"<!-- NDB -->\n";
		return text;
	}
	
	public String sector(Sector sector) {
		String text = "";
		Coordinate sc = ((NodeB) sector.getParent()).getLocation().projectedCoordinate(sector.getAzimuth());
		text += folderHeader("S" + sector.getID(), sector.getName());
		text += placemark("S" + String.valueOf(sector.getAzimuth()),"Sector[" + String.valueOf(sector.getAzimuth()) + "]", sc.y,sc.x);
		for (CellType celltype : EnumSet.allOf(CellType.class)) {
			text += celltype(sector, celltype);
		}
		text += folderFooter()+"<!-- SEC -->\n";
		return text;
	}
	
	public String celltype(Sector sector, CellType celltype) {
		String text = "";
		text += folderHeader(celltype.toString(), "");
		for (Cell cell : sector.getCells()) {
			if (celltype.compareTo(cell.getCellType()) == 0) {
				text += cell(cell, celltype);
			}
		}
		text += folderFooter() + "<!-- CLT -->";

		return text;
	}
	
	public String cell(Cell cell, CellType celltype) {
		String text = "";

		text += "<styleUrl>" + celltype + "</styleUrl>";
		text += folderHeader("C" + cell.getID(), cell.getName());
		text += placemark("C" + cell.getID(), cell.getName(),	neighbourlist(cell).pointlist, celltype);
		text += folderFooter() + "<!-- CEL -->\n";

		return text;
	}
	
	/**
	 * return neighbourlist line markers by celltype
	 * @param cell
	 * @param celltype
	 * @return
	 */
	public NeighbourInfo neighbourlist(Cell cell){
		NeighbourInfo ni = new NeighbourInfo();
		ni.cellcolour = CellColour.valueOf(cell.getCellType().toString());
		for (Cell nbr : cell.getNeighbourList()){
			//if(celltype.compareTo(nbr.getCellType())==0){
				ni.pointlist += ((Sector)cell.getParent()).getSectorCoordinate().x + ", ";
				ni.pointlist += ((Sector)cell.getParent()).getSectorCoordinate().y + ", ";
				ni.pointlist += "0\n";
				ni.pointlist += ((Sector)nbr.getParent()).getSectorCoordinate().x + ", ";
				ni.pointlist += ((Sector)nbr.getParent()).getSectorCoordinate().y + ", ";
				ni.pointlist += "0\n";
			//}
		}
		
		return ni;
		
	}
	
	
}
