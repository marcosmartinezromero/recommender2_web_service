package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * Methods read the list of BioPortal ontologies that are included into UMLS
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 08/05/2014 15:18:01
 */
public class BioPortalUmlsUtil {
	private static List<String> bpUmlsOntologies;

	// Static block to read the pageviews ranking file
	static {
		bpUmlsOntologies = new ArrayList<String>();
		String umlsFile = PropertiesManager.getProperty("bioportalUmlsFile");
		try {
			File f = new File(umlsFile);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				bpUmlsOntologies.add(line);
			}
			if (fr != null) {
				fr.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean includedUmls(String ontologyUri) {
		String ontologyAcronym = OntologyUtil
				.getOntologyAcronymFromUri(ontologyUri);

		if (bpUmlsOntologies.contains(ontologyAcronym))
			return true;
		else
			return false;

	}

	/*** Test code ***/
	public static void main(String[] args) {
		System.out.println(BioPortalUmlsUtil
				.includedUmls("http://data.bioontology.org/ontologies/PMA"));
		System.out.println(BioPortalUmlsUtil
				.includedUmls("http://data.bioontology.org/ontologies/GO"));
		System.out.println(BioPortalUmlsUtil
				.includedUmls("http://data.bioontology.org/ontologies/FMA"));
		System.out.println(BioPortalUmlsUtil
				.includedUmls("http://data.bioontology.org/ontologies/ABCD"));
	}

}
