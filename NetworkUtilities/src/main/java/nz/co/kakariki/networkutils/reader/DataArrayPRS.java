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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Data Array Object representing a read from a single PRS file
 * @author Joseph Ramsay
 *
 */
public class DataArrayPRS implements DataArray{

	private List<String> head;
	private List<List<String>> data;
	private int records;
	private String tablename;
	private String username;
	private Calendar timestamp;
	//private Strategy strategy;
	
	private int integritycolumn = -1;


	@Override
	public List<String> getHead() {
		return head;
	}
	public void setHead(List<String> head){
		this.head = head;
		this.integritycolumn = head.indexOf("Integrity");
	}

	@Override
	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data){
		if(this.integritycolumn>-1)this.data = testIntegrity(data);
		else this.data = data;
		setRecords(this.data.size());
	}

	/**
	 * rebuild of data array to eliminate incomplete rows. PRS is special
	 * @return
	 * TODO look at quicker way to do this using IC column sort or array.delete
	 */
	public List<List<String>> testIntegrity(List<List<String>> olddata){
		List<List<String>> newdata = new ArrayList<>();
		for(List<String> row : olddata){
			if (Integer.parseInt(row.get(integritycolumn).replace("%",""))==100)
				newdata.add(row);
		}
		return newdata;
	}

	@Override
	public int getRecords() {
		return records;
	}
	public void setRecords(int records){
		this.records = records;
	}


	@Override
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename){
		this.tablename = tablename;
	}

	@Override
	public Calendar getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Calendar timestamp){
		this.timestamp = timestamp;
	}
	public void setTimestamp(String timestamp){
		this.timestamp = Calendar.getInstance();

		try {
			DateFormat sdf = new SimpleDateFormat(DF_SHORT);
			Date ds = sdf.parse(timestamp);
			this.timestamp.setTime(ds);
		} catch (ParseException pe1) {
			try{
				DateFormat ldf = new SimpleDateFormat(DF_LONG);
				Date dl = ldf.parse(timestamp);
				this.timestamp.setTime(dl);
			}
			catch(ParseException pe2){
				System.err.println("Couldn't parse date using the supplied formats "+DF_SHORT+" / "+DF_LONG+" :: check "+timestamp);
			}
		}

	}
	@Override
	public String getUsername() {
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	/*
	public Strategy getStrategy() {
		return strategy;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	*/

}
