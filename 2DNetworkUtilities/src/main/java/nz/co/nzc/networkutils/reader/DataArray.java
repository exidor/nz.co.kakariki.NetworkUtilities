package nz.co.nzc.networkutils.reader;

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
