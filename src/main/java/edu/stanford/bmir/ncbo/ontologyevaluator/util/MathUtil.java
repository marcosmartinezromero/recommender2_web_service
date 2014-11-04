package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import edu.stanford.bmir.ncbo.ontologies.util.OntologyMetricsUtil;

/**
 * Some useful mathematical functions
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 03/06/2014 16:30:09
 */
public class MathUtil {

	/**
	 * Calculates the score according to how the rest of the scores are
	 * distributed
	 * 
	 * @param value
	 * @param orderedValues
	 * @param maxScore
	 * @return
	 */
	public static double getScoreAccordingToDistribution(double value,
			double orderedValues[], double maxScore) {		
		double[] cutPoints = getDeciles(orderedValues);
		
		double score = getScoreUsingCutPoints(value, cutPoints, maxScore);
		return score;
	}

	/**
	 * Allows to calculate the deciles for an array of ordered values
	 * 
	 * 
	 * @param orderedValues
	 * @return Deciles for the array of ordered values
	 */
	public static double[] getDeciles(double orderedValues[]) {
		Percentile p = new Percentile();
		// Calculation of cut points using deciles 1 to 9 (percentiles 10 to 90)
		double[] deciles = new double[9];
		int j = 10;
		for (int i = 0; i < 9; i++) {
			deciles[i] = p.evaluate(orderedValues, j);
			j += 10;
		}
		return deciles;
	}

	/**
	 * 
	 * @param cutPoints
	 * @param value
	 * @param maxScore
	 * @return The corresponding score using the interval determined by the
	 *         cutPoints
	 */
	private static double getScoreUsingCutPoints(double value,
			double[] cutPoints, double maxScore) {
		double intervalScore = maxScore / (cutPoints.length+1);
		if (value < cutPoints[0])
			return 1;
		for (int i = 1; i < cutPoints.length; i++) {
			if (value < cutPoints[i])
				return (5*i * intervalScore);
		}
		return (5*(cutPoints.length+1) * intervalScore);
	}

	/*** Test code ***/
	public static void main(String[] args) {
		double[] values = { 5, 50, 150, 200, 350, 600, 1000, 1900, 4000, 20000, 23000 };
		double[] classes = OntologyMetricsUtil
				.getOrderedNumberOfClassesAllOntologies();
		double[] cutPoints = getDeciles(classes);
		System.out.println(Arrays.toString(cutPoints));
		for (int i = 0; i < values.length; i++)
			System.out.println(getScoreAccordingToDistribution(values[i],
					classes, 10));
	}

}
