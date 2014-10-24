package edu.stanford.bmir.ncbo.annotator.to;

/**
 * Transfer Object that represents an annotation between text and an ontology
 * concept. If a specific text is annotated with different ontology concepts,
 * each annotation will be represented as an independent instance of this class
 * 
 * @author Marcos Mart�nez-Romero (marcosmartinez@udc.es)
 * @version 13/05/2014 17:18:27
 */
public class AnnotationTO {
	// Specific position of the annotated text
	private int from;
	private int to;
	// Match type can be "PREF" or "SYN"
	private String matchType;
	// Annotated text
	private String text;
	private String ontologyUri;
	private String conceptUri;

	public AnnotationTO(int from, int to, String matchType, String text,
			String ontologyUri, String conceptUri) {
		super();
		this.from = from;
		this.to = to;
		this.matchType = matchType;
		this.text = text;
		this.ontologyUri = ontologyUri;
		this.conceptUri = conceptUri;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOntologyUri() {
		return ontologyUri;
	}

	public void setOntologyUri(String ontologyUri) {
		this.ontologyUri = ontologyUri;
	}

	public String getConceptUri() {
		return conceptUri;
	}

	public void setConceptUri(String conceptUri) {
		this.conceptUri = conceptUri;
	}

	@Override
	public String toString() {
		return "AnnotationTO [from=" + from + ", to=" + to + ", matchType="
				+ matchType + ", text=" + text + ", ontologyUri=" + ontologyUri
				+ ", conceptUri=" + conceptUri + "]";
	}

}
