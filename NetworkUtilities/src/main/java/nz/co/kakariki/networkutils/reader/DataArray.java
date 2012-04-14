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
import java.util.Calendar;
import java.util.List;

public interface DataArray {
	
	//14/06/2011 10:08:47
	public static final String DF_SHORT = "dd/MM/yyyy hh:mm";
	//Wed Jun 15 12:01:31 NZST 2011
	public static final String DF_LONG = "EEE MMM dd hh:mm:ss zzzz yyyy";
	
	public List<String> getHead();
	public void setHead(List<String> head);
	public List<List<String>> getData();
	public void setData(List<List<String>> data);
	
	public int getRecords();
	public void setRecords(int records);
	public String getTablename();
	public void setTablename(String tablename);
	
	public String getUsername();
	public void setUsername(String username);
	public Calendar getTimestamp();
	public void setTimestamp(Calendar timestamp);
	public void setTimestamp(String timestamp);
}
