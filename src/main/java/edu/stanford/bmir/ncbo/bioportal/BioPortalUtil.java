/**
 * 
 */
package edu.stanford.bmir.ncbo.bioportal;

import org.bioontology.ontologies.api.Client;
import org.bioontology.ontologies.api.http.HTTPOptions;
import org.bioontology.ontologies.api.models.NCBOClass;

import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 30/08/2014 17:17:50
 */
public class BioPortalUtil {

	private static Client bpServicesClient;

	static {		
		String apikey = PropertiesManager.getProperty("apiKey");
		bpServicesClient = new Client(new HTTPOptions(apikey));
	}

	public static NCBOClass getClassById(String classId) {
		NCBOClass c = bpServicesClient.getByID(classId, NCBOClass.class);
		return c;
	}

	// public static void main(String[] args) {
	// String id =
	// "http://bioportal.bioontology.org/ontologies/RCD?p=classes&conceptid=http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FRCD%2FX80Uw";
	// NCBOClass c = bpServicesClient.getByID(id, NCBOClass.class);
	// if (c!=null) {
	// if (c.getSynonym()!=null)
	// System.out.println(c.getSynonym().toString());
	// }
	// }

}
