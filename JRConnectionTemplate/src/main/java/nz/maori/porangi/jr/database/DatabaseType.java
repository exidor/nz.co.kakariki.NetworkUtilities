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

/**
 * Current crop of selectable database types. Add and remove from this enum as required
 * remembering the definitions enum may need to be updated if you are not using a supported
 * database.
 */
public enum DatabaseType {
	POSTGRES_TYPE(ConnectionDefinitions.POSTGRESQL),
	MYSQL_TYPE(ConnectionDefinitions.MYSQL);

	private ConnectionDefinitions connectiondefinitions;

	DatabaseType(ConnectionDefinitions connectiondefinitions)	{
		this.connectiondefinitions = connectiondefinitions;
	}
	public ConnectionDefinitions getConnectionDefinitions(){
		return connectiondefinitions;
	}
};