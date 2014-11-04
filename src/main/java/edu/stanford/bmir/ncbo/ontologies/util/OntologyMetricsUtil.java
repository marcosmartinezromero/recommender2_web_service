package edu.stanford.bmir.ncbo.ontologies.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.bmir.ncbo.ontologies.util.to.OntologyMetricsTO;
import edu.stanford.bmir.ncbo.recommender.util.CommonOperations;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Operations to obtain different ontology metrics for BioPortal ontologies
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 02/06/2014 11:59:12
 */
public class OntologyMetricsUtil {

	private static String apiBaseUrl;
	private static String apiKey;
	private static int maxCallsPerSecond;
	private static HashMap<String, OntologyMetricsTO> metrics;

	static {
		apiBaseUrl = PropertiesManager.getProperty("apiBaseUrl");
		apiKey = PropertiesManager.getApikey();
		maxCallsPerSecond = Integer.parseInt(PropertiesManager
				.getProperty("maxCallsPerSecond"));
		// Metrics initialization
		metrics = getAllOntologyMetrics();
	}

	/**
	 * @return Ontology metrics for all the ontologies in BioPortal
	 */
	private static HashMap<String, OntologyMetricsTO> getAllOntologyMetrics() {
		HashMap<String, OntologyMetricsTO> metricsMap = new HashMap<String, OntologyMetricsTO>();
		String url = apiBaseUrl + "metrics" + "?apikey=" + apiKey;
		// Access to BioPortal API
		String json = CommonOperations.callService(url, maxCallsPerSecond);
		List<OntologyMetricsTO> allMetrics = new Gson().fromJson(json,
				new TypeToken<List<OntologyMetricsTO>>() {
				}.getType());

		for (OntologyMetricsTO om : allMetrics) {
			metricsMap.put(om.getLinks().getOntologyUri(), om);
		}

		return metricsMap;
	}

	/**
	 * @param ontologyUri
	 * @return The total number of classes of a given ontology. The name of the
	 *         ontology must match one of the acronyms used in BioPortal (e.g.
	 *         NCIT, MESH, GO, etc.)
	 */
	public static OntologyMetricsTO getOntologyMetrics(String ontologyUri) {
		if (metrics.containsKey(ontologyUri))
			return metrics.get(ontologyUri);
		else
			return null;
	}

	/**
	 * @return An ordered array with the number of classes for all the
	 *         ontologies
	 */
	public static double[] getOrderedNumberOfClassesAllOntologies() {
		double[] numberOfClasses = new double[metrics.size()];
		int i = 0;
		for (Map.Entry<String, OntologyMetricsTO> entry : metrics.entrySet()) {
			numberOfClasses[i] = entry.getValue().getClasses();
			i++;
		}
		// Sort array
		Arrays.sort(numberOfClasses);

		return numberOfClasses;
	}

}
