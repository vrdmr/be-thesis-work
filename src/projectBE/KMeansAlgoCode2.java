package projectBE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import java.text.DecimalFormat;
import java.util.Vector;

public class KMeansAlgoCode2 {

	/**
	 * @author Varad Meru
	 * @param args
	 *            where the args string input expected is to have :-- args[0] :
	 *            Input File Name args[1] : No. of Rows args[2] : No. of Columns
	 *            args[3] : No. of Clusters args[4] : No. of Iterations
	 */

	/*
	 * Global Declarations
	 */
	double[][] objects;
	double[][] centroids;
	@SuppressWarnings("unchecked")
	Vector[] memberships;

	@SuppressWarnings("unchecked")
	public KMeansAlgoCode2(int numOfRows, int numOfColumns, int numOfClusters) {
		// TODO Constructor of the Class
		objects = new double[numOfRows][numOfColumns];
		centroids = new double[numOfClusters][numOfColumns];
		memberships = new Vector[numOfClusters];
		for (int i = 0; i < memberships.length; i++)
			memberships[i] = new Vector();
		for (int i = 0; i < objects.length; i++)
			memberships[(int) Math.round(Math.random() * (numOfClusters - 1))]
					.add(new Integer(i));
	}

	public static void main(String[] args) {
		// TODO Main Method
		if (args.length != 5) {
			System.err
					.println("Usage: javaKmeans \ninputFileName\nnumRows\nnumColumns\nnumOfClusters\nnumOfIterations");
			return;
		}
		int numOfRows = new Integer(args[1]).intValue();
		int numOfColumns = new Integer(args[2]).intValue();
		int numOfClusters = new Integer(args[3]).intValue();
		int numOfIterations = new Integer(args[4]).intValue();
		KMeansAlgoCode2 m = new KMeansAlgoCode2(numOfRows, numOfColumns,
				numOfClusters);
		m.readObjects(args[0]);
		m.makeClusters(numOfIterations);
		//m.writeClusters();
		m.writeCentroid();
	}// end main

	private void writeCentroid() {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		df.setDecimalSeparatorAlwaysShown(true);
		for (int i = 0; i < memberships.length; i++) {
			System.out.println("Centroid for Cluster " + i + ":");
			for (int j = 0; j < centroids[0].length; j++)
				System.out.println(df.format(centroids[i][j]) + "\t");
			System.out.println();
		}

	}

	private void writeClusters() {
		// TODO Writes the generated cluster onto screen or file
		for (int i = 0; i < memberships.length; i++) {
			System.out.println("Cluster # " + i + "(number of objects ="
					+ memberships[i].size() + " )");
			for (int j = 0; j < memberships[i].size(); j++) {
				System.out.println(" "
						+ ((Integer) memberships[i].get(j)).intValue());
				if (j > 0 && j % 10 == 0)
					System.out.println();
			}
			System.out.println();
		}
	}

	@SuppressWarnings("unchecked")
	private void makeClusters(int numOfIterations) {
		// TODO Does the main CLustering part of the program
		for (int k = 0; k < numOfIterations; k++)
			CalclateCentroids();
		for (int i = 0; i < memberships.length; i++)
			memberships[i].clear();
		for (int i = 0; i < objects.length; i++)
			memberships[findWinner(i)].add(new Integer(i));
	}

	private void CalclateCentroids() {
		// TODO It Calculates the Centroid at the start of each iteration
		for (int i = 0; i < centroids.length; i++)
			for (int j = 0; j < centroids[0].length; j++)
				centroids[i][j] = 0;
		for (int k = 0; k < memberships.length; k++)
			for (int i = 0; i < memberships[k].size(); i++) {
				int m = ((Integer) memberships[k].get(i)).intValue();
				for (int j = 0; j < centroids[0].length; j++)
					centroids[k][j] += objects[m][j];
			}
		for (int i = 0; i < memberships.length; i++)
			for (int j = 0; j < centroids[0].length; j++)
				centroids[i][j] /= memberships[i].size();
	}

	private int findWinner(int objectID) {
		// TODO Finds the winning centroid selected by each point
		double min = distance(objectID, 0);
		int minAt = 0;
		for (int j = 1; j < centroids.length; j++) {
			double temp = distance(objectID, j);
			if (temp < min) {
				min = temp;
				minAt = j;
			}
		}
		return minAt;
	}

	private double distance(int objectID, int centroidID) {
		// TODO Calculates the distance of the point and the corresponding
		// centroid of the cluster it belongs to
		double d = 0;
		for (int j = 0; j < centroids[0].length; j++) {
			double ob = objects[objectID][j];
			double c = centroids[centroidID][j];
			d += (c - ob) * (c - ob);
		}
		return Math.sqrt(d) / centroids[0].length;
	}

	@SuppressWarnings("static-access")
	private void readObjects(String fileName) {
		// TODO Reads the input file to create the array of object
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
				}// inner while end
				i++;
				line = in.readLine();
			}// outer while end
		}// end of try block
		catch (IOException e) {
			System.out.println(e);
		}
	}
}