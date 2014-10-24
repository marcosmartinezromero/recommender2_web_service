package edu.stanford.bmir.ncbo.ontologyevaluator.result;

import java.util.List;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;

/**
 * Transfer Object that encapsulates information about the coverage of an
 * ontology for a given input
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 18/06/2014 22:20:01
 */
public class CoverageResultTO {
	private double score;
	// Score in the range [0,1]
	private double normalizedScore;
	private int numberTermsCovered;
	private int numberWordsCovered;
	// Proportion of words covered with respect to the total number of words
	private double proportionWordsCovered;
	// Best annotations. The previous scores and parameters have been calculated
	// using these annotations
	private List<AnnotationTO> annotations;

	public CoverageResultTO(double score, double normalizedScore,
			int numberTermsCovered, int numberWordsCovered,
			double proportionWordsCovered, List<AnnotationTO> annotations) {
		super();
		this.score = score;
		this.normalizedScore = normalizedScore;
		this.numberTermsCovered = numberTermsCovered;
		this.numberWordsCovered = numberWordsCovered;
		this.proportionWordsCovered = proportionWordsCovered;
		this.annotations = annotations;
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

	public int getNumberTermsCovered() {
		return numberTermsCovered;
	}

	public void setNumberTermsCovered(int numberTermsCovered) {
		this.numberTermsCovered = numberTermsCovered;
	}

	public int getNumberWordsCovered() {
		return numberWordsCovered;
	}

	public void setNumberWordsCovered(int numberWordsCovered) {
		this.numberWordsCovered = numberWordsCovered;
	}

	public double getProportionWordsCovered() {
		return proportionWordsCovered;
	}

	public void setProportionWordsCovered(double proportionWordsCovered) {
		this.proportionWordsCovered = proportionWordsCovered;
	}

	public List<AnnotationTO> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationTO> annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		return "CoverageResultTO [score=" + score + ", normalizedScore="
				+ normalizedScore + ", numberTermsCovered="
				+ numberTermsCovered + ", numberWordsCovered="
				+ numberWordsCovered + ", proportionWordsCovered="
				+ proportionWordsCovered + ", annotations=" + annotations + "]";
	}

}
