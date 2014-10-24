package edu.stanford.bmir.ncbo.ontologies.util;

import java.util.List;

import edu.stanford.bmir.ncbo.ontologies.util.to.OntologyTO;
import edu.stanford.bmir.ncbo.recommender.util.CommonOperations;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Common ontology operations
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 05/05/2014 15:15:38
 */
public class OntologyUtil {
	private static String ontologyBaseUrl;
	private static String ontologyBaseBioPortalUrl;
	private static String apiKey;
	private static int maxCallsPerSecond;
	private static List<OntologyTO> ontologies = null;

	static {
		ontologyBaseUrl = PropertiesManager.getProperty("ontologyBaseUrl");
		ontologyBaseBioPortalUrl = PropertiesManager
				.getProperty("ontologyBaseBioPortalUrl");
		apiKey = PropertiesManager.getProperty("apiKey");
		maxCallsPerSecond = Integer.parseInt(PropertiesManager
				.getProperty("maxCallsPerSecond"));
	}

	/**
	 * @return A list of Transfer Objects with data about the ontologies in
	 *         BioPortal
	 */
	public static List<OntologyTO> getAllOntologies() {
		if (ontologies == null) {
			String url = ontologyBaseUrl + "?apikey=" + apiKey;
			String json = CommonOperations.callService(url, maxCallsPerSecond);
			ontologies = new Gson().fromJson(json,
					new TypeToken<List<OntologyTO>>() {
					}.getType());
		}
		return ontologies;
	}

	/**
	 * @param uri
	 * @return The ontology name (e.g. Medical Subject Headings) or null if the
	 *         ontologyUri has not been found
	 */
	public static String getOntologyNameFromUri(String uri) {
		List<OntologyTO> ontologies = getAllOntologies();
		for (OntologyTO o : ontologies) {
			if (uri.compareTo(ontologyBaseUrl + o.getAcronym()) == 0) {
				return o.getName();
			}
		}
		return null;
	}

	/**
	 * @param uri
	 * @return The ontology acronym (e.g. MESH)
	 */
	public static String getOntologyAcronymFromUri(String uri) {
		String[] uriSplit = uri.split("/");
		String acronym = uriSplit[uriSplit.length - 1];
		return acronym;
	}

	/**
	 * @param acronym
	 * @return The ontology uri (e.g. http://data.bioontology.org/ontologies/MESH)
	 */
	public static String getOntologyUriForAcronym(String acronym) {
		return (ontologyBaseUrl + acronym);
	}

	/**
	 * @param acronym
	 * @return The uri of the BioPortal webpage for the ontology (e.g.
	 *         http://bioportal.bioontology.org/ontologies/MESH)
	 */
	public static String getOntologyBioPortalUriForAcronym(String acronym) {
		return (ontologyBaseBioPortalUrl + acronym);
	}

	/*** Test code ***/
//	public static void main(String[] args) {
//
//		// System.out.println(OntologyOperations.getOntologyMetrics(OntologyUtils
//		// .getOntologyUriForAcronym("NCIT")).getClasses());
//		System.out.println(OntologyUtil.getAllOntologies());
//		System.out
//				.println(OntologyUtil
//						.getOntologyNameFromUri("http://data.bioontology.org/ontologies/RH-MESH"));
//	}

}
