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
import java.util.ArrayList;
import java.util.List;

/**
 * Un attached neighbour list containing two lists of cell ids intended to be integrated into 
 * PLMN object
 * @author Joseph Ramsay
 *
 */
public class Neighbour {	
	
	/**
	 * container class for single neighbour, cell to cell relation
	 */
	public class NeighbourPair {
		public NeighbourPair(int cell1, int cell2){
			this.source = cell1;
			this.target = cell2;
		}
		int source,target;
		public int getSource() {return source;}
		public void setSource(int source) {this.source = source;}
		public int getTarget() {return target;}
		public void setTarget(int target) {this.target = target;}
		
	}
	
	public List<NeighbourPair> neighbourlist; 	

	public List<NeighbourPair> getNeighbourList() {
		return neighbourlist;
	}

	public Neighbour(){
		this.neighbourlist = new ArrayList<>();
	}

	public void addNeighbour(int cell1, int cell2){
		NeighbourPair neighbourpair = new NeighbourPair(cell1,cell2);
		neighbourlist.add(neighbourpair);
	}
	

}
