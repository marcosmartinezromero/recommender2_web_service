package edu.stanford.bmir.ncbo.ontologyevaluator;

import java.util.ArrayList;
import java.util.List;

import org.bioontology.ontologies.api.models.NCBOClass;
import org.bioontology.ontologies.api.models.NCBOOntology;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.bioportal.BioPortalUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.AcceptanceResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.CoverageResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.DetailResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalPageviewsUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalUmlsUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.PubMedUtil;
import edu.stanford.bmir.ncbo.recommender.util.NormUtil;

/**
 * Evaluator of the detail of the knowledge provided by the ontology for the
 * specific input
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version Aug 30, 2014
 */
public class DetailEvaluator {

	public static DetailResultTO evaluate(CoverageResultTO coverageResultTO) {
		double definitionMaxScore = 1;
		int definitionsForMaxScore = 1;
		double synonymsMaxScore = 1;
		int synonymsForMaxScore = 5;
		double propertiesMaxScore = 1;
		int propertiesForMaxScore = 7;

		double sumDef = 0, sumSyn = 0, sumProp = 0, sumFinal = 0;
		List<AnnotationTO> annotations = coverageResultTO.getAnnotations();
		for (AnnotationTO annotation : annotations) {
			double definitionScore = 0;
			double synonymsScore = 0;
			double propertiesScore = 0;
			String conceptUri = annotation.getConceptUri();
			NCBOClass c = BioPortalUtil.getClassById(conceptUri);
			if (c != null) {
				if (c.getDefinition() != null)
					definitionScore = getScoreForElement(c.getDefinition()
							.size(), definitionMaxScore, definitionsForMaxScore);
				if (c.getSynonym() != null)
					synonymsScore = getScoreForElement(c.getSynonym().size(),
							synonymsMaxScore, synonymsForMaxScore);
				if (c.getProperties() != null)
					propertiesScore = getScoreForElement(c.getProperties()
							.size(), propertiesMaxScore, propertiesForMaxScore);

				sumDef += definitionScore;
				sumSyn += synonymsScore;
				sumProp += propertiesScore;
				sumFinal += (definitionScore + synonymsScore + propertiesScore) / 3;

			}
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

	// public static void main(String[] args) {
	// System.out.println(getScoreForElement(4, 10, 5));
	// }

}
