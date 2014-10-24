package edu.stanford.bmir.ncbo.ontologyevaluator.scores;

/**
 * Transfer Object that encapsulates information an evaluation score
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 07/05/2014 18:04:36
 */
public class ScoreTO {
	private double score;
	// Weight assigned to that score
	private double weight;
	
	public ScoreTO(double score, double weight) {
		super();
		this.score = score;
		this.weight = weight;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "ScoreTO [score=" + score + ", weight=" + weight + "]";
	}
	
	
}


