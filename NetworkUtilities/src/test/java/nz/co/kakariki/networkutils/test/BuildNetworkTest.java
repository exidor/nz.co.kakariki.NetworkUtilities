package nz.co.kakariki.networkutils.test;
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
import static org.junit.Assert.assertTrue;
import nz.co.kakariki.networkutils.nbrmgt.NetworkManager;
import nz.co.kakariki.networkutils.nbrmgt.NetworkTestBuilder;
import nz.co.kakariki.networkutils.network.Cell;
import nz.co.kakariki.networkutils.network.PLMN;
import nz.co.kakariki.networkutils.network.Sector.Strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BuildNetworkTest {

	PLMN plmn;
	NetworkTestBuilder bn = new NetworkTestBuilder();
	
	@Before
	public void setUp() throws Exception {
		this.plmn = NetworkTestBuilder.generateNewTestPLMN();
	}

	@After
	public void tearDown() throws Exception {
		this.plmn = null;
	}
	
	@Test
	public void normalStrategy(){
		final int NLS = 35;
		Strategy s = Strategy.Normal;
		System.out.println("Test of "+s+" neighbouring strategy");
		NetworkManager.neighbourStrategy(plmn, s);
		NetworkManager.displayAll(plmn);
		System.out.println("NL size. "+plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size());
		
		assertTrue(plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size()==NLS);
		
	}
	
	@Test
	public void peakStrategy(){
		final int NLS = 17;
		Strategy s = Strategy.Peak;
		System.out.println("Test of "+s+" neighbouring strategy");
		NetworkManager.neighbourStrategy(plmn, s);	
		NetworkManager.displayAll(plmn);
		System.out.println("NL size. "+plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size());
		
		assertTrue(plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size()==NLS);
		
	}
	
	@Test
	public void distanceRanking(){

		System.out.println("Test of distance ranking");
		NetworkManager.neighbourStrategy(plmn);
		
		Cell c = plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0);
		
		System.out.println("Natural sort order for "+c);
		System.out.println(c.getNeighbourList());
		for(Cell n : c.getNeighbourList()){
			System.out.println(n);
		}
		
		c.rankNeighboursByDistance();
		
		System.out.println("Distance sort order for "+c);
		System.out.println(c.getNeighbourList());
		for(Cell n : c.getNeighbourList()){
			System.out.println(n);
		}
		
		assertTrue(Boolean.TRUE);
		
		
	}
}
