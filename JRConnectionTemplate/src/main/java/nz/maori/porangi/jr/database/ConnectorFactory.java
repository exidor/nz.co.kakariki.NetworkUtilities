package nz.maori.porangi.jr.database;

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
import java.util.logging.Logger;


/**
 * Factory class to select JDC connector types based on the required database type
 * @author jnramsay
 *
 */
public class ConnectorFactory {

	private static Logger jlog = Logger.getLogger("nz.maori.porangi.jr.database.ConnectorFactory");

	public static DatabaseType DEFTYPE = DatabaseType.POSTGRES_TYPE;

	private DatabaseType currentType;

	/**
	 * Null constructor initialising with the default DB type
	 */
	public ConnectorFactory(){
		this(DEFTYPE);
	}
	
	//constructors

	public ConnectorFactory(String type){
		this(DatabaseType.valueOf(type.toUpperCase()));
	}
	
	public ConnectorFactory(DatabaseType type){
		this.currentType = type;
		jlog.info("DBT:"+type);
	}

	/**
	 * Returns an instance of the requested JDC connector. Defaults to Test 
	 * or null if the you request something that doesnt exist
	 * @return JDC instance
	 */
	public Connector getInstance(){
		ConnectionDefinitions cd = currentType.getConnectionDefinitions();
		//return new TestJDCConnector(cd);

		switch(currentType){
		case POSTGRES_TYPE:
			return new PostgreSQLConnector(cd);
		case MYSQL_TYPE:
			return new MySQLConnector(cd);
		default:
			return null;
		}

	}

}
