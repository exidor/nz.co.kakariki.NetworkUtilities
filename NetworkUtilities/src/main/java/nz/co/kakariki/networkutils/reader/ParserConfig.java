package nz.co.kakariki.networkutils.reader;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the primary data source, PRS CSV reports. Contains header
 * decoding and file bound checking routines. Subclasses for specific cases
 * but can otherwise be constructed from header information
 * Distributed version parses the output from PRS emailed/ftp'd files
 */
public class ParserConfig extends Parser {


	public ParserConfig(String filename){
		super(filename);
	}

	private String parseFileName(String filename){
		//"___report-datetime.subreport.csv" pattern

		//Pattern p = Pattern.compile("\\w+\\.(.+)\\.csv$");
		Pattern p = Pattern.compile("\\.+\\.csv$");
		Matcher m = p.matcher(filename.substring(filename.lastIndexOf(File.separator)));
		return m.find()?m.group(1):"DC"+String.valueOf(System.currentTimeMillis());
	}

	/**
	 * read and extracts header information to identify database target table
	 */
	public void readFile(FileInputStream fis) throws IOException {
		
		DataArray dac = new DataArrayConfig();

		String line = null;
		//Save Time :Wed Jun 15 12:01:31 NZST 2011 User name :Report Scheduled Task
		Pattern pstr = Pattern.compile("^Strategy\\s+(.*)");
		Pattern preg = Pattern.compile("^Region\\s+(.*)");
		Pattern pprj = Pattern.compile("^Projection\\s+(.*)");
		List<List<String>> data = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		while( (line = in.readLine()) != null){
			if(line.contains("#"))
				continue;
			
			Matcher mreg = preg.matcher(line);
			Matcher mstr = pstr.matcher(line);
			Matcher mprj = pprj.matcher(line);
			if(mstr.find()) {
				//((DataArrayConfig)dac).setStrategy(Strategy.valueOf(mstr.group(1)));
				data.add(new ArrayList<>(Arrays.asList("Strategy",mstr.group(1))));
			}
			else if(mreg.find()){
				//((DataArrayConfig)dac).setRegion(mreg.group(1));
				data.add(new ArrayList<>(Arrays.asList("Region",mreg.group(1))));
			}
			else if(mprj.find()){
				//((DataArrayConfig)dac).setRegion(mprj.group(1));
				data.add(new ArrayList<>(Arrays.asList("Projection",mprj.group(1))));
			}
			else {
				data.add(readCSLine(line));
			}
		}

		/*
		int recs1 = data.size();
		int recs2 = dac.getRecords();
		if(recs2>0 && recs1!=recs2) System.err.println("Record count disagreement :: "+recs1+"!="+recs2);
		dac.setRecords(data.size());
		*/
		dac.setData(data);
		//dac.setTablename(parseFileName(this.filename));
		daplist.add(dac);
	}


}
/* 
#Sample Neighbour List
#Region [ARM,SJH,KPR] to hopefully avoid any cellid collisions
#Strategy [Peak,Normal] marker to set strategy per cell as list is read if not inline
#CellID, Longitude, Latitude,Azimuth,CellType
Region SJH
Strategy Peak
#Test2
10001,140.0,-41.0,60,G9
*/