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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the primary data source, PRS CSV reports. Contains header
 * decoding and file bound checking routines. Subclasses for specific cases
 * but can otherwise be constructed from header information
 * Distributed version parses the output from PRS emailed/ftp'd files
 */
public class ParserNeighbour extends Parser {


	public ParserNeighbour(String filename){
		super(filename);
	}

	private String parseFileName(String filename){

		Pattern p = Pattern.compile("\\.+\\.csv$");
		Matcher m = p.matcher(filename.substring(filename.lastIndexOf(File.separator)));
		return m.find()?m.group(1):"NB"+String.valueOf(System.currentTimeMillis());
	}

	/**
	 * read and extracts header information to identify database target table
	 */
	public void readFile(FileInputStream fis) throws IOException {
		
		DataArray dac = new DataArrayNeighbour();

		String line = null;
		//Save Time :Wed Jun 15 12:01:31 NZST 2011 User name :Report Scheduled Task
		Pattern entrypattern = Pattern.compile("^{\\d{5},\\d{5}$");

		List<List<String>> data = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		while( (line = in.readLine()) != null){
			if(line.contains("#"))
				continue;
			
			Matcher entrymatch = entrypattern.matcher(line);

			if(entrymatch.find()) {
				data.add(readCSLine(line));
			}
		}

		dac.setData(data);
		daplist.add(dac);
	}
}