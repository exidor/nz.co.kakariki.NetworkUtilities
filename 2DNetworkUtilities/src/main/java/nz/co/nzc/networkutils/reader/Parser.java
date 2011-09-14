package nz.co.nzc.networkutils.reader;
/*
 * This file is part of 2DNetworkUtilities.
 *
 * 2DNetworkUtilities is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
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
import java.util.Arrays;
import java.util.List;

public abstract class Parser {

	protected String filename;
	protected List<DataArray> daplist;
	
	public Parser(String filename){
		daplist = new ArrayList<DataArray>();
		setFilename(filename);
	}
	
	public void addDataArray(DataArray da){
		daplist.add(da);
	}
	
	public List<String> readCSLine(String values){
		//System.out.println(values);
		//values = values.replaceAll(",,", ",-,-");
		//if(values.endsWith(",")) values=values.concat("-");
		//ArrayList<String> d = new ArrayList<String>(Arrays.asList(values.split(",",-1)));
		//System.out.println(d);
		return new ArrayList<String>(Arrays.asList(values.split(",",-1)));
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public List<DataArray> getDAPList() {
		return this.daplist;
	}
	
	public DataArray getDataArray(int index) {
		return this.daplist.get(index);
	}
}
