/**
 * 
 */
package edu.stanford.bmir.ncbo.bioportal;

import org.bioontology.ontologies.api.Client;
import org.bioontology.ontologies.api.http.HTTPOptions;
import org.bioontology.ontologies.api.models.NCBOClass;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 30/08/2014 17:17:50
 */
public class BioPortalUtil {

	private static Client bpServicesClient;

	static {
		// TODO: read from config file
		String apikey = "24e0ebf2-54e0-11e0-9d7b-005056aa3316";
		bpServicesClient = new Client(new HTTPOptions(apikey));
	}

	public static NCBOClass getClassById(String classId) {	
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		NCBOClass c = bpServicesClient.getByID(classId, NCBOClass.class);
		return c;
	}

	public static void main(String[] args) {
		String id = "http://bioportal.bioontology.org/ontologies/RCD?p=classes&conceptid=http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FRCD%2FX80Uw";
		NCBOClass c = bpServicesClient.getByID(id, NCBOClass.class);
		if (c!=null) {
			if (c.getSynonym()!=null)
				System.out.println(c.getSynonym().toString());
		}
	}
	
}
