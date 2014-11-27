package projectBE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import projectBE.AlgoData;
import projectBE.Cluster;

/**
 * @author Mansi
 * 
 */

public class KMeansAlgorithm1 {
	private static AlgoData[] myData = new AlgoData[65];
	private static int dataSize = 0;
	private static int c1, c2, c3;
	private static Cluster[] clusters = new Cluster[3];
	private static double[] distances = new double[3];

	public static void main(String[] args) {
		initialize();
		readFileData();
		generateInitialCentroids();
		clustering();

	}// end main

	private static void clustering() {
		int i, j;
		int[] groups = new int[3];
		int count = 0;
		while (count < 15) {
			groups[0] = groups[1] = groups[2] = 0;
			for (i = 0; i < dataSize; i++) {
				for (j = 0; j < 3; j++)
					distances[j] = calculateDistances(clusters[j], myData[i]);

				if (distances[0] < distances[1] && distances[0] < distances[2]) {
					clusters[0].index[groups[0]] = i;
					groups[0]++;
				} else if (distances[1] < distances[0]
						&& distances[1] < distances[2]) {
					clusters[1].index[groups[1]] = i;
					groups[1]++;
				} else if (distances[2] < distances[0]
						&& distances[2] < distances[1]) {
					clusters[2].index[groups[2]] = i;
					groups[2]++;
				}
			}// end for i

			System.out.println("\nFor iteration : " + ++count);
			for (i = 0; i < 3; i++)
				displayClusters(i, groups[i]);
			System.out.println();

			FileWriter f1, f2, f3;
			try {
				f1 = new FileWriter("FirstCluster.txt");
				double a, b;
				// j=0;
				for (i = 0; i < groups[0]; i++) {
					a = myData[clusters[0].index[i]].x;
					b = myData[clusters[0].index[i]].y;
					f1.write(a + " " + b + " ");
				}
				f1.flush();

				f2 = new FileWriter("SecondCluster.txt");
				for (i = 0; i < groups[1]; i++) {
					a = myData[clusters[1].index[i]].x;
					b = myData[clusters[1].index[i]].y;
					f2.write(a + " " + b + " ");
				}
				f2.flush();

				f3 = new FileWriter("ThirdCluster.txt");
				for (i = 0; i < groups[2]; i++) {
					a = myData[clusters[2].index[i]].x;
					b = myData[clusters[2].index[i]].y;
					f3.write(a + " " + b + " ");
				}
				f3.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (i = 0; i < 3; i++)
				calculateCentroids(i, groups[i]);

		}// end while
	}// end clustering()

	private static void displayClusters(int num, int cluster) {
		System.out.println("Cluster no. " + num
				+ " & coordinates of centroid are : " + clusters[num].clusterX
				+ " " + clusters[num].clusterY);
		System.out.println("Cluster " + num + " contains " + cluster
				+ " points and their indices are :");

		for (int j = 0; j < cluster; j++)
			System.out.print(" " + clusters[num].index[j]);
		System.out.println();
	}// end displayClusters()

	private static void calculateCentroids(int j, int totalPts) {
		double sum1, sum2;
		sum1 = sum2 = 0.0;

		for (int i = 0; i < totalPts; i++) {
			sum1 += myData[clusters[j].index[i]].x;
			sum2 += myData[clusters[j].index[i]].y;
		}// end for i

		clusters[j].clusterX = sum1 / totalPts;
		clusters[j].clusterY = sum2 / totalPts;

		System.out.print("\nNew centroids : " + clusters[j].clusterX + ","
				+ clusters[j].clusterY);
	}// end calculateCentroids()

	private static double calculateDistances(Cluster C, AlgoData data) {
		return (Math.sqrt((Math.pow(C.clusterX - data.x, 2))
				+ (Math.pow(C.clusterY - data.y, 2))));
	}// end calculateDistances()

	private static void generateInitialCentroids() {
		int i;
		c1 = c2 = c3 = 0;
		while (c1 == c2 || c2 == c3 || c1 == c3) {
			c1 = (int) (Math.random() * 65);
			c2 = (int) (Math.random() * 65);
			c3 = (int) (Math.random() * 65);
		}// end while

		int[] points = new int[3];
		points[0] = c1;
		points[1] = c2;
		points[2] = c3;

		System.out.print("Initial centroids : \n");
		for (i = 0; i < 3; i++) {
			clusters[i] = new Cluster();
			clusters[i].clusterX = myData[points[i]].x;
			clusters[i].clusterY = myData[points[i]].y;
			System.out.println("Cluster =" + i + " " + clusters[i].clusterX
					+ "," + clusters[i].clusterY);
		}// end for i
	}// end generateInitialCentroids()

	private static void readFileData() {
		int i = 0, k = 0;
		try {
			File ob = new File("E:\\eclipse_old\\welcome\\KMeansDataFile.txt");
			Scanner fileScan = new Scanner(ob);
			while (fileScan.hasNext()) {
				myData[i].index = k;
				myData[i].x = fileScan.nextDouble();
				myData[i].y = fileScan.nextDouble();
				i++;
				k++;
			}// end while
			dataSize = i;
		} catch (IOException e) {
			System.out.print(e);
		}// end try-catch
	}// end readFileData()

	private static void initialize() {
		for (int i = 0; i < myData.length; i++)
			myData[i] = new AlgoData();
	}
}
