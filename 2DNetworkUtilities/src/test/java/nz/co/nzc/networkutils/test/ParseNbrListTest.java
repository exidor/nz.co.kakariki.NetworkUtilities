package nz.co.nzc.networkutils.test;

import static org.junit.Assert.assertTrue;
import nz.co.nzc.networkutils.map.KMLGenerator;
import nz.co.nzc.networkutils.nbrmgt.NetworkManager;
import nz.co.nzc.networkutils.nbrmgt.NetworkTestBuilder;
import nz.co.nzc.networkutils.network.PLMN;
import nz.co.nzc.networkutils.network.RNC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParseNbrListTest {

	String filename = "conf/sample1.csv";
	NetworkTestBuilder bn = new NetworkTestBuilder();
	
	
	PLMN plmn;
	RNC rnc;
	
	
	@Before
	public void setUp() throws Exception {
		plmn = NetworkManager.readNetwork(filename);	
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
		kmlg.outputKML("conf/neighbour.kml", kmlg.getKML(plmn.getRNCs().get(0)));
		assertTrue(true);
	}
}
