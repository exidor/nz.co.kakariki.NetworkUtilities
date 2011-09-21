package nz.co.nzc.networkutils.test;

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
