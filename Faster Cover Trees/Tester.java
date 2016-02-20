

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Nearest Cover Tree tester
 * @author Jiyuan Li and Liye Guo
 *
 */
public class Tester {
	
	private static final String EUCLIDEAN = "Euclidean";
	private static final String HAMMING = "Hamming";
	private static final int minSize = 100;
	private static final int maxSize = 100000;
	private static final int timesToLoop = 1;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("Test case#1: Random 2-D points");
		Object[][] table = new String[5][];
		table[0] = new String[] {"Number of Points", "Linear Search", "Cover Tree", "Nearest Cover Tree"};
		int row = 1;
		//Scan test file
		for(int NumberOfPoints = minSize; NumberOfPoints <= maxSize; NumberOfPoints *= 10)
		{
			System.out.println("Number of 2D Points: " + NumberOfPoints);
			List<double[]> traindata = generate2DPoints(NumberOfPoints, NumberOfPoints);
			System.out.println("Constructing Nearest Cover Tree.....");
			NearestCoverTree<Double> nct = new NearestCoverTree<Double>(EUCLIDEAN, 100);
			for(int i = 0; i < traindata.size(); i++)
			{
				double[] point = traindata.get(i);
				if(nct.getRoot() == null)
					nct.insertAtRoot(0.0, point);
				else
					nct.insert(0.0, nct.getRoot(), point);
			}
			//Update max distance for every node in the tree
			nct.updateMaxDist();
			System.out.println("Done.");			
			
			//nct.tostring();
			
			//Regular linear searching
			System.out.println("Constructing Linear structure.....");
			LinearSearching<Double> list = new LinearSearching<Double>(EUCLIDEAN);
			for(int i = 0; i < traindata.size(); i++)
			{
				double[] point = traindata.get(i);
				list.insert(0.0, point);
			}
			System.out.println("Done.");
			
			//Original Cover Tree
			System.out.println("Constructing Cover Tree.....");
			CoverTree<Double> ctree = new CoverTree<Double>(EUCLIDEAN);
			for(int i = 0; i < traindata.size(); i++)
			{
				double[] point = traindata.get(i);
				ctree.insert(0.0, point);
			}
			System.out.println("Done.");
			
			List<double[]> testdata = generate2DPoints(NumberOfPoints, NumberOfPoints);
			long CT_cost = 0;
			long Linear_cost = 0;
			long NCT_cost = 0;
			for(int loop = 0; loop < timesToLoop; loop++)
			{
				//System.out.println("Testing for Cover Tree....");
				//FileWriter file = new FileWriter("CT_result.txt");
				long CTreeStart = System.currentTimeMillis();
				for(int i = 0; i < testdata.size(); i++)
				{
					ctree.getNearest(testdata.get(i));
					//file.write(Arrays.toString(ctree.getNearest(testdata.get(i)).point)+"\n");
				}
				long CTreeTotal = System.currentTimeMillis() - CTreeStart;
				CT_cost = CT_cost + CTreeTotal;
				//System.out.println("Runtime: " + CTreeTotel +" millisecond.");
				//file.close();
				
				//System.out.println("Testing for Linear Structure....");
				//file = new FileWriter("Linear_result.txt");
				long LinearStart = System.currentTimeMillis();
				for(int i = 0; i < testdata.size(); i++)
				{
					list.findNearestNeighbor(testdata.get(i));
					//file.write(Arrays.toString(list.findNearestNeighbor(testdata.get(i))) + "\n");
				}
				long LinearTotal = System.currentTimeMillis() - LinearStart;
				Linear_cost = Linear_cost + LinearTotal;
				//System.out.println("Runtime: " + LinearTotal + " millisecond.");
				//file.close();
				
				//System.out.println("Testing for Nearest Cover Tree....");
				//file = new FileWriter("NCT_result.txt");
				
				long NCTStart = System.currentTimeMillis();
				for(int i = 0; i < testdata.size(); i++)
				{
					nct.findNearestNeighbor(nct.getRoot(), testdata.get(i), nct.getRoot());
					//file.write(Arrays.toString(node.getPoints()) + "\n");
				}
				long NCTtotal = System.currentTimeMillis() - NCTStart;
				NCT_cost = NCT_cost + NCTtotal;
				//System.out.println("Runtime: " + NCTtotal+ " millisecond.");
				//file.close();
			}
			table[row] = new String[4];
			table[row][0] = NumberOfPoints+"";
			table[row][1] = Linear_cost/timesToLoop + " millsec";
			table[row][2] = CT_cost/timesToLoop + " millsec";
			table[row][3] = NCT_cost/timesToLoop + " millsec";
			row++;
			System.out.println("Testing done....");
			System.out.println();
		}
		
		System.out.println("Testing result: ");
		for(Object[] r: table)
		{
			System.out.format("%-15s%-15s%-15s%-15s\n", r);
		}
		
		//System.out.println();
		
		System.out.println("Test case#2: Tic-Tac-Toe");
		table = new String[2][];
		table[0] = new String[] {"Linear Search", "Cover Tree", "Nearest Cover Tree"};
		List<double[]> traindata = new ArrayList<double[]>();
		for(int i = 1;  i <= 6; i++)
			scanTicTacToe(traindata, "tic-tac-toe-train-"+i+".txt");

		//Nearest Cover Tree
		System.out.println("Constructing Nearest Cover tree....");
		NearestCoverTree<String> nct = new NearestCoverTree<String>(HAMMING, 100);
		for(int i = 0; i < traindata.size(); i++)
		{
			double[] point = traindata.get(i);
			if(nct.getRoot() == null)
				nct.insertAtRoot("",point);
			else
				nct.insert("",nct.getRoot(), point);
		}
		//Update max distance for every node in the tree
		nct.updateMaxDist();
		System.out.println("Done.");
		
		//nct.tostring();
		
		//Regular linear searching
		System.out.println("Constructing Linear structure.....");
		LinearSearching<String> list = new LinearSearching<String>(HAMMING);
		for(int i = 0; i < traindata.size(); i++)
		{
			double[] point = traindata.get(i);
			list.insert("", point);
		}
		System.out.println("Done.");
		
		//Original Cover Tree
		System.out.println("Constructing Cover Tree.....");
		CoverTree<String> ctree = new CoverTree<String>(HAMMING);
		for(int i = 0; i < traindata.size(); i++)
		{
			double[] point = traindata.get(i);
			ctree.insert("", point);
		}
		System.out.println("Done.");
		
		long CT_cost = 0;
		long Linear_cost = 0;
		long NCT_cost = 0;
		List<double[]> testdata = new ArrayList<double[]>();
		scanTicTacToe(testdata, "tic-tac-toe-test.txt");
		System.out.println("Testing.....");
		for(int loop = 0; loop < timesToLoop; loop++)
		{
			//System.out.println("Testing for Cover Tree....");
			//FileWriter file = new FileWriter("CT_result_ttt.txt");
			long CTStart = System.currentTimeMillis();
			for(int i = 0; i < testdata.size(); i++)
			{
				ctree.getNearest(testdata.get(i));
				//file.write(Arrays.toString(ctree.getNearest(testdata.get(i)).point)+"\n");
			}
			long CTtotal = System.currentTimeMillis() - CTStart;
			CT_cost = CT_cost + CTtotal;
			//file.close();
			
			//System.out.println("Testing for Linear Structure....");
			//file = new FileWriter("Linear_result_ttt.txt");
			long LinearStart = System.currentTimeMillis();
			for(int i = 0; i < testdata.size(); i++)
			{
				list.findNearestNeighbor(testdata.get(i));
				//file.write(Arrays.toString(list.findNearestNeighbor(testdata.get(i))) + "\n");
			}
			long LinearTotal = System.currentTimeMillis() - LinearStart;
			Linear_cost = Linear_cost + LinearTotal;
			//file.close();
			
			//System.out.println("Testing for Nearest Cover Tree....");
			//file = new FileWriter("NCT_result_ttt.txt");
			
			long NCTstart = System.currentTimeMillis();
			for(int i = 0; i < testdata.size(); i++)
			{
				nct.findNearestNeighbor(nct.getRoot(), testdata.get(i), nct.getRoot());
				//file.write(Arrays.toString(nct.findNearestNeighbor(nct.getRoot(), testdata.get(i), nct.getRoot()).getPoints()) + "\n");
			}
			long NCTtotal = System.currentTimeMillis()- NCTstart;
			NCT_cost = NCT_cost + NCTtotal;
			//file.close();
		}
		
		//System.out.println("Cover Tree Running time: " + CT_cost/10+ " millisecond.");
		//System.out.println("Linear Search Running time: " + Linear_cost/10 + " millisecond.");
		//System.out.println("Nearest Cover Tree Running time: " + NCT_cost/10 + " millisecond.");
		table[1] = new String[3];
		table[1][0] = Linear_cost/timesToLoop+" millsec";
		table[1][1] = CT_cost/timesToLoop+" millsec";
		table[1][2] = NCT_cost/timesToLoop +" millsec";
		
		for(Object[] r: table)
			System.out.format("%-15s%-15s%-15s\n", r);
	}
	
	/**
	 * Generate 2D points
	 * @param NumberOfPoints
	 * @return
	 */
	public static List<double[]> generate2DPoints(int NumberOfPoints, int max)
	{
		List<double[]> points = new ArrayList<double[]>();
		for(int i = 0; i < NumberOfPoints; i++)
		{
			double X = getRandomPoint(0, max);
			double Y = getRandomPoint(0, max);
			double[] point = new double[2];
			point[0] = X;
			point[1] = Y;
			points.add(point);
		}
		return points;
	}
	
	/**
	 * Get a random number between min and max
	 * @param max
	 * @param min
	 * @return
	 */
	public static double getRandomPoint(int max, int min)
	{
		int range = max - min + 1;
		return (Math.random() * range) + min;
	}
	
	public static void scanTicTacToe(List<double[]> data, String name) throws FileNotFoundException
	{
		File train = new File(name);
		Scanner s = new Scanner(train);
		while(s.hasNextLine())
		{
			String line = s.nextLine();
			String[] splitline = line.split(",");
			double[] points = new double[9];
			for(int i = 0; i < splitline.length-1; i++)
			{
				//o to 1, x to 0
				if(splitline[i].equals("o"))
					points[i] = 1;
				else
					points[i] = 0;
			}
			data.add(points);
		}
		s.close();
	}
	

}
