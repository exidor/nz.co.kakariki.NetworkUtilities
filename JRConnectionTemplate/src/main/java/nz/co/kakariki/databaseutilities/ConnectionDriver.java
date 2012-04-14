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
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
//import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


public class ConnectionDriver implements Driver {

	public static final String URL_PREFIX = "jdbc:jdc:";
	private static final int MAJOR_VERSION = 1;
	private static final int MINOR_VERSION = 0;
	private ConnectionPool pool;

	public ConnectionDriver(String driver, String url, String user, String pass)
	throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		DriverManager.registerDriver(this);
		Class.forName(driver).newInstance();
		pool = new ConnectionPool(url, user, pass);
	}

	@Override
	public Connection connect(String url, Properties props)
	throws SQLException {
		if(!url.startsWith(URL_PREFIX))
			return null;//throw new SQLException("Requested prefix not supported "+url+"!="+URL_PREFIX);
		return pool.getConnection();
	}

	@Override
	public boolean acceptsURL(String url) {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public int getMajorVersion() {
		return MAJOR_VERSION;
	}

	@Override
	public int getMinorVersion() {
		return MINOR_VERSION;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String str, Properties props) {
		return new DriverPropertyInfo[0];
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

/* Java 1.7 JDBC 4 method */

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
