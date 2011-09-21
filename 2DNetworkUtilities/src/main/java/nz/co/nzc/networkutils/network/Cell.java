package nz.co.nzc.networkutils.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Cell extends Network {

	public static final String DEF_NM = "CellName";
	public static final String DEF_ADDR = "CellAddress";
	
	
	private List<Cell> nbrs;

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

	public Cell(int id,String name){
		super(id,name);
		nbrs = new ArrayList<>();
	}
	
	public Cell(int id){
		this(id,DEF_NM+id);
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
	
	public void setNeighbourList(List<Cell> nbrs){
		this.nbrs = nbrs;
	}

	public CellType getCellType() {
		return celltype;
	}

	public void setCellType(CellType celltype) {
		this.celltype = celltype;
	}

	public String toString(){
		return "C:"+this.getID()+"/"+this.getName()+"/"+this.getCellType();
	}

	public int getNeighbourCount(){
		return nbrs.size();
	}


	//comparators

	public void truncateNeighbourList(int size){
		rankNeighboursByDistance();
		setNeighbourList(nbrs.size()>size?nbrs.subList(0, size):nbrs);
	}
	
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
			return (int)Math.round(displacementCartesianPlane(this.cell,c2));
		}

		public double displacementCartesianPlane(Cell c1, Cell c2) {
			double disp1 = Math.sqrt(
					Math.pow(
				    (((Sector) this.cell.getParent()).getSectorCoordinate().y
				    -((Sector)        c1.getParent()).getSectorCoordinate().y),
				    2)		+ 
					Math.pow(
					(((Sector) this.cell.getParent()).getSectorCoordinate().x
					-((Sector)        c1.getParent()).getSectorCoordinate().x),
					2));
			double disp2 = Math.sqrt(
					Math.pow(
				    (((Sector) this.cell.getParent()).getSectorCoordinate().y
				    -((Sector)        c2.getParent()).getSectorCoordinate().y),
				    2)		+ 
					Math.pow(
					(((Sector) this.cell.getParent()).getSectorCoordinate().x
					-((Sector)        c2.getParent()).getSectorCoordinate().x),
					2));
			return 1000 * (disp1 - disp2);

		}
	}

	//uses JTS coordinate distance calculation
	public class GeometricDistanceComparator implements Comparator<Cell> {
		private Cell cell;
		
		public GeometricDistanceComparator(Cell cell){
			this.cell = cell;
		}
		public int compare(Cell c1, Cell c2) {
			return (int) Math.round(displacementCartesianGeometry(c1,c2));
		}
		public double displacementCartesianGeometry(Cell c1,Cell c2){
			//System.out.println(">>>"+cell+":"+nbr+"-"+1000*(new Coordinate(clng,clat)).distance(new Coordinate(nlng,nlat)));
			double disp1 = ((Sector)this.cell.getParent()).getSectorCoordinate()
					.distance(((Sector)c1.getParent()).getSectorCoordinate());
			double disp2 = ((Sector)this.cell.getParent()).getSectorCoordinate()
					.distance(((Sector)c2.getParent()).getSectorCoordinate());
			return 1000*(disp1-disp2);
		}
	}

}
