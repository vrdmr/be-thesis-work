package kohonenRKM;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 * An algorithm to cluster given set of objects
 * */

public class KohonenRKMeans {
	double[][] objects;
	double[][] weights;
	Vector[] memberships;
	double alpha;

	/**
	 * Reads the object vectors from a file
	 * 
	 * @param fileName
	 *            Name of the file containing the objects
	 * */
	public void readObjects(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			String line = in.readLine();
			int i = 0;
			while (i < objects.length && line != null) {
				StreamTokenizer tok = new StreamTokenizer(
						new StringReader(line));
				int j = 0;
				while (j < objects[0].length && tok.nextToken() != tok.TT_EOF) {
					objects[i][j] = tok.nval;
					j++;
				}
				i++;
				line = in.readLine();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void writeClusters() {
		for (int i = 0; i < memberships.length; i++) {
			System.out.println("Cluster #" + i + " (Number of objects = "
					+ memberships[i].size() + ")");
			for (int j = 0; j < memberships[i].size(); j++) {
				System.out.print(" "
						+ ((Integer) memberships[i].get(j)).intValue());
				if (j > 0 && j % 10 == 0)
					System.out.println();
			}
			System.out.println();
		}
	}

	public void writeCentroids() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		df.setDecimalSeparatorAlwaysShown(true);

		for (int i = 0; i < memberships.length; i++) {
			System.out.println("Weight vector for cluster " + i + ":");
			for (int j = 0; j < weights[0].length; j++) {
				System.out.print(df.format(weights[i][j]) + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * Finds the distance between an object and a weight
	 * 
	 * @param objectID
	 * @param weightID
	 * */

	double dist(int objectID, int weightID) {
		double d = 0;
		for (int j = 0; j < weights[0].length; j++) {
			double o = objects[objectID][j];
			double c = weights[weightID][j];
			d += (c - o) * (c - o);
		}
		if (weights[0].length == 0)
			return 0;
		return Math.sqrt(d) / weights[0].length;
	}

	/**
	 * @param rows
	 *            specifies the number of objects
	 * @param cols
	 *            specifies the number of columns/dimensions to represent eac
	 *            object
	 * @param clusters
	 *            specifies desired the number of clusters
	 * */

	public KohonenRKMeans(int rows, int cols, int clusters, double initAlpha) {
		// Initialize all the arrays and vectors
		objects = new double[rows][cols];
		weights = new double[clusters][cols];
		memberships = new Vector[clusters];
		alpha = initAlpha;
		for (int i = 0; i < memberships.length; i++)
			memberships[i] = new Vector();
	}

	void initializeWeights() {
		// Initialize membership lists for each cluster
		for (int i = 0; i < objects.length; i++)
			memberships[(int) Math.round(Math.random()
					* (memberships.length - 1))].add(new Integer(i));

		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[0].length; j++)
				weights[i][j] = 0;

		for (int k = 0; k < memberships.length; k++)
			for (int i = 0; i < memberships[k].size(); i++) {
				int m = ((Integer) memberships[k].get(i)).intValue();
				for (int j = 0; j < weights[0].length; j++)
					weights[k][j] += objects[m][j];
			}

		for (int i = 0; i < memberships.length; i++)
			for (int j = 0; j < weights[0].length; j++)
				weights[i][j] /= memberships[i].size();
		// Don't need these random memberships anymore
		for (int i = 0; i < memberships.length; i++)
			memberships[i].clear();
	}

	int findWinner(int objectID) {
		double min = dist(objectID, 0);
		int minAT = 0;
		for (int j = 1; j < weights.length; j++) {
			double temp = dist(objectID, j);
			if (temp < min) {
				min = temp;
				minAT = j;
			}
		}
		return minAT;
	}

	void update(int winner, int objectID) {
		for (int j = 0; j < weights[winner].length; j++)
			weights[winner][j] = (1 - alpha) * weights[winner][j] + alpha
					* objects[objectID][j];
	}

	/**
	 * @param iter
	 *            specifies the number of iterations
	 * */

	public void makeClusters(int iter) {
		initializeWeights();
		for (int k = 0; k < iter; k++)
			for (int i = 0; i < objects.length; i++)
				update(findWinner(i), i);

		for (int i = 0; i < objects.length; i++)
			memberships[findWinner(i)].add(new Integer(i));
	}

	/**
	 * @param args
	 *            An array of Strings, e.g. test.txt 7 7 2 10
	 * */

	public static void main(String[] args) {

		if (args.length < 5) {
			System.err
					.println("Usage: java Kohonen inputFileName numRows numColumns numOfClusters numOfIterations");
			return;
		}
		int numOfRows = new Integer(args[1]).intValue();
		int numOfCols = new Integer(args[2]).intValue();
		int numOfClusters = new Integer(args[3]).intValue();
		int numOfIterations = new Integer(args[4]).intValue();
		Kohonen m = new Kohonen(numOfRows, numOfCols, numOfClusters, 0.01);
		m.readObjects(args[0]);
		m.makeClusters(numOfIterations);
		m.writeClusters();
		m.writeCentroids();
	}
}
