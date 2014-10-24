package edu.stanford.bmir.ncbo.ontologies.util.to;

/**
 * Transfer Object to encapsulate basic information about an ontology
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 09/05/2014 13:03:05
 */
public class OntologyTO {
	private String acronym;
	private String name;

	public OntologyTO(String acronym, String name) {
		super();
		this.acronym = acronym;
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "OntologyTO [acronym=" + acronym + ", name=" + name + "]";
	}

}
