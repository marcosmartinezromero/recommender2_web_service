package edu.stanford.bmir.ncbo.ontologyevaluator.result;

/**
 * Transfer Object that encapsulates information about the specialization of an
 * ontology for a given input
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 05/05/2014 11:20:44
 */
public class SpecializationResultTO {

	private double score;
	// Score in the range [0,1]
	// The normalized score is computed after evaluating all the ontologies
	private double normalizedScore;

	public SpecializationResultTO(double score, double normalizedScore) {
		super();
		this.score = score;
		this.normalizedScore = normalizedScore;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getNormalizedScore() {
		return normalizedScore;
	}

	public void setNormalizedScore(double normalizedScore) {
		this.normalizedScore = normalizedScore;
	}

	@Override
	public String toString() {
		return "SpecializationResultTO [score=" + score + ", normalizedScore="
				+ normalizedScore + "]";
	}
}
