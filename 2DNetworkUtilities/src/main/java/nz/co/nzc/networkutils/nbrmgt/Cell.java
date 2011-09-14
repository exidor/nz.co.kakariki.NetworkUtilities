package nz.co.nzc.networkutils.nbrmgt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import com.vividsolutions.jts.geom.Coordinate;
public class Cell {

	private List<Cell> nbrs;
	private int cellid;

	public Sector parent;

	public enum CellType {
		G9("\\d{4}1","\\d{4}3","\\d{4}5"),
		G18("\\d{4}2","\\d{4}4","\\d{4}6"),
		U9("\\d{4}1","\\d{4}3","\\d{4}5"),
		U21F1("2\\d{4}7","2\\d{4}8","2\\d{4}9"),
		U21F2("4\\d{3}7","4\\d{3}8","4\\d{3}9"),
		U21F3("6\\d{3}7","6\\d{3}8","6\\d{3}9");

		Pattern s1,s2,s3;

		CellType(String s1, String s2, String s3){
			this.s1 = Pattern.compile(s1);
			this.s2 = Pattern.compile(s2);
			this.s3 = Pattern.compile(s3);
		}
	}

	public CellType celltype = CellType.U21F1;

	public Cell(int cellid){
		this.setCellID(cellid);
		nbrs = new ArrayList<>();
	}

	public int getCellID() {
		return cellid;
	}

	public void setCellID(int cellid) {
		this.cellid = cellid;
	}


	public Sector getParent() {
		return parent;
	}

	public void setParent(Sector sector) {
		this.parent = sector;
	}

	/**
	 * adds single cell neighbour
	 * @param nbr
	 */
	public void addNeighbourCell(Cell nbr){
		nbrs.add(nbr);
	}


	public void deleteNeighbour(Cell nbr){
		nbrs.remove(nbr);
	}

	public List<Cell> getNeighbourList(){
		return nbrs;
	}

	public CellType getCellType() {
		return celltype;
	}

	public void setCellType(CellType celltype) {
		this.celltype = celltype;
	}

	public String toString(){
		return "C:"+String.valueOf(this.cellid)+"/"+this.celltype;
	}

	public int getNeighbourCount(){
		return nbrs.size();
	}


	//comparators

	public void rankNeighboursByDistance(){		
		Collections.sort(nbrs,new GeometricDistanceComparator(this));
	}

	//simple pythagorean hypotenuse calculation
	public class PlanarDistanceComparator implements Comparator<Cell> {
		private Cell cell;
		
		public PlanarDistanceComparator(Cell cell){
			this.cell = cell;
		}
		public int compare(Cell c1, Cell c2) {
			return (int)Math.round(dispCartesianPlane(this.cell,c2));
		}
		public double dispCartesianPlane(Cell cell,Cell nbr){
			float latdisp = cell.getParent().getParent().getLocation().getLatitude()-nbr.getParent().getParent().getLocation().getLatitude();
			float lngdisp = cell.getParent().getParent().getLocation().getLongitude()-nbr.getParent().getParent().getLocation().getLongitude();
			return 1000*Math.sqrt(Math.pow(latdisp, 2)+Math.pow(lngdisp,2));

		}
	}

	//uses JTS coordinate distance calculation
	public class GeometricDistanceComparator implements Comparator<Cell> {
		private Cell cell;
		
		public GeometricDistanceComparator(Cell cell){
			this.cell = cell;
		}
		public int compare(Cell c1, Cell c2) {
			return (int) Math.round(dispCartesianGeometry(this.cell,c2));
		}
		public double dispCartesianGeometry(Cell cell,Cell nbr){
			float clat = cell.getParent().getParent().getLocation().getLatitude();
			float nlat = nbr.getParent().getParent().getLocation().getLatitude();
			float clng = cell.getParent().getParent().getLocation().getLongitude();
			float nlng = nbr.getParent().getParent().getLocation().getLongitude();
			//System.out.println(">>>"+cell+":"+nbr+"-"+1000*(new Coordinate(clng,clat)).distance(new Coordinate(nlng,nlat)));
			return 1000*(new Coordinate(clng,clat)).distance(new Coordinate(nlng,nlat));
		}
	}

}
