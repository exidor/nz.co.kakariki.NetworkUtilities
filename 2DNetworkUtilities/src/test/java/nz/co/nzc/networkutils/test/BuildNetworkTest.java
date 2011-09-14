package nz.co.nzc.networkutils.test;

import static org.junit.Assert.assertTrue;
import nz.co.nzc.networkutils.nbrmgt.Cell;
import nz.co.nzc.networkutils.nbrmgt.PLMN;
import nz.co.nzc.networkutils.nbrmgt.Sector.Strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BuildNetworkTest {

	PLMN plmn;
	BuildNetwork bn = new BuildNetwork();
	
	@Before
	public void setUp() throws Exception {
		this.plmn = BuildNetwork.generateNewTestPLMN();
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
		BuildNetwork.neighbourStrategy(plmn, s);
		BuildNetwork.displayAll(plmn);
		System.out.println("NL size. "+plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size());
		
		assertTrue(plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size()==NLS);
		
	}
	
	@Test
	public void peakStrategy(){
		final int NLS = 17;
		Strategy s = Strategy.Peak;
		System.out.println("Test of "+s+" neighbouring strategy");
		BuildNetwork.neighbourStrategy(plmn, s);	
		BuildNetwork.displayAll(plmn);
		System.out.println("NL size. "+plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size());
		
		assertTrue(plmn.getRNC("ABC").getNodeBs().get(0).getSectors().get(0).getCells().get(0).getNeighbourList().size()==NLS);
		
	}
	
	@Test
	public void distanceRanking(){
		Strategy s = Strategy.Normal;
		System.out.println("Test of distance ranking");
		BuildNetwork.neighbourStrategy(plmn, s);
		
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