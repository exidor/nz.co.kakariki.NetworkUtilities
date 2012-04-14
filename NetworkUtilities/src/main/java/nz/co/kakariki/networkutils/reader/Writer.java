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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

	protected String filename;
	
	public Writer(String filename){	
		setFilename(filename);
	}
	
	public void writeString(String output){
		try (BufferedWriter out = new BufferedWriter(new FileWriter(getFilename()))){
			out.write(output);
			out.close();
		} catch (IOException ioe) {
			System.out.println("Error writing "+getFilename()+"::"+ioe);

		}
		
	}
	

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

}
