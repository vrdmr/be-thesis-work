package projectBE;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class RoughKMeans2 {

	private static int numOfObjects;
	private static int numOfDimensions;
	private static int numOfClusters;
	private static double[][] objects; // complete data
	private static double[][] centroid; // complete data
	private static double w_l = 0.85;
	private static double w_u = 0.15;
	private static int maxIter;
	private static double threshold;

	static double distance(double[] x, double[] y) {
		double tempDistance = 0.0;
		for (int k = 0; k < numOfDimensions; k++) {
			tempDistance = tempDistance + (x[k] - y[k]) * (x[k] - y[k]);
		}
		return Math.sqrt(tempDistance);
	}

	public static void initialize(String clstrs, String iter, String thrshd) {
		maxIter = Integer.valueOf(iter);
		threshold = Double.valueOf(thrshd);
		numOfClusters = Integer.valueOf(clstrs);
		centroid = new double[numOfClusters][numOfDimensions];
		
		for (int i = 0; i < numOfClusters; i++) {
			int c = (int) (Math.random() * numOfObjects);
			for (int j = 0; j < numOfDimensions; j++) {
				centroid[i][j] = objects[c][j];
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void evolve() {
		for (int iter = 0; iter < maxIter; iter++) {
			Vector[] T = getMembership();
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
					if (T[i].contains(k)) {
						double factor;
						if (T[i].size() == 1) {
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

	@SuppressWarnings("unchecked")
	public static void printClusters() {
		Vector[] T = getMembership();
		for (int j = 0; j < numOfClusters; j++) {
			System.out.print("Centroid for cluster " + j + ": ");
			for (int i = 0; i < numOfDimensions; i++) {
				System.out.printf("%.2f ", centroid[j][i]);
			}
			System.out.println("\nLower bound for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() == 1)
					System.out.print(i + " ");
			}
			System.out.println("\nBoundary region for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() > 1)
					System.out.print(i + " ");
			}
			System.out.println();
		}
	}

	@SuppressWarnings("unchecked")
	public static Vector[] getMembership() {
		Vector[] T = new Vector[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			T[i] = new Vector();
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

	public static void main(String[] v) {
		if (v.length < 4) {
			System.err
					.println("Usage: java RKM dataFile clusters iter threshold");
			System.err.println("Example: java RKM synthetic2010.txt 3 50 0.7");
			System.exit(0);
		}
		readFileData(v[0]);
		initialize(v[1], v[2], v[3]);
		evolve();
		printClusters();
	}
}
