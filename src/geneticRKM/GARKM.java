package geneticRKM;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javaGAlib.*;

public class GARKM extends GAFloat {
	public static int numOfObjects;
	private static int numOfDimensions;
	private static int numOfClusters;
	static double[][] objects;
	private static double threshold = 1.4;
	private static double w_l = 0.75;
	private static double w_u = 0.25;

	public static void main(String[] args) {
		readFileData();
		System.out.println("Finding centroids with GA");
		try {
			GARKM centroidFit = new GARKM();
			Thread threadCentroidFit = new Thread(centroidFit);
			threadCentroidFit.run();
		} catch (GAException gae) {
			System.out.print(gae.getMessage());
		}
	}// end main

	private static void readFileData() {
		int i = 0;
		try {
			Scanner fileScan = new Scanner(new File("/Users/varadmeru/Documents/JavaWorkspace/CollegeProject/src/geneticRKM/DataFile2.txt"));
			numOfObjects = fileScan.nextInt();
			numOfDimensions = fileScan.nextInt();
			numOfClusters = fileScan.nextInt();
			objects = new double[numOfObjects][numOfDimensions];
			while (fileScan.hasNext()) {
				for (int j = 0; j < numOfDimensions; j++) {
					objects[i][j] = fileScan.nextDouble();
				}
				i++;
			}
		} catch (Exception e) {
			System.out.print("Error:" + e);
			System.exit(0);
		}
	}

	public GARKM() throws GAException {
		super(numOfClusters * numOfDimensions,// no.of genes in a chromosome
				100,// population of chromosome
				0.7,// crossover probability
				6,// random selection chance
				50,// stop after these many generations
				10,// no. of preliminary runs to build good breeding stack for
					// finding fall run
				20,// max preliminary runs
				0.1,// chromosome mutation probability
				Crossover.ctTwoPoint,// crossover type
				2,// number of decimal pts of precision
				false,// considers only float numbers
				true);// compute statistics
	}// end constructor

	public int evolve() {
		int temp = super.evolve();
		Vector[] T = new Vector[numOfObjects];
		for (int i = 0; i < numOfObjects; i++)
			T[i] = new Vector();
		double error = getFitnessMembership((ChromFloat) this
				.getFittestChromosome(), T);
		for (int j = 0; j < numOfClusters; j++) {
			System.out.println("Lower bound for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() == 1)
					System.out.println(i + " ");
			}
			System.out.println("\nBoundary region for cluster " + j + ":");
			for (int i = 0; i < numOfObjects; i++) {
				if (T[i].contains(j) && T[i].size() > 1)
					System.out.println(i + " ");
			}
			System.out.println();
		}
		return temp;
	}// end evolve

	double getFitnessMembership(ChromFloat ob, Vector[] T) {
		double error = 0.0, distance = 0.0, min;
		int k = 0;
		double[][] centroid = new double[numOfClusters][numOfDimensions];
		for (int i = 0; i < numOfClusters; i++) {
			for (int j = 0; j < numOfDimensions; j++) {
				centroid[i][j] = ob.getGene(k);
				k++;
			}
		}
		double[] objectError = new double[numOfObjects];
		for (int i = 0; i < numOfObjects; i++) {
			double[] tempDistance = new double[numOfClusters];
			min = tempDistance[0] = distance(objects[i], centroid[0]);
			int closest = 0;
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
						&& tempDistance[j] / tempDistance[closest] <= threshold) {
					T[i].add(j);
					objectError[i] += tempDistance[j];
				}
			}
			if (T[i].size() == 1)
				objectError[i] *= w_l;
			else
				objectError[i] *= w_u;
			error += objectError[i];
		}
		return error;
	}// end getFitnessMembership

	protected double getFitness(int iChromIndex) {
		Vector[] T = new Vector[numOfObjects];
		for (int i = 0; i < numOfObjects; i++)
			T[i] = new Vector();
		double error = getFitnessMembership(this.getChromosome(iChromIndex), T);
		if (Math.abs(error) > 0)
			return 1 / error;
		else
			return (1e-12);
	}

	double distance(double[] x, double[] y) {
		double tempD = 0.0;
		for (int k = 0; k < numOfDimensions; k++)
			tempD += (x[k] - y[k]) * (x[k] - y[k]);
		return Math.sqrt(tempD);
	}
}