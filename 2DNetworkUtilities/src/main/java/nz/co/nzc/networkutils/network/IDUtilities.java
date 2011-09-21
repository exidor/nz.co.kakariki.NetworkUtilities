package nz.co.nzc.networkutils.network;

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
