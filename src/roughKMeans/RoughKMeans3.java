package roughKMeans;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Varad Meru
 * @Date 11/10/10
 * @Performs This programs has been implemented to find out the differences
 *           between the two approaches of finding clusters using rough K-means
 * 
 *           1. The first approach is a porposed refinement of using ratios which 
 *           was introduced by George Peters which uses the following criterion to
 *           assign membership to an upper approximation
 * 
 *           c/b <= threshold , such that b= distance between point and the
 *           closest centroid and c= distance between the point and the centroid
 *           which is not the closest to that corresponding point.
 * 
 *           2. The second approach was written in the paper by Lingras et. al.
 *           named "Precision of Rough Set Clustering" were the ratios are taken
 *           of b/c <= threshold, such that b= distance between point and the
 *           closest centroid and c= distance between the point and the centroid
 *           which is not the closest to that corresponding point.
 * 
 * @methods getMembershipPeters() - 1st Approach getMembershipLingras() - 2nd
 *          Approach
 * 
 */

public class RoughKMeans3 {

	private static int numOfObjects; // no. of objects to be grouped into
	// clusters
	private static int numOfDimensions; // no of dimensions of the objects
	private static int numOfClusters; // no. of clusters
	private static int maxIter; // maximum number of Iterations

	private static double[][] objects; // 2D array of objects
	private static double[][] centroid; // 2D array of centroids

	private static double w_l, w_u, threshold; // Rough Set Clustering

	@SuppressWarnings("unchecked")
	static Vector[] PetersT;
	@SuppressWarnings("unchecked")
	static Vector[] LingrasT;

	static double distance(double[] x, double[] y) {
		double tempDistance = 0.0;
		for (int k = 0; k < numOfDimensions; k++) {
			tempDistance = tempDistance + (x[k] - y[k]) * (x[k] - y[k]);
		}
		return Math.sqrt(tempDistance);
	}

	/**s
	 * 
	 * @param clusters
	 * @param iterations
	 * @param threshld
	 * @param wl
	 */

	public static void initialize(String clusters, String iterations,
			String threshld, String wl) {
		maxIter = Integer.valueOf(iterations);
		threshold = Double.valueOf(threshld);
		numOfClusters = Integer.valueOf(clusters);
		w_l = Double.valueOf(wl);
		w_u = 1 - w_l;
		centroid = new double[numOfClusters][numOfDimensions];

		for (int i = 0; i < numOfClusters; i++) {
			int c = (int) (Math.random() * numOfObjects);
			for (int j = 0; j < numOfDimensions; j++)
				centroid[i][j] = objects[c][j];
		}
	}

	/**
	 * evolve() :- It calculates the centroids for the next iterations so as to
	 * get the most stable centroids after sufficient number of iterations
	 */
	public static void evolve() {
		for (int iter = 0; iter < maxIter; iter++) {

			PetersT = getMembershipPeters();

			for (int k = 0; k < numOfClusters; k++) {
				int sizeL = 0;
				int sizeU = 0;
				double[] centroidL = new double[numOfDimensions];
				double[] centroidU = new double[numOfDimensions];
				for (int j = 0; j < numOfDimensions; j++) {
					centroidL[j] = 0;
					centroidU[j] = 0;
				}
				for (int i = 0; i < numOfObjects; i++) {
					if (PetersT[i].contains(k)) {
						if (PetersT[i].size() == 1) {
							for (int j = 0; j < numOfDimensions; j++) {
								centroidL[j] += objects[i][j];
							}
							sizeL++;
						} else {
							for (int j = 0; j < numOfDimensions; j++) {
								centroidU[j] += objects[i][j];
							}
							sizeU++;
						}
					}
				}
				for (int j = 0; j < numOfDimensions; j++) {
					if (sizeL != 0 && sizeU != 0)
						centroid[k][j] = centroidL[j] * w_l / sizeL
						+ centroidU[j] * w_u / sizeU;
					else if (sizeL == 0 && sizeU != 0)
						centroid[k][j] = centroidU[j] / sizeU;
					if (sizeL != 0 && sizeU == 0)
						centroid[k][j] = centroidL[j] / sizeL;
				}
			}
		}
	}

	/**
	 * 
	 */
	public static void printClusters() {
		PetersT = getMembershipPeters();
		for (int j = 0; j < numOfClusters; j++) {
			System.out.print("Centroid for cluster " + j + ": ");
			for (int i = 0; i < numOfDimensions; i++) {
				System.out.printf("%.2f ", centroid[j][i]);
			}
			System.out.println("\nLower bound for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (PetersT[i].contains(j) && PetersT[i].size() == 1)
					System.out.print(i + " ");
			}
			System.out.println("\nBoundary region for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (PetersT[i].contains(j) && PetersT[i].size() > 1)
					System.out.print(i + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 
	 * @param fileName
	 */
	public static void readFileData(String fileName) {
		int i = 0;
		try {
			Scanner fileScan = new Scanner(new File(fileName));
			numOfObjects = fileScan.nextInt();
			numOfDimensions = fileScan.nextInt();
			objects = new double[numOfObjects][numOfDimensions];
			while (fileScan.hasNext()) {
				for (int j = 0; j < numOfDimensions; j++)
					objects[i][j] = fileScan.nextDouble();
				i++;
			}
		} catch (Exception e) {
			System.out.println(" the error" + e);
		}
	}

	/**
	 * 
	 * @return PetersT
	 */
	@SuppressWarnings("unchecked")
	public static Vector[] getMembershipPeters() {
		PetersT = new Vector[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			PetersT[i] = new Vector();
		}
		double min;
		double[] objectError = new double[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			double tempDistance[] = new double[numOfClusters];
			tempDistance[0] = distance(objects[i], centroid[0]);
			min = tempDistance[0];
			int closest = 0;
			// find the closest centroid to ith object
			for (int j = 1; j < numOfClusters; j++) {
				tempDistance[j] = distance(objects[i], centroid[j]);
				if (tempDistance[j] < min) {
					min = tempDistance[j];
					closest = j;
				}
			}
			PetersT[i].add(closest);
			objectError[i] = tempDistance[closest];
			for (int j = 0; j < numOfClusters; j++) {
				if (j != closest
						&& tempDistance[closest] / tempDistance[j] >= threshold) {
					PetersT[i].add(j);
					objectError[i] += tempDistance[j];
				}
			}
		}
		return PetersT;
	}

	/**
	 * 
	 * @return LingrasT
	 */
	@SuppressWarnings("unchecked")
	public static Vector[] getMembershipLingras() {
		LingrasT = new Vector[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			LingrasT[i] = new Vector();
		}
		double min;
		double[] objectError = new double[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			double tempDistance[] = new double[numOfClusters];
			tempDistance[0] = distance(objects[i], centroid[0]);
			min = tempDistance[0];
			int closest = 0;
			// find the closest centroid to ith object
			for (int j = 1; j < numOfClusters; j++) {
				tempDistance[j] = distance(objects[i], centroid[j]);
				if (tempDistance[j] < min) {
					min = tempDistance[j];
					closest = j;
				}
			}
			LingrasT[i].add(closest);
			objectError[i] = tempDistance[closest];
			for (int j = 0; j < numOfClusters; j++) {
				if (j != closest
						&& tempDistance[closest] / tempDistance[j] <= threshold) {
					LingrasT[i].add(j);
					objectError[i] += tempDistance[j];
				}
			}
		}
		return LingrasT;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 5) {
			System.err
			.println("Usage : java \n RoughKMeans Data File \n Number of Clusters \n Number of Iterations \n Threshold \n Weight_Lower");
			System.err.println("Example : java KMeansDataFile.txt 3 40 0.7 0.85");
		}
		readFileData(args[0]);
		initialize(args[1], args[2], args[3], args[4]);
		evolve();
		printClusters();
	}
}
