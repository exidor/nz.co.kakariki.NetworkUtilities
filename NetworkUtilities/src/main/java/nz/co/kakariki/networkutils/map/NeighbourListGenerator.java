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
import nz.co.kakariki.networkutils.network.Cell;
import nz.co.kakariki.networkutils.network.PLMN;
import nz.co.kakariki.networkutils.reader.Writer;


//

/**
 * Temporary class for generating KML files, prefer API when available with maven or direct dl.
 * 1. Seperate TAG generation from extraction functions
 * 2. Employ layer functionality using folder TAGs
 * @author Joseph Ramsay
 *
 */
public class NeighbourListGenerator {

	/**
	 * geenric page header text
	 * @return
	 */
	public static String header(){
		return "# Neighbour List\n";
	}
	/**
	 * generic page footer text
	 * @return
	 */
	public static String footer(){
	    return "";
	}

	
	
	//---------------------------------------------------------------------------------------------
	
	public String showNetwork(PLMN plmn){
		String text = header();
		for (Cell cell : plmn.getCells()){
			for (Cell nbr : cell.getNeighbourList()){
				text += cell.getID()+","+cell.getName()+","+cell.getCellType()+",";
				text += nbr.getID()+","+nbr.getName()+","+nbr.getCellType();
				text += "\n";
				
			}
		}
		return text+footer();
	}
	
	public static void output(String filename,String output){
		Writer csvfile = new Writer(filename);
		csvfile.writeString(output);
	}	
}
