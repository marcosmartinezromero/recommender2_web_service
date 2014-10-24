package edu.stanford.bmir.ncbo.recommender.result;

import java.util.List;

import edu.stanford.bmir.ncbo.ontologyevaluator.result.AcceptanceResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.DetailResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.SpecializationResultTO;

/**
 * Transfer Object that encapsulates all the information about each item
 * (ontology or set of ontologies) suggested by the Recommender
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 31/07/2014 19:12:22
 */
public class RecommendationResultTO implements
		Comparable<RecommendationResultTO> {
	private List<String> ontologyUris;
	private List<String> ontologyUrisBioPortal;
	private List<String> ontologyNames;
	private List<String> ontologyAcronyms;
	private double finalScore;
	private CoverageResultTO coverageResult;
	private SpecializationResultTO specializationResult;
	private AcceptanceResultTO acceptanceResult;
	private DetailResultTO detailResult;

	public RecommendationResultTO(List<String> ontologyUris,
			List<String> ontologyUrisBioPortal, List<String> ontologyNames,
			List<String> ontologyAcronyms, double finalScore,
			CoverageResultTO coverageResult,
			SpecializationResultTO specializationResult,
			AcceptanceResultTO acceptanceResult, DetailResultTO detailResult) {
		this.ontologyUris = ontologyUris;
		this.ontologyUrisBioPortal = ontologyUrisBioPortal;
		this.ontologyNames = ontologyNames;
		this.ontologyAcronyms = ontologyAcronyms;
		this.finalScore = finalScore;
		this.coverageResult = coverageResult;
		this.specializationResult = specializationResult;
		this.acceptanceResult = acceptanceResult;
		this.detailResult = detailResult;
	}

	public List<String> getOntologyUris() {
		return ontologyUris;
	}

	public void setOntologyUris(List<String> ontologyUris) {
		this.ontologyUris = ontologyUris;
	}

	public List<String> getOntologyUrisBioPortal() {
		return ontologyUrisBioPortal;
	}

	public void setOntologyUrisBioPortal(List<String> ontologyUrisBioPortal) {
		this.ontologyUrisBioPortal = ontologyUrisBioPortal;
	}

	public List<String> getOntologyNames() {
		return ontologyNames;
	}

	public void setOntologyNames(List<String> ontologyNames) {
		this.ontologyNames = ontologyNames;
	}

	public List<String> getOntologyAcronyms() {
		return ontologyAcronyms;
	}

	public void setOntologyAcronyms(List<String> ontologyAcronyms) {
		this.ontologyAcronyms = ontologyAcronyms;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}

	public CoverageResultTO getCoverageResult() {
		return coverageResult;
	}

	public void setCoverageResult(CoverageResultTO coverageResult) {
		this.coverageResult = coverageResult;
	}

	public SpecializationResultTO getSpecializationResult() {
		return specializationResult;
	}

	public void setSpecializationResult(
			SpecializationResultTO specializationResult) {
		this.specializationResult = specializationResult;
	}

	public AcceptanceResultTO getAcceptanceResult() {
		return acceptanceResult;
	}

	public void setAcceptanceResult(AcceptanceResultTO acceptanceResult) {
		this.acceptanceResult = acceptanceResult;
	}

	public DetailResultTO getDetailResult() {
		return detailResult;
	}

	public void setDetailResult(DetailResultTO detailResult) {
		this.detailResult = detailResult;
	}

	// Allows to compare recommended items to rank them
	@Override
	public int compareTo(RecommendationResultTO r1) {
		if (this.getFinalScore() == r1.getFinalScore()) {
			if (this.getCoverageResult().getScore() == r1.getCoverageResult()
					.getScore()) {
				if (this.getOntologyUris().size() == r1.getOntologyUris()
						.size())
					return 0;
				else if (this.getOntologyUris().size() > r1.getOntologyUris()
						.size())
					return 1;
				else
					return -1;
			} else if (this.getCoverageResult().getScore() < r1
					.getCoverageResult().getScore())
				return 1;
			else
				return -1;

		} else if (this.getFinalScore() < r1.getFinalScore())
			return 1;
		else
			return -1;
	}

	@Override
	public String toString() {
		return "RecommendationResultTO [ontologyUris=" + ontologyUris
				+ ", ontologyUrisBioPortal=" + ontologyUrisBioPortal
				+ ", ontologyNames=" + ontologyNames + ", finalScore="
				+ finalScore + ", coverageResult=" + coverageResult
				+ ", specializationResult=" + specializationResult
				+ ", acceptanceResult=" + acceptanceResult + ", detailResult="
				+ detailResult + "]";
	}

}
