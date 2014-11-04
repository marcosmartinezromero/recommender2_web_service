package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Source;
import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.ontologies.util.to.OntologyTO;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * Methods to obtain the number of mentions of ontology names in PubMed articles
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 08/05/2014 19:09:06
 */
public class PubMedUtil {

	// <Acronym, citations>
	private static Map<String, Integer> pubmedCitations;
	// <Acronym, ontology name>
	private static Map<String, String> ontologyNameForSearch;

	// Static block to read the data files
	static {
		// PubMed Citations
		pubmedCitations = new HashMap<String, Integer>();
		String pcFile = PropertiesManager.getProperty("pubmedCitationsFile");
		try {
			File pcf = new File(pcFile);
			FileReader fr = new FileReader(pcf);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\|");
				pubmedCitations.put(tokens[0], Integer.parseInt(tokens[2]));
			}
			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Ontology name for search
		ontologyNameForSearch = new HashMap<String, String>();
		String onFile = PropertiesManager
				.getProperty("ontologyNamesForSearchFile");
		try {
			File onf = new File(onFile);
			FileReader fr = new FileReader(onf);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\|");
				ontologyNameForSearch.put(tokens[0], tokens[1]);
			}
			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return Maximum number of citations in PubMed for all the ontologies in
	 *         BioPortal. This value is used for normalization purposes
	 */
	public static int getMaxPubMedCitations() {
		List<OntologyTO> ontologies = OntologyUtil.getAllOntologies();
		int maxCitations = 0;
		for (OntologyTO o : ontologies) {
			String ontologyUri = OntologyUtil.getOntologyUriForAcronym(o
					.getAcronym());
			int citations = getPubMedCitationsForOntology(ontologyUri);
			if (citations > maxCitations)
				maxCitations = citations;
		}
		return maxCitations;
	}

	/**
	 * @param ontologyUri
	 * @return Number of PubMed citations for the ontology, extracted from the
	 *         file with such information (pubmed_citations.txt). If the
	 *         ontology is not found in the file, it returns 0.
	 */
	public static int getPubMedCitationsForOntology(String ontologyUri) {
		String ontologyAcronym = OntologyUtil
				.getOntologyAcronymFromUri(ontologyUri);

		if (pubmedCitations.containsKey(ontologyAcronym))
			return pubmedCitations.get(ontologyAcronym);
		else
			return 0;
	}

	/**
	 * 
	 * @param text
	 *            Text to find in PubMed
	 * @return number of PubMed articles than contain the text. Help:
	 *         http://eutils
	 *         .ncbi.nlm.nih.gov/corehtml/query/static/esearch_help.html
	 */
	private static int getPubMedCitationsForText(String text) {
		int hits = 0;
		try {
			String url = "http://www.ncbi.nlm.nih.gov/entrez/"
					+ "eutils/esearch.fcgi?db=pubmed&term="
					+ URLEncoder.encode("\"" + text + "\"", "UTF-8")
					+ "[All fields]";

			Source source = new Source(new URL(url));
			hits = new Integer(source.getAllElements("count").get(0)
					.getContent().toString());
			System.out.println(hits);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hits;
	}

	/**
	 * Saves the PubMed citations for all the ontologies in BioPortal to a file.
	 * If the file exists, it is overwritten. This method should be used to
	 * update the citations file periodically
	 */
	public static void pubMedCitationsToFile() {
		String file = PropertiesManager.getProperty("pubmedCitationsFile");
		PrintWriter writer;
		try {
			List<OntologyTO> ontologies = OntologyUtil.getAllOntologies();
			writer = new PrintWriter(file, "UTF-8");
			for (OntologyTO ontology : ontologies) {
				System.out.println("Processing " + ontology.getAcronym());
				writer.print(ontology.getAcronym() + "|");
				String ontologyName = getOntologyNameForSearch(ontology);
				writer.print(ontologyName + "|");
				writer.println(getPubMedCitationsForText(ontologyName));
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ontologyAcronym
	 * @return The name of the ontology adequately changed (manually) for
	 *         searching. Example: "Physical Medicine and Rehabilitation" would
	 *         be changed to "Physical Medicine and Rehabilitation Ontology" to
	 *         avoid false positives. This method access to a file with the most
	 *         adequate names for each ontology that I reviewed and modified
	 *         manually (ontology_names_for_search.txt)
	 */
	private static String getOntologyNameForSearch(OntologyTO ontology) {
		String ontologyAcronym = ontology.getAcronym();
		if (ontologyNameForSearch.containsKey(ontologyAcronym))
			return ontologyNameForSearch.get(ontologyAcronym);
		else
			// If the name of the ontology was not found in the
			// ontology_names_for_search.txt file, the name of the ontology
			// provided by BioPortal is provided
			return ontology.getName();
	}

	/*** Test code ***/
	public static void main(String[] args) {
		PubMedUtil.pubMedCitationsToFile();

		// int citations = PubMedUtil
		// .getPubMedCitationsForOntology("http://bioportal.bioontology.org/ontologies/GO");
		// System.out.println(citations);
		//
		// System.out.println(PubMedUtil.getOntologyNameForSearch(new
		// OntologyTO("PMR",
		// "Physical Medicine and Rehabilitation")));
	}

}
