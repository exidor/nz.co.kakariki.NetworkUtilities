package nz.co.nzc.networkutils.reader;

/*
 * This file is part of CapacityDB.
 *
 * CapacityDB is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * CapacityDB is distributed in the hope that it will be useful,
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the primary data source, PRS CSV reports. Contains header
 * decoding and file bound checking routines. Subclasses for specific cases
 * but can otherwise be constructed from header information
 * Distributed version parses the output from PRS emailed/ftp'd files
 */
public class ParserPRS extends Parser {


	public ParserPRS(String filename){
		super(filename);
	}

	private String parseFileName(String filename){
		//"___report-datetime.subreport.csv" pattern

		//Pattern p = Pattern.compile("\\w+\\.(.+)\\.csv$");
		Pattern p = Pattern.compile("\\d{14}\\.(.+)\\.csv$");
		Matcher m = p.matcher(filename.substring(filename.lastIndexOf(File.separator)));
		return m.find()?m.group(1):"DC"+String.valueOf(System.currentTimeMillis());
	}

	/**
	 * read and extracts header information to identify database target table
	 */
	public void readFile(FileInputStream fis) throws IOException {
		DataArray dap = new DataArrayPRS();

		String line = null;
		//Save Time :Wed Jun 15 12:01:31 NZST 2011 User name :Report Scheduled Task
		Pattern p = Pattern.compile("Save Time :(\\w+ \\w+ \\d{1,2} \\d{1,2}:\\d{2}:\\d{2} \\w{4} \\d{4}) User name :(.*)");
		List<List<String>> data = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		while( (line = in.readLine()) != null){
			//System.out.println(line);
			Matcher m = p.matcher(line);
			if(m.find()) {
				dap.setTimestamp(m.group(1));//ts
				dap.setUsername(m.group(2));

			}
			else if(line.contains("Time,")){
				dap.setHead(readCSLine(line));
			}
			/* TODO Check if Java Bug? Pattern.matches arguments are not passed.
			String p = "^\\d{2}\\/\\d{2}\\/\\d{4} \\d{2}:\\d{2},";
			if(line.matches(p)){
				System.out.println("BB");
				data.add(readBody(line));
			}
			System.out.println("regex1::"+line+"::"+java.util.regex.Pattern.matches(p,line));
			System.out.println("regex2::"+line+"::"+line.matches(p.toString()));
			*/
			//Possible source of errors having to test for : and / in date string
			else if(line.contains("-")&line.contains(":")) {
				data.add(readCSLine(line));
			}
		}
		int recs1 = data.size();
		int recs2 = dap.getRecords();
		if(recs2>0 && recs1!=recs2) System.err.println("Record count disagreement :: "+recs1+"!="+recs2);
		dap.setRecords(data.size());
		dap.setData(data);
		dap.setTablename(parseFileName(this.filename));
		daplist.add(dap);
	}


}
/* Distributed Header sequence, Main report, timestamp, user, null?, lf, record count, lf, table head
HSUPA.Upg.Monitor
Save Time :Wed Jun 15 12:01:31 NZST 2011 User name :Report Scheduled Task
Time,NodeB,IUB Bandwidth Util Ratio (DL),...
*/