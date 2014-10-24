package edu.stanford.bmir.ncbo.ontologyevaluator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.bmir.ncbo.annotator.AnnotatorAdapter;
import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.ontologies.util.OntologyMetricsUtil;
import edu.stanford.bmir.ncbo.ontologies.util.to.OntologyMetricsTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.SpecializationResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.MathUtil;
import edu.stanford.bmir.ncbo.recommender.util.NormUtil;

/**
 * Ontology specialization evaluator
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 01/05/2014 16:54:16
 */
public class SpecializationEvaluator {

	private static HashMap<String, SpecializationResultTO> specializationScores = null;
	private static String lastInput = "";
	private static int lastInputType = -1;

	/**
	 * @param input
	 * @param inputType
	 * @param ontologyUri
	 * @return An object that encapsulates information about how specialized the
	 *         ontology is for a given input
	 */
	public static SpecializationResultTO evaluate(String input, int inputType,
			String ontologyUri) {
		// Initialization of the static variable, or update if the input changed
		if ((input.compareTo(lastInput) != 0) || (inputType != lastInputType)) {
			lastInputType = inputType;
			lastInput = input;
			specializationScores = new HashMap<String, SpecializationResultTO>();
			// All annotations are obtained
			HashMap<String, List<AnnotationTO>> allAnnotations = AnnotatorAdapter
					.getAnnotations(input, inputType, null);
			for (Map.Entry<String, List<AnnotationTO>> entry : allAnnotations
					.entrySet()) {
				String oUri = entry.getKey();
				OntologyMetricsTO metrics = OntologyMetricsUtil
						.getOntologyMetrics(entry.getKey());
				int numberOfClasses;
				double specializationScore;

				if (metrics != null) {
					numberOfClasses = metrics.getClasses();
					if (numberOfClasses == 0) {
						specializationScore = 0;
					} else {

						int numberOfAnnotations = entry.getValue().size();
						// double denominator =
						// MathUtil.getScoreAccordingToDistribution(
						// numberOfClasses,
						// OntologyMetricsUtil.getOrderedNumberOfClassesAllOntologies(),
						// 10);
						double denominator = Math.log10(numberOfClasses);
						specializationScore = (double) numberOfAnnotations
								/ denominator;
					}

				} else {

					// TODO: this has been done to give a good result for the
					// NCIT, because BioPortal fails when try to access to
					// ontology metrics for it
					if (oUri.compareTo("http://data.bioontology.org/ontologies/NCIT") == 0) {
						numberOfClasses = 300000;
						int numberOfAnnotations = entry.getValue().size();
						double denominator = Math.log10(numberOfClasses);
						specializationScore = (double) numberOfAnnotations
								/ denominator;
					} else
						specializationScore = 0;
				}
				// The normalizedScore will be computed and assigned after
				// evaluating all the ontologies
				specializationScores.put(oUri, new SpecializationResultTO(
						specializationScore, -1));
			}

			// Scores normalization
			double topSpecializationScore = getTopSpecializationScore();
			for (Map.Entry<String, SpecializationResultTO> entry : specializationScores
					.entrySet()) {
				String oUri = entry.getKey();
				double score = entry.getValue().getScore();
				NormUtil normalizer = new NormUtil(topSpecializationScore, 0,
						1, 0);
				double normalizedScore = normalizer.normalize(score);
				specializationScores.put(oUri, new SpecializationResultTO(
						score, normalizedScore));
			}
		}
		if (specializationScores.containsKey(ontologyUri))
			return specializationScores.get(ontologyUri);
		else
			return null;
	}

	/**
	 * @return Maximum specialization score for all the ontologies evaluated
	 */
	private static double getTopSpecializationScore() {
		double topScore = 0;
		for (Map.Entry<String, SpecializationResultTO> entry : specializationScores
				.entrySet()) {
			double score = entry.getValue().getScore();
			if (score > topScore)
				topScore = score;
		}
		return topScore;
	}
}
