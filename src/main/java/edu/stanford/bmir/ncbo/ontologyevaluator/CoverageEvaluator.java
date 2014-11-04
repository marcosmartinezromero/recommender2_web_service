package edu.stanford.bmir.ncbo.ontologyevaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.bmir.ncbo.annotator.AnnotatorAdapter;
import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.annotator.util.AnnotatorUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;

/**
 * Ontology coverage evaluator
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 3/05/2014 20:43:38
 */
public class CoverageEvaluator {

	private Double topScore = null;

	/**
	 * @param input
	 * @param inputType
	 * @param annotations
	 * @param prefScore
	 *            Score assigned to "PREF" annotations (done with a concept
	 *            preferred name)
	 * @param synScore
	 *            Score assigned to "SYN" annotations (done with a concept
	 *            synonym)
	 * @param multiTermScore
	 *            Score assigned to annotations done with multi-word terms (e.g.
	 *            white blood cell)
	 * @return An object that encapsulates information about the coverage
	 *         provided by the ontology for a given input
	 */
	public CoverageResultTO evaluate(String input, int inputType,
			List<AnnotationTO> annotations, double prefScore, double synScore,
			double multiTermScore) {
		double score = 0;
		AnnotatorUtil util = new AnnotatorUtil();
		List<AnnotationTO> bestAnnotations = util.getBestAnnotationsForText(
				input, annotations, prefScore, synScore, multiTermScore);

		// The final score is computed as the sum of the scores of the selected
		// annotations
		for (AnnotationTO annotation : bestAnnotations) {
			score += util.getAnnotationScore(annotation, prefScore, synScore,
					multiTermScore);
		}

		if (topScore == null)
			topScore = getTopCoverageScore(input, inputType, prefScore,
					synScore, multiTermScore);

		// Score normalization
		double normalizedScore = normalizeScore(score, topScore);
		int numberOfTermsCovered = util
				.getNumberOfTermsCovered(bestAnnotations);
		int numberOfWordsCovered = util
				.getNumberOfWordsCovered(bestAnnotations);
		int totalWords = input.split("\\s+").length;
		double proportionWordsCovered = numberOfWordsCovered / totalWords;

		CoverageResultTO coverageResultTO = new CoverageResultTO(score,
				normalizedScore, numberOfTermsCovered, numberOfWordsCovered,
				proportionWordsCovered, bestAnnotations);

		return coverageResultTO;
	}

	/**
	 * @param score
	 * @param topScore
	 * @return Score normalized in the range [0,1]
	 */
	private double normalizeScore(double score, double topScore) {
		double normalizedScore = score / topScore;
		return normalizedScore;
	}

	/**
	 * @param input
	 * @param inputType
	 * @param prefScore
	 * @param synScore
	 * @param multiTermScore
	 * @return Maximum possible coverage score provided by all the annotations
	 *         for all the ontologies in BioPortal
	 */
	private double getTopCoverageScore(String input, int inputType,
			double prefScore, double synScore, double multiTermScore) {
		AnnotatorUtil util = new AnnotatorUtil();
		HashMap<String, List<AnnotationTO>> annotations = AnnotatorAdapter
				.getAnnotations(input, inputType, null);
		// All the annotations are put together
		List<AnnotationTO> allAnnotations = new ArrayList<AnnotationTO>();
		for (Map.Entry<String, List<AnnotationTO>> entry : annotations
				.entrySet()) {
			allAnnotations.addAll(entry.getValue());
		}
		Collections.enumeration(annotations.values());
		// The annotations that provide the best score are selected
		List<AnnotationTO> bestAnnotations = util.getBestAnnotationsForText(
				input, allAnnotations, prefScore, synScore, multiTermScore);
		double topScore = 0;
		// We calculate the final score as the sum of all scores
		for (AnnotationTO annotation : bestAnnotations) {
			topScore += util.getAnnotationScore(annotation, prefScore,
					synScore, multiTermScore);
		}
		return topScore;
	}

}