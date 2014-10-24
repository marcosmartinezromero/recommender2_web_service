package edu.stanford.bmir.ncbo.ontologyevaluator;

import java.util.List;

import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.ontologies.util.to.OntologyTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.result.AcceptanceResultTO;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalPageviewsUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalUmlsUtil;
import edu.stanford.bmir.ncbo.ontologyevaluator.util.PubMedUtil;
import edu.stanford.bmir.ncbo.recommender.util.NormUtil;

/**
 * Ontology acceptance evaluator
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 08/05/2014 12:17:43
 */
public class AcceptanceEvaluator {

	/**
	 * @param ontologyUri
	 * @param wPageviews
	 *            Weight of the pageviews of the ontology in BioPortal
	 * @param wPubMed
	 *            Weight of the number of PubMed articles that mention the name
	 *            of the ontology
	 * @return An object that encapsulates information about the acceptance of
	 *         the ontology by the biomedical community
	 */
	public AcceptanceResultTO evaluate(String ontologyUri, double wPageviews, double wUmls,
			double wPubMed) {
		double pageviewsScore = getPageviewsScore(ontologyUri);
		double umlsScore = getUmlsScore(ontologyUri);
		double pubMedScore = getPubMedScore(ontologyUri);
		double score = (wPageviews * pageviewsScore + wUmls * umlsScore + 
				wPubMed * pubMedScore);
		AcceptanceResultTO result = new AcceptanceResultTO(score, pubMedScore,
				pageviewsScore, umlsScore);
		
		return result;
	}

	/**
	 * @param ontologyUri
	 * @return Normalized pageviews score (range [0,1])
	 */
	private double getPageviewsScore(String ontologyUri) {
		int positionInRanking = BioPortalPageviewsUtil.getPosition(ontologyUri);
		// If the ontology is not in the ranking the score will be 0
		if (positionInRanking == -1)
			return 0;
		int rankingSize = BioPortalPageviewsUtil.getRankingSize();
		// Formula = 1 - (position-1 / n-1)
		// Example: suppose n=100
		// 1st ontology = 1 - (1-1 / 100) = 1
		// 100 ontology = 1 - (100-1 / 100-1) = 0
		double pageviewsScore = (double) 1
				- ((double) (positionInRanking - 1) / (double) (rankingSize - 1));

		return pageviewsScore;
	}
	
	/**
	 * @param ontologyUri
	 * @return UMLS score (0 or 1)
	 */
	private double getUmlsScore(String ontologyUri) {
		if (BioPortalUmlsUtil.includedUmls(ontologyUri))
			return 1;
		else
			return 0;
	}


	/**
	 * @param ontologyUri
	 * @return Normalized PubMed score (range [0,1])
	 */
	private double getPubMedScore(String ontologyUri) {
		int citations = PubMedUtil.getPubMedCitationsForOntology(ontologyUri);
		if (citations == 0)
			return 0;
		double score = Math.log10(citations);
		// Score normalization
		int maxCitations = PubMedUtil.getMaxPubMedCitations();
		double maxScore = Math.log10(maxCitations);
		NormUtil normalizer = new NormUtil(maxScore, 0, 1, 0);
		double normalizedScore = normalizer.normalize(score);

		return normalizedScore;
	}

	/*** Test code ***/
	public static void main(String[] args) {
		AcceptanceEvaluator ev = new AcceptanceEvaluator();
		List<OntologyTO> ontologies = OntologyUtil.getAllOntologies();
		for (OntologyTO o : ontologies) {
			String oUri = "http://data.bioontology.org/ontologies/" + o.getAcronym();
			AcceptanceResultTO aTO = ev.evaluate(oUri, 0.4, 0.4, 0.2);
			//System.out.println(pm + "-" + pv + "-" + umls);
			System.out.println(o.getAcronym()+"|"+o.getName()+"|"+aTO.getScore());			
		}
		

	}

}
