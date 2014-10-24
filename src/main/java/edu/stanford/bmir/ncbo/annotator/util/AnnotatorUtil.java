package edu.stanford.bmir.ncbo.annotator.util;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;

/**
 * Common operations to manage BioPortal annotations
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 27/05/2014 17:23:18
 */
public class AnnotatorUtil {

	/**
	 * Selects the best annotations (i.e. annotations with the highest score)
	 * for the input text from a given list of annotations
	 * 
	 * @param text
	 * @param annotations
	 * @param prefScore
	 *            Score assigned to "PREF" annotations (done with a concept
	 *            preferred name)
	 * @param synScore
	 *            Score assigned to "SYN" annotations (done with a concept
	 *            synonym)
	 * @param multiTermScore
	 *            Score assigned to annotations in which a concept annotates a
	 *            term composed by several words (e.g. white blood cell)
	 * @return A list of annotations
	 */
	public List<AnnotationTO> getBestAnnotationsForText(String text,
			List<AnnotationTO> annotations, double prefScore, double synScore,
			double multiTermScore) {
		AnnotatorUtil util = new AnnotatorUtil();
		List<AnnotationTO> bestAnnotations = new ArrayList<AnnotationTO>();
		// Compute the best annotation for each text fragment
		int start = 0;
		int end = 0;
		while (start + 1 < text.length()) {
			end = start + 1;
			AnnotationTO annotation = util.getBestAnnotationForTextFragment(
					start, end, annotations, prefScore, synScore,
					multiTermScore);
			if (annotation != null) {
				bestAnnotations.add(annotation);
				start = annotation.getTo() + 1;
			} else {
				start++;
			}
		}
		return bestAnnotations;
	}

	/**
	 * @param start
	 * @param end
	 * @param annotations
	 * @param prefScore
	 * @param synScore
	 * @param multiTermScore
	 * @return The best annotation for a text fragment
	 */
	private AnnotationTO getBestAnnotationForTextFragment(int start, int end,
			List<AnnotationTO> annotations, double prefScore, double synScore,
			double multiTermScore) {
		// Get all the annotations for the text fragment
		List<AnnotationTO> annotationsForTextFragment = getAllAnnotationsForTextFragment(
				start, end, annotations);
		// Get the best annotation for the text fragment
		AnnotationTO bestAnnotation = getBestAnnotation(
				annotationsForTextFragment, prefScore, synScore, multiTermScore);
		return bestAnnotation;
	}

	/**
	 * @param start
	 * @param end
	 * @param annotations
	 * @return All the annotations for a text fragment
	 */
	private List<AnnotationTO> getAllAnnotationsForTextFragment(int start,
			int end, List<AnnotationTO> annotations) {
		List<AnnotationTO> annotationsForTextFragment = new ArrayList<AnnotationTO>();
		for (AnnotationTO annotation : annotations) {
			if ((annotation.getFrom() == start) && (annotation.getTo() >= end))
				annotationsForTextFragment.add(annotation);
		}
		return annotationsForTextFragment;
	}

	/**
	 * @param annotations
	 * @param prefScore
	 * @param synScore
	 * @param multiTermScore
	 * @return The best annotation (i.e. annotation with the highest score) from
	 *         a list of annotations
	 */
	private AnnotationTO getBestAnnotation(List<AnnotationTO> annotations,
			double prefScore, double synScore, double multiTermScore) {
		AnnotationTO bestAnnotation = null;
		double bestAnnotationScore = 0;
		for (AnnotationTO annotation : annotations) {
			if (bestAnnotation == null) {
				bestAnnotation = annotation;
				bestAnnotationScore = getAnnotationScore(annotation, prefScore,
						synScore, multiTermScore);
			} else {
				double annotationScore = getAnnotationScore(annotation,
						prefScore, synScore, multiTermScore);
				// TODO: for the ontology sets input, it is possible to have
				// multiple annotations from different ontologies for the same
				// input text, that is, multiple "best annotations" for a
				// specific text. In this case, it could be useful to use the
				// evaluation results for other evaluation criteria (e.g.
				// acceptance and specialization) to select the best annotation.
				// At the moment, the first annotation is kept.
				if (annotationScore > bestAnnotationScore) {
					bestAnnotation = annotation;
					bestAnnotationScore = getAnnotationScore(annotation,
							prefScore, synScore, multiTermScore);
				}
			}
		}
		return bestAnnotation;
	}

	/**
	 * @param annotations
	 * @return The number of terms covered by a list of annotations. Note: a
	 *         term can be composed by one or multiple words
	 */
	public int getNumberOfTermsCovered(List<AnnotationTO> annotations) {
		return annotations.size();
	}

	/**
	 * @param annotations
	 * @return The number of words covered by a list of annotations
	 */
	public int getNumberOfWordsCovered(List<AnnotationTO> annotations) {
		int wordsCovered = 0;
		for (AnnotationTO annotation : annotations) {
			wordsCovered += annotation.getText().split("\\s+").length;
		}

		return wordsCovered;
	}
	
	public double getTotalScoreForAnnotations(List<AnnotationTO> annotations,
			double prefScore, double synScore, double multiTermScore) {
		double totalScore = 0;
		for (AnnotationTO annotation : annotations) {
			totalScore += getAnnotationScore(annotation, prefScore,
					synScore, multiTermScore);			
		}
		return totalScore;		
	}

	/**
	 * @param annotation
	 * @param prefScore
	 * @param synScore
	 * @param multiTermScore
	 * @return The score computed for given annotation
	 */
	public double getAnnotationScore(AnnotationTO annotation, double prefScore,
			double synScore, double multiTermScore) {
		double totalScore = 0;
		double annotationTypeScore = 0;
		double numberOfAnnotatedWords = annotation.getText().split("\\s+").length;

		if (annotation.getMatchType().compareTo("PREF") == 0)
			annotationTypeScore = prefScore;
		else
			annotationTypeScore = synScore;

		if (numberOfAnnotatedWords == 1)
			totalScore = annotationTypeScore;
		// Multi-word annotation
		else
			totalScore = (annotationTypeScore + multiTermScore)
					* numberOfAnnotatedWords;

		return totalScore;
	}
}
