package edu.stanford.bmir.ncbo.ontologyevaluator;

import java.util.List;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.DetailResultTO;

/**
 * Evaluator of the detail of the knowledge provided by the ontology for the
 * specific input
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version Aug 30, 2014
 */
public class DetailEvaluator {

	public static DetailResultTO evaluate(CoverageResultTO coverageResultTO, int definitionsForMaxScore,
			int synonymsForMaxScore, int propertiesForMaxScore) {
		double definitionMaxScore = 1;
		double synonymsMaxScore = 1;
		double propertiesMaxScore = 1;
	
		double sumDef = 0, sumSyn = 0, sumProp = 0, sumFinal = 0;
		List<AnnotationTO> annotations = coverageResultTO.getAnnotations();
		for (AnnotationTO annotation : annotations) {
			double definitionScore = 0;
			double synonymsScore = 0;
			double propertiesScore = 0;

			definitionScore = getScoreForElement(
					annotation.getNumberOfDefinitions(), definitionMaxScore,
					definitionsForMaxScore);

			synonymsScore = getScoreForElement(
					annotation.getNumberOfSynonyms(), synonymsMaxScore,
					synonymsForMaxScore);

			propertiesScore = getScoreForElement(
					annotation.getNumberOfProperties(), propertiesMaxScore,
					propertiesForMaxScore);

			sumDef += definitionScore;
			sumSyn += synonymsScore;
			sumProp += propertiesScore;
			sumFinal += (definitionScore + synonymsScore + propertiesScore) / 3;

		}
		int size = annotations.size();
		DetailResultTO detailResult = new DetailResultTO(sumFinal / size,
				sumDef / size, sumSyn / size, sumProp / size);
		return detailResult;

	}

	private static double getScoreForElement(int size, double maxScore,
			int elementsForMaxScore) {
		if (size >= elementsForMaxScore)
			return maxScore;
		else
			return (double) size / elementsForMaxScore;
	}

//	public static void main(String[] args) {
//		System.out.println(getScoreForElement(4, 10, 5));
//	}

}
