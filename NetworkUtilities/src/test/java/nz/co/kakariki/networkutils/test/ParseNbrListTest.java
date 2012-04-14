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
import nz.co.kakariki.networkutils.map.KMLGenerator;
import nz.co.kakariki.networkutils.nbrmgt.NetworkManager;
import nz.co.kakariki.networkutils.nbrmgt.NetworkTestBuilder;
import nz.co.kakariki.networkutils.network.PLMN;
import nz.co.kakariki.networkutils.network.RNC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParseNbrListTest {

	String filename1 = "conf/sample1.csv";
	String filename2 = "conf/sample1.csv";
	NetworkTestBuilder bn = new NetworkTestBuilder();
	
	
	PLMN plmn;
	RNC rnc;
	
	
	@Before
	public void setUp() throws Exception {
		plmn = NetworkManager.readNetwork(filename1);	
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void readConfig(){
		NetworkManager.neighbourStrategy(plmn);
		NetworkManager.displayAll(plmn);
		KMLGenerator kmlg = new KMLGenerator();
		System.out.println(kmlg.getKML(plmn.getRNCs().get(0)));
		kmlg.output("conf/neighbour.kml", kmlg.getKML(plmn.getRNCs().get(0)));
		assertTrue(true);
	}
}
