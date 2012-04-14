package nz.co.kakariki.networkutils.network;
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
public class IDUtilities {
	
	/* 12345/10 = 1234.5
	 * round(1234.5) = 1234
	 * 1234*10 = 12340
	 * 12345-12340 = 5
	 */
	public static int cellpart(int ci){
		return ci-10*Math.round(ci/10);
	}
	
	/* 23456/10000 = 2.3456
	 * round(2.3456) = 2
	 * 2*10000 = 20000
	 */
	public static int typepart(int ci){
		return 10000*Math.round(ci/10000);
	}
	
	/* 23456 - 20000 = 3456
	 * 3456 - 6 = 3450
	 */
	public static int nodebid(int ci){
		return ci-cellpart(ci)-typepart(ci);	
	}
	
	public static int sectorid(int ci){
		switch (cellpart(ci)){
		case 1:
		case 2:
		case 7: return 1;
		case 3:
		case 4:
		case 8: return 2;
		case 5:
		case 6:
		case 9: return 3;
		}

		return 0;
	}
}
