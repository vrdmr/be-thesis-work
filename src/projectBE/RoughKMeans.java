package projectBE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Varad Meru 
 * @Date : 30/9/10 
 * @Time : 2200 Hrs
 * @Details
 *         Contains the Following Methods main -- Main Method of our program
 *         readFileData -- read the input file to get the objects into memory
 *         initialize -- intialize the parameters and other objects evolve --
 *         assignment of values getMembership -- find out the centroid suited
 *         for you distance -- distance calculator printClusters -- print out
 *         the clusters
 */

public class RoughKMeans {

	private static int numOfObjects; // no. of objects to be grouped into
										// clusters
	private static int numOfDimensions; // no of dimensions of the objects
	private static int numOfClusters; // no. of clusters
	private static int maxIter; // maximum number of Iterations

	private static double[][] objects; // 2D array of objects
	private static double[][] centroid; // 2D array of centroids

	private static double w_l, w_u, threshold; // Rough Set Clustering
	
	@SuppressWarnings("unchecked")
	static
	Vector[] T;

	public static void main(String[] args) {
		if (args.length < 6) {
			System.err
					.println("Usage : java \n RoughKMeans Data File \n Number of Clusters \n Number of Iterations \n Threshold \n Weight_Lower \n Weight_Upper");
			System.err
					.println("Example : java KMeansData.txt 3 40 0.7 0.85 0.15");
		}

		readFileData(args[0]);
		initialize(args[1], args[2], args[3], args[4], args[5]);
		evolve();
		printClusters();
	}

	public static void readFileData(String Filename) {
		Scanner filescan;
		try {
			filescan = new Scanner(new File(Filename));
			numOfObjects = filescan.nextInt();
			numOfDimensions = filescan.nextInt();
			objects = new double[numOfObjects][numOfDimensions];
			
			while (filescan.hasNext()) {
				for (int i = 0, j = 0; j < numOfDimensions; i++, j++)
					objects[i][j] = filescan.nextDouble();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("The error :" + e);
			e.printStackTrace();
		}
	}

	public static void initialize(String clusters, String iterations,
			String threshld, String wl, String wu) {
		maxIter = Integer.valueOf(iterations);
		threshold = Double.valueOf(threshld);
		numOfClusters = Integer.valueOf(clusters);
		w_l = Double.valueOf(wl);
		w_u = Double.valueOf(wu);
		centroid = new double[numOfClusters][numOfDimensions];
		
		for (int i = 0; i < numOfClusters; i++) {
			int c = (int) (Math.random() * numOfObjects);
			for (int j = 0; j < numOfDimensions; j++)
				centroid[i][j] = objects[c][j];
		}
	}

	public static void evolve() {
		for (int iter = 0; iter < maxIter; iter++) {
			T = getMemberships();
			for (int k = 0; k < numOfClusters; k++) {
				int sizeL = 0, sizeU = 0;
				double[] CentroidL = new double[numOfDimensions];
				double[] CentroidU = new double[numOfDimensions];
				for (int j = 0; j < numOfDimensions; j++)
					CentroidL[j] = CentroidU[j] = 0;
				for (int i = 0; i < numOfObjects; i++) {
					if (T[i].contains(k)) {
						if (T[i].size() == 1) {
							for (int j = 0; j < numOfDimensions; j++)
								CentroidL[j] += objects[i][j];
							sizeL++;
						} else {
							for (int j = 0; j < numOfDimensions; j++)
								CentroidU[j] += objects[i][j];
							sizeU++;
						}
					}
				}
				for (int j = 0; j < numOfDimensions; j++) {
					if (sizeL != 0 && sizeU != 0)
						centroid[k][j] = (CentroidL[j] / sizeL) * w_l
								+ (CentroidU[j] / sizeU) * w_u;
					else if (sizeL == 0 && sizeU != 0)
						centroid[k][j] = CentroidU[j] / sizeU;
					else if (sizeL != 0 && sizeU == 0)
						centroid[k][j] = CentroidL[j] / sizeL;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static Vector[] getMemberships() {
		Vector[] T = new Vector[numOfObjects];
		double min;
		double[] objectError = new double[numOfObjects];

		for (int i = 0; i < numOfObjects; i++)
			T[i] = new Vector();
		for (int i = 0; i < numOfObjects; i++) {
			int closest = 0;
			double tempDistance[] = new double[numOfClusters];
			tempDistance[0] = distance(objects[i], centroid[0]);
			min = tempDistance[0];
			for (int j = 1; j < numOfClusters; j++) {
				tempDistance[j] = distance(objects[i], centroid[j]);
				if (tempDistance[j] < min) {
					min = tempDistance[j];
					closest = j;
				}
			}
			T[i].add(closest);
			objectError[i] = tempDistance[closest];
			for (int j = 0; j < numOfClusters; j++) {
				if (j != closest
						&& tempDistance[closest] / tempDistance[j] >= threshold) {
					T[i].add(j);
					objectError[i] += tempDistance[j];
				}
			}
		}
		return T;
	}

	public static double distance(double[] x, double[] y) {
		double TempD = 0.0;
		for (int k = 0; k < numOfDimensions; k++) {
			TempD += (x[k] - y[k]) * (x[k] - y[k]);
		}
		return Math.sqrt(TempD);
	}

	public static void printClusters() {
		for (int j = 0; j < numOfClusters; j++) {
			System.out.println("Centroid for Cluster :" + j + ":");
			for (int i = 0; i < numOfDimensions; i++)
				System.out.printf("%1f  ", centroid[j][i]);
			System.out.println("\nLower Bound for Cluster" + j + ";");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() == 1)
					System.out.print(i + " ");
			}
			System.out.println("\n Boundry Region for CLusters " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() > 1)
					System.out.print(i + " ");
			}
			System.out.println();
		}
	}
}