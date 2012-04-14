package nz.co.kakariki.databaseutilities;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Connector implementation for the test database. 
 * @author jnramsay
 *
 */
public class PostgreSQLConnector implements Connector{

	private static Logger jlog = Logger.getLogger("nz.co.nzcomms.capacitydb.database.MySQLConnector");

	ConnectionDefinitions connectiondefinitions;

	private static final String DEF_USER = "pguser";
	private static final String DEF_PASS = "pgpass";
	private static final String DEF_HOST = "127.0.0.1";//? wherever we end up putting the real server
	private static final String DEF_PORT = "5432";
	private static final String DEF_DBNM = "test";

	/*
	static {
	try{
		new JDCConnectionDriver("org.postgresql.Driver","jdbc:postgresql","pguser","pgpass");
	}
	catch(Exception e){
		System.err.println("Error building JDCConnectionDriver :: "+e);
		
	}
	}
	*/
	
	public PostgreSQLConnector(){
		this(ConnectionDefinitions.POSTGRESQL);
	}
	public PostgreSQLConnector(ConnectionDefinitions cd){
		setConnectionDefinitions(cd);
		init();
	}

	public void init(){
		init(DEF_USER,DEF_PASS);
	}
	
	public void init(String user,String pass){
		jlog.info("Setup connection CDB :: "+url());
	
		try{
			new ConnectionDriver(driver(),url(),user, pass);
		}
		catch(Exception e){
			System.err.println("Error building ConnectionDriver :: "+e);
			
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:jdc:jdcpool");
	}


	public ConnectionDefinitions getConnectionDefinitions() {
		return connectiondefinitions;
	}

	public void setConnectionDefinitions(ConnectionDefinitions cd) {
		this.connectiondefinitions = cd;
	}

	private String url(String host,String port, String db){
		return connectiondefinitions.prefix()+"://"+host+":"+port+"/"+db;
	}

	private String url(){
		return url(DEF_HOST,DEF_PORT,DEF_DBNM);
	}
	
	private String driver(){
		return connectiondefinitions.driver();
	}
	
	@Override
	public String toString(){
		return "CDB:"+connectiondefinitions.driver();
	}

}
