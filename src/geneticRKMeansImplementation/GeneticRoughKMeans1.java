package geneticRKMeansImplementation;

import javaGAlib.*;

/**
 * @author VaradMeru
 * @date 19-Dec-2010 1200Hrs
 * @name GeneticRoughKMeans1.java
 * 
 */
public class GeneticRoughKMeans1 {

	public GeneticRoughKMeans1(String filename, int numClusters,
			double thrshold, double lowerbound, int generations, double xo,
			double mut, int randomSelect, int decPrec, boolean positive,
			boolean stats) {
		fileName = filename;
		numOfClusters = numClusters;
		threshold = thrshold;
		w_l = lowerbound;
		w_u = 1 - w_l;
		maxGenerations = generations;
		xoProb = xo;
		chMut = mut;
		ranSelect = randomSelect;
		precision = decPrec;
		positiveOnly = positive;
		compStats = stats;
	}

	private static String fileName;

	/** Number of objects to be grouped into cluster */
	private static int numOfObjects;

	/** Number of dimensions of the object */
	private static int numOfDimensions;

	/** Number of Clusters Required */
	private static int numOfClusters;

	/** Maximum Number of Generations */
	private static int maxGenerations;

	/** Rough Set Clustering Parameters */
	private static double w_l, w_u, threshold;

	/** Genetic Algorithm Parameters */
	private static double xoProb, chMut, ranSelect;

	/** Input Parameters */
	private static int precision;
	private static boolean positiveOnly;
	private static boolean compStats;

	public static void main(String[] s) {
		if (s.length < 12) {
			System.err.println("Usage :\nRoughKMeans input Data File "
					+ "\nNumber of Clusters " + "\nThreshold "
					+ "\nWeight_Lower " + "\nNumber of Generations "
					+ "\nCrossover Probability(0-1) "
					+ "\nChromosome Mutation Probability(0-1)"
					+ "\nRandom Selection(%) " + "\nCrossoverType(0-3)"
					+ "\nPrecision of Double"
					+ "\nOnly Consider Positive Values ?"
					+ "\nCompute Statistics ? ");
			System.err
					.println("Example : KMeansData.txt 3 0.7 0.85 500 .7 0.05 40 2 1 true true");
			System.exit(1);
		}
		/*
		 * public GeneticRoughKMeans1(String filename, int numClusters, double
		 * thrshold, double lowerbound, int generations, double xo, double mut,
		 * int randomSelect, int decPrec, boolean positive, boolean stats)
		 */

		GeneticRoughKMeans1 roughKmeans = new GeneticRoughKMeans1(s[0], Integer
				.parseInt(s[1]), Double.parseDouble(s[2]), Double
				.parseDouble(s[3]), Integer.parseInt(s[4]), Double
				.parseDouble(s[5]), Double.parseDouble(s[6]), Integer
				.parseInt(s[7]), Integer.parseInt(s[8]), Boolean
				.parseBoolean(s[9]), Boolean.parseBoolean(s[10]));
		

	}
}
