package projectBE;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

class AlgoData {
	int index;
	double x;
	double y;
}

class Cluster {
	double ClusterX;
	double ClusterY;
	int[] index = new int[65];
}

/**
 * @author Varad Meru
 * @throws IOException
 * 
 */

public class KMeansAlgoCode {
	private static AlgoData[] myData = new AlgoData[65];
	private static int DataSize = 0;
	private static int c1, c2, c3;
	private static Cluster[] clusters = new Cluster[3];
	private static double[] distances = new double[3];
	private static int group[] = new int[3];

	public static void main(String[] args) throws IOException {
		// TODO The main method of our program

		initialise();
		readFileData();
		generateInitialCentroids();
		Clustering();
		writeClusters();
	}

	public static void initialise() {
		// TODO Initial the Data Variables
		for (int z = 0; z < myData.length; z++)
			myData[z] = new AlgoData();
	}

	public static void readFileData() {
		// TODO To Read the input synthetic data from the file
		int i = 0, k = 0;
		File object = new File("KMeansDataFile.txt");
		Scanner fileScan;
		try {
			fileScan = new Scanner(object);
			while (fileScan.hasNext()) {
				myData[i].index = k;
				myData[i].x = fileScan.nextDouble();
				myData[i].y = fileScan.nextDouble();
				i++;
				k++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataSize = i;
	}

	public static void generateInitialCentroids() {
		// TODO To generate the initial Centroids for the first preliminary
		// round
		int i;
		c1 = c2 = c3 = 0;
		while (c1 == c2 || c2 == c3 || c3 == c1) {
			c1 = (int) (Math.random() * 65);
			c2 = (int) (Math.random() * 65);
			c3 = (int) (Math.random() * 65);
		}
		int[] points = new int[3];
		points[0] = c1;
		points[1] = c2;
		points[2] = c3;

		for (i = 0; i < 3; i++) {
			clusters[i] = new Cluster();
		}
		for (i = 0; i < 3; i++) {
			clusters[i].ClusterX = myData[points[i]].x;
			clusters[i].ClusterY = myData[points[i]].y;
		}
		for (i = 0; i < 3; i++) {
			System.out.println("Initial Centroids=" + i + " "
					+ clusters[i].ClusterX + " " + clusters[i].ClusterY + " ");
		}
	}

	public static void Clustering() throws IOException {
		// TODO Does he original K-Means Part with Clustering Distribution of
		// members
		int i, j;
		int count = 0;
		while (count < 15) {
			group[0] = group[1] = group[2] = 0;
			for (i = 0; i < DataSize; i++) {
				for (j = 0; j < 3; j++)
					distances[j] = CalculateDistances(clusters[j], myData[i]);
				if ((distances[0] < distances[1])
						&& (distances[0] < distances[2])) {
					clusters[0].index[group[0]] = i;
					group[0]++;
				} else if ((distances[1] < distances[0])
						&& (distances[1] < distances[2])) {
					clusters[1].index[group[1]] = i;
					group[1]++;
				} else if ((distances[2] < distances[0])
						&& (distances[2] < distances[1])) {
					clusters[2].index[group[2]] = i;
					group[2]++;
				}
			}
			System.out.println("iteration: " + count);
			for (i = 0; i < 3; i++) {
				displayClusters(i, group[i]);
			}
			System.out.println();
			for (i = 0; i < 3; i++)
				CalculateCentroids(i, group[i]);
			count++;
		}
	}

	public static void writeClusters() throws IOException {
		for (int i = 0; i < clusters.length; i++) {
			File tempfile = new File("Cluster" + i + ".txt");
			// BufferedOutputStream bf=new BufferedOutputStream(new
			// DataOutputStream(new FileOutputStream(tempfile)));
			DataOutputStream output = new DataOutputStream(
					new FileOutputStream(tempfile));
			output.writeUTF("Centroid " + clusters[i].ClusterX + " "
					+ clusters[i].ClusterY + "\n");
			for (int j = 0; j < group[i]; j++) {
				output.writeUTF("" + myData[clusters[i].index[j]].x);
				output.writeUTF("\t");
				output.writeUTF("" + myData[clusters[i].index[j]].y);
				output.writeUTF("\n");
			}
			output.close();
		}
	}

	public static void displayClusters(int num, int cluster) {
		// TODO It displays the clusters with an array of members
		System.out.println(clusters[num].ClusterX + " "
				+ clusters[num].ClusterY);
		for (int j = 0; j < cluster; j++) {
			System.out.print(" " + clusters[num].index[j]);
		}
		System.out.print(" " + cluster);

	}

	public static void CalculateCentroids(int j, int totalpoints) {
		// TODO Calculates the centroids based on the distribution of current
		// clusters to find the appropriate or precise centroid
		double sum1, sum2;
		sum1 = sum2 = 0.0;
		for (int i = 0; i < totalpoints; i++) {
			sum1 = sum1 + myData[clusters[j].index[i]].x;
			sum2 = sum2 + myData[clusters[j].index[i]].y;
		}
		clusters[j].ClusterX = sum1 / totalpoints;
		clusters[j].ClusterY = sum2 / totalpoints;
		System.out.println();
		System.out.print("New Centroids: " + clusters[j].ClusterX + " "
				+ clusters[j].ClusterY);
	}

	public static double CalculateDistances(Cluster cluster, AlgoData algoData) {
		// TODO Calculates the Minimum Distance for each corresponding
		// coordinate
		return Math.sqrt(Math.pow(cluster.ClusterX - algoData.x, 2)
				+ Math.pow(cluster.ClusterY - algoData.y, 2));
	}
}