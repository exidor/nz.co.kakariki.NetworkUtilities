package nz.co.nzc.networkutils.network;
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
import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;


public class Location {
	
	private static Logger jlog = Logger.getLogger("nz.co.nzc.networkutils.network.Location");
	
	public static final double DEF_LAT = -41.280917d;
	public static final double DEF_LNG = 174.779557d; 
	//http://www.csgnetwork.com/degreelenllavcalc.html for latitude DEF_LAT
	public static final double DEF_XOFFSET = 84135.15;
	public static final double DEF_YOFFSET = 111053.88;
	
	public static final float DEF_RADIUS = 200.0f;//50m sector radius
	
	private Coordinate coordinate;
	
	private String address;
	
	public Location(){
		this(DEF_LAT,DEF_LNG);
	}
	
	public Location(String address){
		this();
		setAddress(address);
	}
	public Location(double latitude, double longitude){
		this.coordinate = new Coordinate();
		setLatitude(latitude);
		setLongitude(longitude);
	}

	

	public double getLatitude() {
		return this.getCoordinate().y;
	}
	public void setLatitude(double latitude) {
		this.getCoordinate().y = latitude;
	}
	public double getLongitude() {
		return this.getCoordinate().x;
	}
	public void setLongitude(double longitude) {
		this.getCoordinate().x = longitude;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	//TODO. check direction of 0degrees, north and CW?
	public Coordinate projectedCoordinate(float angle){
		return projectedCoordinate(angle,DEF_RADIUS);
	}
	
	public Coordinate projectedCoordinate(float angle, float radius){
		if(angle==0)jlog.warn("Zero Azimuth projection request");
		Coordinate shifted = new Coordinate(this.coordinate);
		shifted.x += Math.sin(Math.toRadians(angle))*radius/DEF_XOFFSET;
		shifted.y += Math.cos(Math.toRadians(angle))*radius/DEF_YOFFSET;
		//System.out.println(angle+" :: "+coordinate+" >>> "+shifted);
		return shifted;
		
	}
}