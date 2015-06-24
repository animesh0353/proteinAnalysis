import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;

import javax.swing.filechooser.FileNameExtensionFilter;





public class Graph {

	HashMap<String, Node> binaryToNodeMap ;
	HashMap<Integer, String> idToBinaryMap ;
	int size;
	
	public Graph() {
		// TODO Auto-generated constructor stub
		binaryToNodeMap = new HashMap<String, Node>();
		idToBinaryMap = new HashMap<Integer, String>();
	}
	
	public void initialize() {
		Scanner in = null;
		try {
			in = new Scanner(new File("DataPrecompute.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int id;
		String binary;
		double energy;
		int level;
		int neighbours;
		
		String string = in.nextLine(); // to remove the header from the file  
		System.out.println("loading ...  with following attribute " + string );
		while( in.hasNextLine()) {
			string = in.nextLine();
			String[] str = string.split(",");
			id = Integer.parseInt(str[0]);
			binary = str[1];
			energy = Double.parseDouble(str[2]);
			level = Integer.parseInt(str[3]);
			int[] arr = new int[str.length - 4];
			for (int j = 0; j < arr.length; j++) {
				arr[j] = Integer.parseInt(str[j+4]);
			}
			Node node = new Node(id, binary, energy, level, arr);
			binaryToNodeMap.put(binary, node);
			idToBinaryMap.put(id, binary);
		}
		size = binaryToNodeMap.size();
	}
	
	public int montecarloWalk(int start, int steps_taken[]) {
		int T = 298; // Temperature
		double R = 0.008314; // R constant
		int nsteps = 30000;     //100000;
		int[] trace = new int[nsteps];
		int run = 500;  // Number of Run from each base step
		String fileName =  "Steps_Taken_monteCarlo_"+Integer.toString(run)+ "run.csv";

		int[][] level = new int[nsteps][run];
		int max = 1;
		
		for (int i = 0; i < run; i++) {


			Random rand = new Random();
			Node node = null;
			String binary = idToBinaryMap.get(start);
			node = binaryToNodeMap.get(binary);
			trace[0] = node.id;
			level[0][i] = node.level;
			
			for (int j = 1; j < nsteps; j++) {

				int numOfNeighbours = node.neighboursList.size();
				int randomNum = rand.nextInt(numOfNeighbours);
				Node nextNode = binaryToNodeMap.get(idToBinaryMap.get(node.neighboursList.get(randomNum)));
				double bowlt = Math.exp(- (nextNode.energy - node.energy) / (R * T) );
				if(bowlt > 1.0 || bowlt > rand.nextDouble())
					node = nextNode;
				trace[j] = node.id;
				level[j][i] = node.level;
//							if(max < node.level)
//								max = node.level;
//							if(max == 56)
//							{ steps_taken[0] = j;
//								break;
//							}

			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fileWriter);
//			bw.write("Level,NodeId");
			for (int i = 0; i < nsteps; i++) {
				StringBuffer bf = new StringBuffer();
				bf.append(i);
				bw.write(bf.toString());
				for (int j = 0; j < run; j++) {
					StringBuffer bufr = new StringBuffer();
					bufr.append(",");
					bufr.append(level[i][j]);
					bw.write(bufr.toString());
				}
				bw.write("\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return max;
	}
	
	public int montecarloWalkTime(int start, int steps_taken[]) {
		int T = 298; // Temperature
		double R = 0.008314; // R constant
		int nsteps = 30000;     //100000;
		int[] trace = new int[nsteps];
		int run = 1000;  // Number of Run from each base step
		String fileName =  "Steps_Taken_monteCarlo_"+Integer.toString(run)+ "run.csv";

		long[] time = new long[run];
		int[][] level = new int[nsteps][run];
		int max = 1;
		
		for (int i = 0; i < run; i++) {

			max = 1;
			long startTime = System.nanoTime();

			Random rand = new Random();
			Node node = null;
			String binary = idToBinaryMap.get(start);
			node = binaryToNodeMap.get(binary);
			trace[0] = node.id;
			level[0][i] = node.level;
			
			for (int j = 1; j < nsteps; j++) {

				int numOfNeighbours = node.neighboursList.size();
				int randomNum = rand.nextInt(numOfNeighbours);
				Node nextNode = binaryToNodeMap.get(idToBinaryMap.get(node.neighboursList.get(randomNum)));
				double bowlt = Math.exp(- (nextNode.energy - node.energy) / (R * T) );
				if(bowlt > 1.0 || bowlt > rand.nextDouble())
					node = nextNode;
				trace[j] = node.id;
				level[j][i] = node.level;
				if(max < node.level)
					max = node.level;
				if(max == 56)
				{ 
					steps_taken[0] = j;
					long endTime = System.nanoTime();
//					time[i] = endTime - startTime;
					time[i] = j;
					break;
				}

			}
		}
		FileWriter fileWriter = null;
		BufferedWriter bw = null;
		try {
			
			File file = new File(fileName);
			if(!file.exists())
			{
				file.createNewFile();
				fileWriter = new FileWriter(file);
				bw = new BufferedWriter(fileWriter);
				bw.write("Starting Sequence ID");
				for (int i = 1; i <= run; i++) {
					bw.write(","+i);
				}
			}
			else {
				fileWriter = new FileWriter(file,true);
				bw = new BufferedWriter(fileWriter);
			}
//			bw.write("Level,NodeId");
			bw.write("\n"+start);
			for (int i = 0; i < run; i++) {
					bw.write(","+time[i]);
				}
				
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return max;
	}
	
	
	
	
	public int randomWalkEnergyDiff(int start, int steps_taken[] ) {
		int T = 298; // Temperature
		double R = 0.008314; // R constant
		int nsteps = 100000;
		int[] trace = new int[nsteps];
		int[] level = new int[nsteps];
		
		Random rand = new Random();
		Node node = null;
		String binary = idToBinaryMap.get(start);
		node = binaryToNodeMap.get(binary);
		trace[0] = node.id;
		level[0] = node.level;
		int max = node.level;
		for (int j = 1; j < nsteps; j++) {
			
			int numOfNeighbours = node.neighboursList.size();
			int randomNum = rand.nextInt(numOfNeighbours);
			Node nextNode = binaryToNodeMap.get(idToBinaryMap.get(node.neighboursList.get(randomNum)));
			double bowlt = Math.exp(- (nextNode.energy - node.energy) / (R * T) );
			if(bowlt > 1.0 )
				node = nextNode;
			trace[j] = node.id;
			level[j] = node.level;
			if(max < node.level)
				max = node.level;
			if(max == 56)
			{ steps_taken[1] = j;
				break;
			}
			
		}
//		FileWriter fileWriter = null;
//		try {
//			fileWriter = new FileWriter("monteCarlo.csv");
//			BufferedWriter bw = new BufferedWriter(fileWriter);
//			bw.write("Level,NodeId");
//			for (int i = 0; i < level.length; i++) {
//				bw.write(level[i]+","+trace[i]+"\n");
//			}
//			
//			bw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
		return max;
	}
	
	public int randomWalkEnergyDiff_probability(int start, int steps_taken[] ) {
		int T = 298; // Temperature
		double R = 0.008314; // R constant
		int nsteps = 100000;
		int[] trace = new int[nsteps];
		int[] level = new int[nsteps];
		
		Random rand = new Random();
		Node node = null;
		String binary = idToBinaryMap.get(start);
		node = binaryToNodeMap.get(binary);
		trace[0] = node.id;
		level[0] = node.level;
		int max = node.level;
		Node nextNode = null;
		
		for (int j = 1; j < nsteps; j++) {
			
			int numOfNeighbours = node.neighboursList.size();
			
			Iterator<Integer> itr = node.neighboursList.iterator();
			double sum = 0.0;
			ArrayList<Double> energyList = new ArrayList<Double>();
			double energy2 = 0.0;
			while (itr.hasNext()) {
				int itemId = itr.next();
				energy2 = Math.exp(- ( binaryToNodeMap.get(idToBinaryMap.get(itemId)).energy - node.energy) / (R * T) );
				sum += energy2;
				energyList.add(energy2);
			}
			double randValue = rand.nextDouble();
			Iterator<Double> itr2 = energyList.iterator();
			int index = 0;
			double cummSum = 0.0;
			while (itr2.hasNext()) {
				double bowlt2 = itr2.next();
				cummSum += ( bowlt2 / sum );
				if(randValue < cummSum)
				{
					nextNode = binaryToNodeMap.get(idToBinaryMap.get(node.neighboursList.get(index)));
					break;
				}
				index++;
			}
			
			if(nextNode != null)
				node = nextNode;
			trace[j] = node.id;
			level[j] = node.level;
			if(max < node.level)
				max = node.level;
			if(max == 56)
			{ steps_taken[1] = j;
				break;
			}
			
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("monteCarlo.csv");
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write("Level,NodeId");
			for (int i = 0; i < level.length; i++) {
				bw.write(level[i]+","+trace[i]+"\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return max;
	}
	
	
	void print()
	{
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("Data.csv");
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write("NodeId,size");
			for (int i = 1; i < size; i++) {
				bw.write(binaryToNodeMap.get(idToBinaryMap.get(i)).id+","+binaryToNodeMap.get(idToBinaryMap.get(i)).neighboursList.size()+"\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
