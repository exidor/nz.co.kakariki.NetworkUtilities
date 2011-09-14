package nz.co.nzc.networkutils.test;



import nz.co.nzc.networkutils.test.BuildNetworkTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BuildNetworkTest.class })
public class AllTests {
	
	/*
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(BuildNetworkTest.class);

		return suite;
	}
	*/

	/**
	 * Runs the test suite using the textual runner.
	 */
	/*
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}*/
}
