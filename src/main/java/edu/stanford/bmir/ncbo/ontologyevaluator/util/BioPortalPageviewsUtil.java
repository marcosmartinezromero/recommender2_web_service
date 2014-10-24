package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * Methods to read information about ontology pageviews in BioPortal
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 08/05/2014 15:18:01
 */
public class BioPortalPageviewsUtil {
	private static List<String> pageviewsRanking;

	// Static block to read the pageviews ranking file
	static {
		pageviewsRanking = new ArrayList<String>();
		String pvFile = PropertiesManager.getProperty("pageviewsRankingFile");
		try {
			File f = new File(pvFile);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				pageviewsRanking.add(line);
			}
			if (fr != null) {
				fr.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ontologyUri
	 *            (example: http://data.bioontology.org/ontologies/NCIT)
	 * @return The position of the ontology in the ranking or -1 in the ontology
	 *         was not found
	 */
	public static int getPosition(String ontologyUri) {
		String acronym = OntologyUtil.getOntologyAcronymFromUri(ontologyUri);
		int position = pageviewsRanking.indexOf(acronym);
		if (position != -1)
			position++;
		return position;
	}

	/**
	 * 
	 * @return Number of ontologies in the ranking
	 */
	public static int getRankingSize() {
		return pageviewsRanking.size();
	}

	/*** Test code ***/
	public static void main(String[] args) {
		System.out.println(BioPortalPageviewsUtil.getRankingSize());
		// System.out.println(BioPortalPageviewsUtil.getPosition("http://data.bioontology.org/ontologies/NCIT"));
	}

}
