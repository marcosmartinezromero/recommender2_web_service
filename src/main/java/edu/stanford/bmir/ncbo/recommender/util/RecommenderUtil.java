/**
 * 
 */
package edu.stanford.bmir.ncbo.recommender.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.annotator.util.AnnotatorUtil;
import edu.stanford.bmir.ncbo.recommender.result.RecommendationResultTO;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 18/08/2014 18:56:55
 */
public class RecommenderUtil {

	/**
	 * Given the large number of ontologies in BioPortal (more than 380 by Aug.
	 * 2014) it is necessary to filter the ontologies that will be used to
	 * create the sets of ontologies (combinations) that will be evaluated. That
	 * is the aim of this method. If all the ontologies are used, the system
	 * will be too slow.
	 * 
	 * @param annotations
	 * @return The selected ontology uris
	 */
//	public static List<String> selectUrisForCombinedRanking_old(
//			HashMap<String, List<AnnotationTO>> annotationsForUris) {
//		List<String> selectedUris = new ArrayList<String>();
//		List<String> urisAlreadyCompared = new ArrayList<String>();
//
//		for (Map.Entry<String, List<AnnotationTO>> entry : annotationsForUris
//				.entrySet()) {
//			boolean contained = false;
//			String uri = entry.getKey();
//			urisAlreadyCompared.add(uri);
//			List<AnnotationTO> annotations = entry.getValue();
//			innerloop: for (Map.Entry<String, List<AnnotationTO>> entry2 : annotationsForUris
//					.entrySet()) {
//				if (!urisAlreadyCompared.contains(entry2.getKey())) {
//					contained = isContainedIn(annotations, entry2.getValue());
//					// If the annotations are a subset of the annotations of
//					// other ontology
//					if (contained == true) {
//						break innerloop;
//					}
//				}
//			}
//			if (contained == false) {
//				selectedUris.add(uri);
//			}
//		}
//
//		return selectedUris;
//	}
	
	/**
	 * Given the large number of ontologies in BioPortal (more than 380 by Aug.
	 * 2014) it is necessary to filter the ontologies that will be used to
	 * create the sets of ontologies (combinations) that will be evaluated. That
	 * is the aim of this method. If all the ontologies are used, the system
	 * will be too slow.
	 * 
	 * @param annotations
	 * @return The selected ontology uris
	 */
//	public static List<String> selectUrisForCombinedRanking_old(
//			Map<String, List<AnnotationTO>> annotations) {
//		List<String> selectedUris = new ArrayList<String>();
//		List<String> urisAlreadyCompared = new ArrayList<String>();
//
//		for (Map.Entry<String, List<AnnotationTO>> entry : annotations.entrySet()) {
//			System.out.println(entry.getKey() + " -> " + entry.getValue().size());
//		}
//		
//		// Sort the annotationsForUris by the number of annotations
//		Map<String, List<AnnotationTO>> sortedAnnotationsForUris = 
//				sortByValueSize(annotations);
//		System.out.println("---Sorted---");
//		
//		for (Map.Entry<String, List<AnnotationTO>> entry : sortedAnnotationsForUris.entrySet()) {
//			System.out.println(entry.getKey() + " -> " + entry.getValue().size());
//		}
//		
//		for (Map.Entry<String, List<AnnotationTO>> entry : sortedAnnotationsForUris.entrySet()) {			
//			boolean contained = false;
//			String uri = entry.getKey();
//			urisAlreadyCompared.add(uri);			
//			innerloop: for (Map.Entry<String, List<AnnotationTO>> entry2 : sortedAnnotationsForUris
//					.entrySet()) {
//				if (!urisAlreadyCompared.contains(entry2.getKey())) {
//					contained = isContainedIn(entry.getValue(), entry2.getValue());
//					// If the annotations are a subset of the annotations of
//					// other ontology
//					if (contained == true) {
//						break innerloop;
//					}
//				}
//			}
//			if (contained == false) {
//				System.out.println("Selected: " + entry.getKey() + " -> " + entry.getValue().size());
//				selectedUris.add(uri);
//			}
//		}
//
//		return selectedUris;
//	}
	
	/**
	 * Given the large number of ontologies in BioPortal (more than 380 by Aug.
	 * 2014) it is necessary to filter the ontologies that will be used to
	 * create the sets of ontologies (combinations) that will be evaluated. That
	 * is the aim of this method. If all the ontologies are used, the system
	 * will be too slow.
	 * 
	 * If the annotations provided by an ontology O1 include all the annotations
	 * provided by another ontology O2, then O2 can be ignored and it will not
	 * be taking into account to generate ontology combinations. If one
	 * particular annotation is provided by two different ontologies, then the
	 * ontology that has a better evaluation score (excluding the coverage
	 * criteria) will be selected.
	 * 
	 */
	public static List<String> selectUrisForCombinedRanking(
			Map<String, List<AnnotationTO>> annotations, double prefScore,
			double synScore, double multiTermScore, 
			Map<String, RecommendationResultTO> singleEvaluationResults,
			double wd, double ws, double wa) {
		List<String> selectedUris = new ArrayList<String>();

//		System.out.println("Annotations: ");
//		for (Map.Entry<String, List<AnnotationTO>> entry : annotations
//				.entrySet()) {
//			System.out.println("  " + entry.getKey());
//			for (AnnotationTO a : entry.getValue()) {
//				System.out.println("    " + a);
//			}
//		}
		
		String uriToRemove = null;
		for (Map.Entry<String, List<AnnotationTO>> entry : annotations
				.entrySet()) {

//			System.out.println("***Selected Uris: ***");
//			for (String s : selectedUris) {
//				System.out.println("  " + s);
//			}
//			System.out.println("*********************");

			boolean containedAinB = false;
			boolean containedBinA = false;
			boolean sameUri = false;

			innerloop: for (String uri : selectedUris) {
//				System.out.println("Comparison: " + entry.getKey() + " vs " + uri);
				if (entry.getKey().compareTo(uri) != 0) {
					// Check if the annotations for the candidate URI are
					// contained into the annotations of the selected URI
					containedAinB = isContainedIn(entry.getKey(), entry.getValue(),
							uri, annotations.get(uri), prefScore, synScore,
							multiTermScore, singleEvaluationResults, wd, ws, wa);
					containedBinA = isContainedIn(uri, annotations.get(uri),
							entry.getKey(), entry.getValue(), prefScore, synScore,
							multiTermScore, singleEvaluationResults, wd, ws, wa);
					
//					if (containedAinB) {
//						System.out.println("  " + entry.getKey() + " contained in " + uri);
//					}

					if (containedBinA) {
//						System.out.println("  " + uri + " contained in " + entry.getKey());
						uriToRemove = uri;
					}

					if ((containedAinB == true) || (containedBinA == true)) {						
						break innerloop;
					}
				} else {
//					System.out.println("  They are the same Uri");
					sameUri = true;
				}
			}

			if (!sameUri) {

				if (containedAinB) {
//					System.out.println("  Action: do nothing");
					// Do nothing
				}
				if (containedBinA) {
//					System.out.println("  Action: Remove " + uriToRemove + " and add " + entry.getKey());					
					selectedUris.remove(uriToRemove);
					selectedUris.add(entry.getKey());
				}

				if ((containedAinB == false) && (containedBinA == false)) {
//					System.out.println("  Action: Add " + entry.getKey());
					// System.out.println("Selected: " + entry1.getKey() +
					// " -> " + entry1.getValue().size());
					selectedUris.add(entry.getKey());
				}
			}
		}

		return selectedUris;
	}
	
//	public static Map<String, List<AnnotationTO>> sortByValueSize(Map<String, List<AnnotationTO>> unsortMap) {	 
//		List<List<AnnotationTO>> list = new LinkedList(unsortMap.entrySet());
//	 
//		Collections.sort(list, new Comparator() {
//			public int compare(Object o1, Object o2) {
//				return ((Comparable) ((List)((Map.Entry) (o1)).getValue()).size())
//							.compareTo(((List)((Map.Entry) (o2)).getValue()).size());
//			}
//		});
//	 
//		Map<String, List<AnnotationTO>> sortedMap = new LinkedHashMap<String, List<AnnotationTO>>();
//		for (Iterator it = list.iterator(); it.hasNext();) {
//			Map.Entry entry = (Map.Entry) it.next();
//			sortedMap.put((String)entry.getKey(), (List<AnnotationTO>)entry.getValue());
//		}
//		return sortedMap;
//	}
	
	private static boolean isContainedIn(String uri1,
			List<AnnotationTO> annotations1, String uri2,
			List<AnnotationTO> annotations2, double prefScore, double synScore,
			double multiTermScore,
			Map<String, RecommendationResultTO> singleEvaluationResults,
			double wd, double ws, double wa) {
		AnnotatorUtil util = new AnnotatorUtil();
		for (AnnotationTO a1 : annotations1) {
			boolean contained = false;
			innerloop: for (AnnotationTO a2 : annotations2) {
				if ((a1.getFrom() >= a2.getFrom())
						&& (a1.getTo() <= a2.getTo())) {
					// Compare annotation scores
					double a1Score = util.getAnnotationScore(a1, prefScore,
							synScore, multiTermScore);
					double a2Score = util.getAnnotationScore(a2, prefScore,
							synScore, multiTermScore);
					if (a2Score > a1Score) {
						contained = true;
						break innerloop;
					} else if (a2Score == a1Score) {
						if (ws == 0 || wa == 0) {
							// Do nothing
						} else {
							// If two ontologies provide exactly the same
							// annotation
							// then we give priority to the ontology that has
							// better
							// evaluation score for the rest of evaluation
							// criteria
							RecommendationResultTO results1 = singleEvaluationResults
									.get(uri1);
							RecommendationResultTO results2 = singleEvaluationResults
									.get(uri2);
							// System.out.println("  " + uri1 + " : SpecScore="
							// +
							// results1.getSpecializationResult().getNormalizedScore()
							// + "; AcceptScore=" +
							// results1.getAcceptanceResult().getScore());
							// System.out.println("  " + uri2 + " : SpecScore="
							// +
							// results2.getSpecializationResult().getNormalizedScore()
							// + "; AcceptScore=" +
							// results2.getAcceptanceResult().getScore());
							double uri1Score = /* wd*... */ws
									* results1.getSpecializationResult()
											.getNormalizedScore() + wa
									* results1.getAcceptanceResult().getScore();
							double uri2Score = /* wd*... */ws
									* results2.getSpecializationResult()
											.getNormalizedScore() + wa
									* results2.getAcceptanceResult().getScore();
							// System.out.println("  Uri1Score = " + uri1Score +
							// " | Uri2Score = " + uri2Score);
							if (uri2Score > uri1Score) {
								// System.out.println(" " + uri1 +
								// " containedIn " + uri2);
								contained = true;
								break innerloop;
							}
						}
					}
				}
			}
			if (contained == false)
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		List<AnnotationTO> annotations1 = new ArrayList<AnnotationTO>();
		List<AnnotationTO> annotations2 = new ArrayList<AnnotationTO>();

		AnnotationTO a1 = new AnnotationTO(1, 3, "", "", "", "");
		AnnotationTO a2 = new AnnotationTO(2, 6, "", "", "", "");
		annotations1.add(a1);

		annotations2.add(a1);
		annotations2.add(a2);

		//System.out.println(containsAll(annotations2, annotations1, 10, 5, 4));

	}

}
