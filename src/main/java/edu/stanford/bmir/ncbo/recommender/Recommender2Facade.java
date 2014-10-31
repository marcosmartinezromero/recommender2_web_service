package edu.stanford.bmir.ncbo.recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.bmir.ncbo.annotator.AnnotatorAdapter;
import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.annotator.util.AnnotatorUtil;
import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.AcceptanceEvaluator;
import edu.stanford.bmir.ncbo.ontologyevaluator.CoverageEvaluator;
import edu.stanford.bmir.ncbo.ontologyevaluator.DetailEvaluator;
import edu.stanford.bmir.ncbo.ontologyevaluator.SpecializationEvaluator;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.AcceptanceResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.DetailResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.SpecializationResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.scores.Aggregator;
import edu.stanford.bmir.ncbo.ontologyevaluator.scores.ScoreTO;
import edu.stanford.bmir.ncbo.recommender.result.RecommendationResultTO;
import edu.stanford.bmir.ncbo.recommender.util.RecommenderUtil;
import edu.stanford.bmir.ncbo.util.CombinatoricalOperations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Facade that provides the main interface to the Ontology Recommender
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 28/10/2013 11:46:28
 */
public class Recommender2Facade {

	public static final int INPUT_TYPE_TEXT = 1;
	public static final int INPUT_TYPE_KEYWORDS = 2;
	public static Logger logger = LoggerFactory.getLogger("Recommender2Facade");

	/**
	 * Main entry point to the Recommender. For a given input, it provides a
	 * list of ontologies (or ontology sets) recommended to annotate that input
	 * 
	 * @param input
	 *            Input text or keywords
	 * @param inputType
	 *            Text or keywords
	 * @param outputType
	 *            Single ontology or sets ("combinations") of ontologies
	 * @param maxElementsInCombination
	 *            Maximum number of ontologies in each set
	 * @param ontologyUris
	 *            Ontology uris that will be evaluated. If this parameter is
	 *            null, all BioPortal ontologies will be evaluated
	 * @param wc
	 *            Weight assigned to the coverage score
	 * @param prefScore
	 *            Score assigned to "PREF" annotations (done with a concept
	 *            preferred name)
	 * @param synScore
	 *            Score assigned to "SYN" annotations (done with a concept
	 *            synonym)
	 * @param multiTermScore
	 *            Score assigned to annotations in which a concept annotates a
	 *            term composed by several words (e.g. white blood cell)
	 * @param wr
	 *            Weight assigned to the richness score
	 * @param ws
	 *            Weight assigned to the specialization score
	 * @param wa
	 *            Weight assigned to the acceptance score
	 * @param waPv
	 *            Weight assigned to the pageviews score
	 * @param waPm
	 *            Weight assigned to the PubMed score
	 * @return Ranked list of ontologies or ontology sets
	 */
	public static List<RecommendationResultTO> getRanking(String input,
			int inputType, int outputType, int maxElementsInCombination,
			List<String> ontologyUris, double wc, double prefScore,
			double synScore, double multiTermScore, double ws, double wa,
			double waPv, double waUmls, double waPm, double wd,
			int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {

		// Single output
		if (outputType == 1) {
			return getSingleRanking(input, inputType, ontologyUris, wc,
					prefScore, synScore, multiTermScore, ws, wa, waPv, waUmls,
					waPm, wd, definitionsForMaxScore, synonymsForMaxScore,
					propertiesForMaxScore);
		}
		// Combined output
		else {
			return getCombinedRanking(input, inputType,
					maxElementsInCombination, ontologyUris, wc, prefScore,
					synScore, multiTermScore, ws, wa, waPv, waUmls, waPm, wd,
					definitionsForMaxScore, synonymsForMaxScore,
					propertiesForMaxScore);
		}
	}

	/**
	 * @return Recommendations in JSON format
	 */
	public static String getRankingJson(String input, int inputType,
			int outputType, int maxElementsInCombination,
			List<String> ontologyUris, double wc, double prefScore,
			double synScore, double multiTermScore, double ws, double wa,
			double waPv, double waUmls, double waPm, double wd,
			int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {
		// Single output
		if (outputType == 1) {

			return getSingleRankingJson(input, inputType, ontologyUris, wc,
					prefScore, synScore, multiTermScore, ws, wa, waPv, waUmls,
					waPm, wd, definitionsForMaxScore, synonymsForMaxScore,
					propertiesForMaxScore);
		}
		// Ontology sets output
		else {
			return getCombinedRankingJson(input, inputType,
					maxElementsInCombination, ontologyUris, wc, prefScore,
					synScore, multiTermScore, ws, wa, waPv, waUmls, waPm, wd,
					definitionsForMaxScore, synonymsForMaxScore,
					propertiesForMaxScore);
		}
	}

	/**
	 * @return Single ontology ranking. Each position in the ranking is an
	 *         ontology recommended to annotate the input provided
	 */
	private static List<RecommendationResultTO> getSingleRanking(String input,
			int inputType, List<String> ontologyUris, double wc,
			double prefScore, double synScore, double multiTermScore,
			double ws, double wa, double waPv, double waUmls, double waPm,
			double wd, int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {
		Logger logger = LoggerFactory.getLogger("Recommender2Facade");
		CoverageEvaluator coverageEvaluator = new CoverageEvaluator();
		AcceptanceEvaluator acceptanceEvaluator = new AcceptanceEvaluator();
		List<RecommendationResultTO> results = new ArrayList<RecommendationResultTO>();

		HashMap<String, List<AnnotationTO>> annotations = AnnotatorAdapter
				.getAnnotations(input, inputType, ontologyUris);
		// Only the ontologies that have annotations will be evaluated
		List<String> selectedUris = Collections.list(Collections
				.enumeration(annotations.keySet()));

		for (String ontologyUri : selectedUris) {
			List<ScoreTO> scores = new ArrayList<ScoreTO>();

			CoverageResultTO coverageResult = null;
			SpecializationResultTO specializationResult = null;
			AcceptanceResultTO acceptanceResult = null;
			DetailResultTO detailResult = null;

			// if (wc > 0) {
			/*** Coverage evaluation ***/
			coverageResult = coverageEvaluator.evaluate(input, inputType,
					annotations.get(ontologyUri), prefScore, synScore,
					multiTermScore);
			scores.add(new ScoreTO(coverageResult.getNormalizedScore(), wc));
			// }

			// if (ws > 0) {
			/*** Specialization evaluation ***/
			specializationResult = SpecializationEvaluator.evaluate(input,
					inputType, ontologyUri);
			scores.add(new ScoreTO(specializationResult.getNormalizedScore(),
					ws));
			// }

			// if (wa > 0) {
			/*** Acceptance evaluation ***/
			acceptanceResult = acceptanceEvaluator.evaluate(ontologyUri, waPv,
					waUmls, waPm);
			scores.add(new ScoreTO(acceptanceResult.getScore(), wa));
			// }

			// if (wd > 0) {
			/*** Knowledge detail evaluation ***/
			detailResult = DetailEvaluator.evaluate(coverageResult,
					definitionsForMaxScore, synonymsForMaxScore,
					propertiesForMaxScore);
			scores.add(new ScoreTO(detailResult.getScore(), wd));
			// }

			/*** Final result ***/
			// System.out.println(scores);
			double aggregatedScore = Aggregator.aggregateScores(scores);

			List<String> ontUris = new ArrayList<String>();
			ontUris.add(ontologyUri);
			List<String> ontologyBioPortalUris = new ArrayList<String>();
			ontologyBioPortalUris.add(OntologyUtil
					.getOntologyBioPortalUriForAcronym(OntologyUtil
							.getOntologyAcronymFromUri(ontologyUri)));
			List<String> ontologyNames = new ArrayList<String>();
			ontologyNames.add(OntologyUtil.getOntologyNameFromUri(ontologyUri));

			List<String> ontologyAcronyms = new ArrayList<String>();
			ontologyAcronyms.add(OntologyUtil
					.getOntologyAcronymFromUri(ontologyUri));

			RecommendationResultTO result = new RecommendationResultTO(ontUris,
					ontologyBioPortalUris, ontologyNames, ontologyAcronyms,
					aggregatedScore, coverageResult, specializationResult,
					acceptanceResult, detailResult);
			results.add(result);
		}
		// The results are ranked according to the final score for each
		// ontology, from the highest to the lowest score
		Collections.sort(results);

		return results;
	}

	/**
	 * @return Single ontology ranking in JSON format
	 */
	private static String getSingleRankingJson(String input, int inputType,
			List<String> ontologyUris, double wc, double prefScore,
			double synScore, double multiTermScore, double ws, double wa,
			double waPv, double waUmls, double waPm, double wd,
			int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<RecommendationResultTO> ranking = getSingleRanking(input,
				inputType, ontologyUris, wc, prefScore, synScore,
				multiTermScore, ws, wa, waPv, waUmls, waPm, wd,
				definitionsForMaxScore, synonymsForMaxScore,
				propertiesForMaxScore);
		// Converts the Java object to JSON format and stores it as a JSON
		// formatted string
		String json = gson.toJson(ranking);
		return json;
	}

	/**
	 * @return Combined ontology ranking. Each position in the ranking is a set
	 *         from 1 to "maxElementsInCombination" ontologies that are
	 *         recommended to annotate the input provided
	 */
	private static List<RecommendationResultTO> getCombinedRanking(
			String input, int inputType, int maxElementsInCombination,
			List<String> ontologyUris, double wc, double prefScore,
			double synScore, double multiTermScore, double ws, double wa,
			double waPv, double waUmls, double waPm, double wd,
			int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {
		List<RecommendationResultTO> results = new ArrayList<RecommendationResultTO>();
		CoverageEvaluator coverageEvaluator = new CoverageEvaluator();

		List<RecommendationResultTO> singleRanking = getSingleRanking(input,
				inputType, ontologyUris, wc, prefScore, synScore,
				multiTermScore, ws, wa, waPv, waUmls, waPm, wd,
				definitionsForMaxScore, synonymsForMaxScore,
				propertiesForMaxScore);

		// Store the single results in a map<uri,result> to access them easily
		HashMap<String, RecommendationResultTO> singleResults = new HashMap<String, RecommendationResultTO>();
		for (RecommendationResultTO result : singleRanking) {
			singleResults.put(result.getOntologyUris().get(0), result);
		}

		logger.info("Selected ontologies: " + singleRanking.size());

		HashMap<String, List<AnnotationTO>> annotations = AnnotatorAdapter
				.getAnnotations(input, inputType, ontologyUris);

		// In order to improve performance, only a subset of the ontologies are
		// selected to enter the evaluation stage of ontology combinations
		List<String> selectedUrisForCombinedRanking = RecommenderUtil
				.selectUrisForCombinedRanking(annotations, prefScore, synScore,
						multiTermScore, singleResults, wd, ws, wa);

		logger.info("Filtered ontologies: "
				+ selectedUrisForCombinedRanking.size());

		// Calculates all the combinations of ontologyUris without repetition
		ArrayList<ArrayList<String>> combinedUris = CombinatoricalOperations
				.getCombinations(selectedUrisForCombinedRanking,
						maxElementsInCombination);
		logger.info("Combinations: " + combinedUris.size());

		AnnotatorUtil util = new AnnotatorUtil();

		// Combinations filtering. If the potential max sum of coverage is lower
		// than the coverage provided by the first ontology in the single
		// ranking, the combination is directly discarded.
		ArrayList<ArrayList<String>> combinedUris2 = new ArrayList<ArrayList<String>>();
		for (List<String> uris : combinedUris) {
			double maxCoverage = 0;
			for (String uri : uris) {
				maxCoverage += singleResults.get(uri).getCoverageResult()
						.getNormalizedScore();
			}
			if (maxCoverage >= singleRanking.get(0).getCoverageResult()
					.getNormalizedScore()) {
				combinedUris2.add((ArrayList<String>) uris);
			}
		}
		combinedUris = combinedUris2;
		logger.info("Filtered combs.: " + combinedUris.size());

		// Evaluation of each ontology set
		long totalTimeCov = 0;
		long totalTimeSpec = 0;
		long totalTimeAcc = 0;
		long startEvaluationTime = System.currentTimeMillis();
		for (List<String> uris : combinedUris) {
			/*** Combined Coverage evaluation ***/
			long startTimeCov = System.currentTimeMillis();
			// All the annotations are put together
			List<AnnotationTO> annotationsForCombination = new ArrayList<AnnotationTO>();
			// For each uri in each group
			for (String uri : uris) {
				annotationsForCombination.addAll(annotations.get(uri));
			}
			// The coverage evaluation is made for all the annotations together
			CoverageResultTO combinedCoverageResult = coverageEvaluator
					.evaluate(input, inputType, annotationsForCombination,
							prefScore, synScore, multiTermScore);
			// Calculates the "contribution" made by each ontology to the
			// total coverage score. This contribution will be used to calculate
			// the rest of the scores proportionally
			double totalScore = combinedCoverageResult.getScore();
			HashMap<String, Double> individualCoverageScores = new HashMap<String, Double>();
			for (String uri : uris) {
				double individualScore = 0;
				for (AnnotationTO annotation : combinedCoverageResult
						.getAnnotations()) {
					if (annotation.getOntologyUri().compareTo(uri) == 0) {
						individualScore += util.getAnnotationScore(annotation,
								prefScore, synScore, multiTermScore);
					}
					individualCoverageScores.put(uri, individualScore);
				}
			}
			long timeCov = System.currentTimeMillis() - startTimeCov;
			totalTimeCov += timeCov;

			/*** Combined Specialization evaluation ***/
			long startTimeSpec = System.currentTimeMillis();
			double combinedSpecScore = 0;
			double combinedNormSpecScore = 0;
			for (String uri : uris) {
				SpecializationResultTO specializationResult = singleResults
						.get(uri).getSpecializationResult();
				combinedSpecScore += (individualCoverageScores.get(uri) / totalScore)
						* specializationResult.getScore();
				combinedNormSpecScore += (individualCoverageScores.get(uri) / totalScore)
						* specializationResult.getNormalizedScore();
			}
			SpecializationResultTO combinedSpecializationResult = new SpecializationResultTO(
					combinedSpecScore, combinedNormSpecScore);
			long timeSpec = System.currentTimeMillis() - startTimeSpec;
			totalTimeSpec += timeSpec;

			/*** Combined Acceptance evaluation ***/
			long startTimeAcc = System.currentTimeMillis();
			double combinedAccepScore = 0;
			double combinedPubMedScore = 0;
			double combinedBioportalPageviewsScore = 0;
			double combinedUmlsScore = 0;
			for (String uri : uris) {
				AcceptanceResultTO acceptanceResult = singleResults.get(uri)
						.getAcceptanceResult();
				combinedAccepScore += (individualCoverageScores.get(uri) / totalScore)
						* acceptanceResult.getScore();
				combinedPubMedScore += (individualCoverageScores.get(uri) / totalScore)
						* acceptanceResult.getPubMedScore();
				combinedBioportalPageviewsScore += (individualCoverageScores
						.get(uri) / totalScore)
						* acceptanceResult.getBioportalPageviewsScore();
				combinedUmlsScore += (individualCoverageScores.get(uri) / totalScore)
						* acceptanceResult.getUmlsScore();
			}
			AcceptanceResultTO combinedAcceptanceResult = new AcceptanceResultTO(
					combinedAccepScore, combinedPubMedScore,
					combinedBioportalPageviewsScore, combinedUmlsScore);
			long timeAcc = System.currentTimeMillis() - startTimeAcc;
			totalTimeAcc += timeAcc;

			// System.out.println("*** Execution times for iteration ***");
			// System.out.println("Coverage evaluation: " + timeCov + " ms");
			// System.out.println("Specialization evaluation: " + timeSpec +
			// " ms");
			// System.out.println("Acceptance evaluation: " + timeAcc + " ms");

			/*** Combined Detail of Knowledge evaluation ***/
			double combinedDetailScore = 0;
			double combinedDefinitionsScore = 0;
			double combinedSynonymsScore = 0;
			double combinedPropertiesScore = 0;
			for (String uri : uris) {
				DetailResultTO detailResult = singleResults.get(uri)
						.getDetailResult();
				combinedDetailScore += (individualCoverageScores.get(uri) / totalScore)
						* detailResult.getScore();
				combinedDefinitionsScore += (individualCoverageScores.get(uri) / totalScore)
						* detailResult.getDefinitionsScore();
				combinedSynonymsScore += (individualCoverageScores.get(uri) / totalScore)
						* detailResult.getSynonymsScore();
				combinedPropertiesScore += (individualCoverageScores.get(uri) / totalScore)
						* detailResult.getPropertiesScore();
			}
			DetailResultTO combinedDetailResult = new DetailResultTO(
					combinedDetailScore, combinedDefinitionsScore,
					combinedSynonymsScore, combinedPropertiesScore);

			List<String> ontologyUrisBioPortal = new ArrayList<String>();
			List<String> ontologyNames = new ArrayList<String>();
			List<String> ontologyAcronyms = new ArrayList<String>();

			// We put all ontologyUrisBioPortal and the ontologyNames together
			for (String uri : uris) {
				ontologyUrisBioPortal.add(OntologyUtil
						.getOntologyBioPortalUriForAcronym(OntologyUtil
								.getOntologyAcronymFromUri(uri)));
				ontologyNames.add(OntologyUtil.getOntologyNameFromUri(uri));
				ontologyAcronyms.add(OntologyUtil
						.getOntologyAcronymFromUri(uri));
			}

			/*** Final result ***/
			List<ScoreTO> scores = new ArrayList<ScoreTO>();
			scores.add(new ScoreTO(combinedCoverageResult.getNormalizedScore(), wc));
			scores.add(new ScoreTO(combinedSpecializationResult
					.getNormalizedScore(), ws));
			scores.add(new ScoreTO(combinedAcceptanceResult.getScore(), wa));
			scores.add(new ScoreTO(combinedDetailResult.getScore(), wd));
			double aggregatedScore = Aggregator.aggregateScores(scores);

			RecommendationResultTO result = new RecommendationResultTO(uris,
					ontologyUrisBioPortal, ontologyNames, ontologyAcronyms,
					aggregatedScore, combinedCoverageResult,
					combinedSpecializationResult, combinedAcceptanceResult,
					combinedDetailResult);

			results.add(result);

		}
		if (combinedUris.size() > 0) {
			long evaluationTime = System.currentTimeMillis()
					- startEvaluationTime;
			long avgTimeCov = totalTimeCov / combinedUris.size();
			long avgTimeSpec = totalTimeSpec / combinedUris.size();
			long avgTimeAcc = totalTimeAcc / combinedUris.size();

			logger.info("*** Evaluation times ***");
			logger.info("Evaluation time: " + evaluationTime + "ms");
			logger.info("Avg coverage: " + avgTimeCov + " ms");
			logger.info("Avg specialization: " + avgTimeSpec + " ms");
			logger.info("Avg acceptance: " + avgTimeAcc + " ms");
		}
		Collections.sort(results);
		// Only keep a specific number of results
		int maxResults = 25;
		if (results.size() > maxResults)
			results = results.subList(0, maxResults);
		return results;
	}

	/**
	 * @return Combined ontology ranking in JSON format
	 */
	private static String getCombinedRankingJson(String input, int inputType,
			int maxElementsInCombination, List<String> ontologyUris, double wc,
			double prefScore, double synScore, double multiTermScore,
			double ws, double wa, double waPv, double waUmls, double waPm,
			double wd, int definitionsForMaxScore, int synonymsForMaxScore,
			int propertiesForMaxScore) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<RecommendationResultTO> ranking = getCombinedRanking(input,
				inputType, maxElementsInCombination, ontologyUris, wc,
				prefScore, synScore, multiTermScore, ws, wa, waPv, waUmls,
				waPm, wd, definitionsForMaxScore, synonymsForMaxScore,
				propertiesForMaxScore);

		// Converts the Java object to JSON format and stores it as a JSON
		// formatted string
		String json = gson.toJson(ranking);
		return json;

	}
}
