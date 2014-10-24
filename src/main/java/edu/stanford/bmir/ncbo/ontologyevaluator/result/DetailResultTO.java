/**
 * 
 */
package edu.stanford.bmir.ncbo.ontologyevaluator.result;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 30/08/2014 15:51:02
 */
public class DetailResultTO {

	private double score;
	private double definitionsScore;
	private double synonymsScore;
	private double propertiesScore;

	public DetailResultTO(double score, double definitionsScore,
			double synonymsScore, double propertiesScore) {
		this.score = score;
		this.definitionsScore = definitionsScore;
		this.synonymsScore = synonymsScore;
		this.propertiesScore = propertiesScore;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getDefinitionsScore() {
		return definitionsScore;
	}

	public void setDefinitionsScore(double definitionsScore) {
		this.definitionsScore = definitionsScore;
	}

	public double getSynonymsScore() {
		return synonymsScore;
	}

	public void setSynonymsScore(double synonymsScore) {
		this.synonymsScore = synonymsScore;
	}

	public double getPropertiesScore() {
		return propertiesScore;
	}

	public void setPropertiesScore(double propertiesScore) {
		this.propertiesScore = propertiesScore;
	}

	@Override
	public String toString() {
		return "DetailResultTO [score=" + score + ", definitionsScore="
				+ definitionsScore + ", synonymsScore=" + synonymsScore
				+ ", propertiesScore=" + propertiesScore + "]";
	}

}
