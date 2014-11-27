package kohonenRKM;

import java.io.*;
import java.util.*;
import java.text.*;

@SuppressWarnings("unused")
public class Kohonen {

	double[][] objects;
	double[][] weights;
	@SuppressWarnings("unchecked")
	Vector[] memberships;
	double alpha;
	static int numOfRows, numOfColumns, numOfClusters, numOfIterations;

	public static void main(String[] args) {
		// args[0] is the filename to be passed as an argument
		numOfRows = new Integer(args[1]).intValue();
		numOfColumns = new Integer(args[2]).intValue();
		numOfClusters = new Integer(args[3]).intValue();
		numOfIterations = new Integer(args[4]).intValue();

		Kohonen m = new Kohonen(numOfRows, numOfColumns, numOfClusters, 0.01);
		m.readObjects(args[0]);
		m.makeClusters(numOfIterations);
		m.writeClusters();
		m.writeCentroids();
	}// end main()

	@SuppressWarnings("unchecked")
	public Kohonen(int rows, int cols, int clusters, double initAlpha) {
		objects = new double[rows][cols];
		weights = new double[clusters][cols];
		memberships = new Vector[clusters];
		alpha = initAlpha;
		for (int i = 0; i < memberships.length; i++)
			memberships[i] = new Vector();
	}// end constructor

	public void readObjects(String fileName) {
		try {
			Scanner fileScan = new Scanner(new File(fileName));
			int i = 0;
			while (fileScan.hasNext()) {
				for (int j = 0; j < numOfColumns; j++)
					objects[i][j] = fileScan.nextDouble();
				i++;
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}// end readObjects

	@SuppressWarnings("unchecked")
	public void makeClusters(int iter) {
		initializeWeights();
		for (int i = 0; i < objects.length; i++)
			update(findWinner(i), i);

		for (int i = 0; i < objects.length; i++)
			memberships[findWinner(i)].add(new Integer(i));

	}// end makeClusters
	
	@SuppressWarnings("unchecked")
	public void initializeWeights() {
		for (int i = 0; i < objects.length; i++)
			memberships[(int) Math.round(Math.random()
					* (memberships.length - 1))].add(new Integer(i));

		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[0].length; j++)
				weights[i][j] = 0.0;

		for (int k = 0; k < memberships.length; k++) {
			for (int i = 0; i < memberships[k].size(); i++) {
				int m = ((Integer) memberships[k].get(i)).intValue();
				for (int j = 0; j < weights[0].length; j++)
					weights[k][j] += objects[m][j];
			}
		}

		for (int i = 0; i < memberships.length; i++)
			for (int j = 0; j < weights[0].length; j++)
				weights[i][j] /= memberships[i].size();

		for (int i = 0; i < memberships.length; i++)
			memberships[i].clear();
	}// end initializeWeights

	public int findWinner(int objectID) {
		double min = dist(objectID, 0);
		int minAt = 0;
		for (int j = 0; j < weights.length; j++) {
			double temp = dist(objectID, j);
			if (temp < min) {
				min = temp;
				minAt = j;
			}
		}
		return minAt;
	}// end findWinner

	public double dist(int objectID, int weightID) {
		double d = 0;
		for (int j = 0; j < weights[0].length; j++) {
			double d1 = objects[objectID][j];
			double d2 = weights[weightID][j];
			d += (d2 - d1) * (d2 - d1);
		}
		if (weights[0].length == 0)
			return 0;
		return Math.sqrt(d) / weights[0].length;
	}// end dist
	public void update(int winner, int objectID) {
		for (int j = 0; j < weights[winner].length; j++)
			weights[winner][j] = (1 - alpha) * weights[winner][j] + alpha
					* objects[objectID][j];
	}// end update

	public void writeClusters() {
		for (int i = 0; i < memberships.length; i++) {
			System.out.println("@@@");
			System.out.println("Cluster : " + i + " (Number of objects : "
					+ memberships[i].size() + ")");

			for (int j = 0; j < memberships[i].size(); j++) {
				System.out.print(" "
						+ ((Integer) memberships[i].get(j)).intValue());
				if (j > 0 && j % 10 == 0)
					System.out.println();
			}
			System.out.println();
		}
	}// end writeClusters

	public void writeCentroids() {
		for (int i = 0; i < weights.length; i++) {
			System.out.print("Centroids for cluster " + i + ": ");
			for (int j = 0; j < weights[0].length; j++)
				System.out.print(weights[i][j] + " ");
			System.out.println();
		}
	}
}