package edu.stanford.bmir.ncbo.ontologyevaluator.result;

/**
 * Transfer Object that encapsulates information about the acceptance of an
 * ontology by the biomedical community
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 08/05/2014 14:52:54
 */
public class AcceptanceResultTO {

	// Acceptance score (range [0,1])
	private double score;
	private double pubMedScore;
	private double bioportalPageviewsScore;
	private double umlsScore;

	public AcceptanceResultTO(double score, double pubMedScore,
			double bioportalPageviewsScore, double umlsScore) {
		this.score = score;
		this.pubMedScore = pubMedScore;
		this.bioportalPageviewsScore = bioportalPageviewsScore;
		this.umlsScore = umlsScore;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getPubMedScore() {
		return pubMedScore;
	}

	public void setPubMedScore(double pubMedScore) {
		this.pubMedScore = pubMedScore;
	}

	public double getBioportalPageviewsScore() {
		return bioportalPageviewsScore;
	}

	public void setBioportalPageviewsScore(double bioportalPageviewsScore) {
		this.bioportalPageviewsScore = bioportalPageviewsScore;
	}

	public double getUmlsScore() {
		return umlsScore;
	}

	public void setUmlsScore(double umlsScore) {
		this.umlsScore = umlsScore;
	}

	@Override
	public String toString() {
		return "AcceptanceResultTO [score=" + score + ", pubMedScore="
				+ pubMedScore + ", bioportalPageviewsScore="
				+ bioportalPageviewsScore + ", umlsScore=" + umlsScore + "]";
	}
	
	

}
