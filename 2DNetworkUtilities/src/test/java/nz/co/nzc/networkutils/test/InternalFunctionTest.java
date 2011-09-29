package nz.co.nzc.networkutils.test;
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
import nz.co.nzc.networkutils.map.KMLGenerator;
import nz.co.nzc.networkutils.nbrmgt.NetworkManager;
import nz.co.nzc.networkutils.nbrmgt.NetworkTestBuilder;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.RNC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalFunctionTest {

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
	public void coordProjection(){
		NetworkManager.neighbourStrategy(plmn);
		RNC rnc = plmn.getRNCs().get(0);
		KMLGenerator kmlg = new KMLGenerator();
		System.out.println(kmlg.getKML(rnc));
	}
	
}
