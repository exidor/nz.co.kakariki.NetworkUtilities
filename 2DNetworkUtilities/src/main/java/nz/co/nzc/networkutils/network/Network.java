package nz.co.nzc.networkutils.network;

public abstract class Network {
	
	public int id;
	public String name;
	public Network parent;
	
	
	public Network(int id,String name){
		setID(id);
		setName(name);
	}
	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
	}

	public Network getParent() {
		return this.parent;
	}

	public void setParent(Network parent) {
		this.parent = parent;
	}

	public int compareTo(Network network) {
		return this.getID()-network.getID();
	}
}
