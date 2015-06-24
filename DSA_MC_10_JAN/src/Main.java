
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		long startTime = System.currentTimeMillis();
		Graph graph = new Graph();
		graph.initialize();
		System.out.println(graph.size);
		graph.print();
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) + " milliseconds");
		
		

		startTime = System.currentTimeMillis();
		int counter = 0 ;
		int counter1 = 0;
		int[] steps = new int[2];
		System.out.println("NodeID, Steps taken to reach 56 folded Ones in Metropolis walk, Random Walk based on energy");
//		for (int i = 1; i < graph.size; i++) {
//			steps[0] = 0;
//			steps[1] = 0;
//			if(graph.montecarloWalk(i, steps) < 56)
//			{
//				counter++;
//				
//			}
//			if(graph.randomWalkEnergyDiff_probability(i, steps) < 56)
//			{
//				counter1++;
//				
//			}
//			System.out.println(+steps[0]); //+" "+steps[1]);
//		}
//		int[][] stepsMCMC100 = new int[57][100];
//		for (int j = 0; j < 100; j++) {
//
//
			for (int i = 1; i <= 56; i++) {
				steps[0] = 0;
				int count = graph.montecarloWalkTime(i, steps);
//				stepsMCMC100[i][j] = steps[0]; 
//				System.out.println(steps[0]);  //+", "+graph.idToBinaryMap.get(i));

				System.out.println("Passed the test for start node : "+i);

			}
//
//		}
//		
		
//		for (int i = 1; i <= 56; i++) {
//			for (int j = 0; j < 100; j++) {
//				System.out.print(stepsMCMC100[i][j]+",");
//			}
//			System.out.println();
//		}
//		counter = graph.randomWalkEnergyDiff(1, steps);
//		graph.randomWalkEnergyDiff_probability(1, steps);
		
//		for (int i = 1; i <= 56; i++) {
//			System.out.println("'"+graph.idToBinaryMap.get(i)+"'");
//		}
		System.out.println(counter+"----"+counter1+"----------");
		
		endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) + " milliseconds");
//		graph.montecarloWalk(1);
	}

}
