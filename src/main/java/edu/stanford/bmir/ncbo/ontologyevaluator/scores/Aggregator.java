/**
 * 
 */
package edu.stanford.bmir.ncbo.ontologyevaluator.scores;

import java.util.List;

/**
 * Combines all the scores into a single one
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 07/05/2014 18:00:53
 */
public class Aggregator {

	public static double aggregateScores(List<ScoreTO> scores) {
		double finalScore = 0;
		
		for (ScoreTO score : scores) {
			finalScore += score.getScore() * score.getWeight();
		}
		
		return finalScore;
	}
}
