import java.util.ArrayList;


public class Node {
	int id;
	String binary;
	double energy;
	int level;
	ArrayList<Integer> neighboursList;

	public Node() {
		id = -1;
		binary = null;
		energy = 0.0;
		level = -1;
		neighboursList = new ArrayList<Integer>();
	}
	
	public Node(int id, String binary, double energy, int level, int[] neighbours) {
		this();
		this.id = id;
		this.binary = binary;
		this.energy = energy;
		this.level = level;
		for (int i : neighbours) {
			neighboursList.add(i);
		}
	}
	
	public void print(){
		System.out.println("Id : " + id);
		System.out.println("Binary : " + binary);
		System.out.println("Energy : " + energy);
		System.out.println();
	}
	
}
