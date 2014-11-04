package edu.stanford.bmir.ncbo.annotator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;
import edu.stanford.bmir.ncbo.ontologies.util.OntologyUtil;
import edu.stanford.bmir.ncbo.recommender.Recommender2Facade;
import edu.stanford.bmir.ncbo.recommender.util.CommonOperations;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * Adapter to the NCBO Annotator.
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 21/06/2014 11:51:12
 */
public class AnnotatorAdapter {

	private static String apiKey;
	private static int maxCallsPerSecond;
	public static final String annotatorUrl = "http://data.bioontology.org/annotator";
	public static String storedInput = "";
	public static int storedInputType = -1;
	public static HashMap<String, List<AnnotationTO>> annotations = null;

	static {
		apiKey = PropertiesManager.getApikey();
		maxCallsPerSecond = Integer.parseInt(PropertiesManager
				.getProperty("maxCallsPerSecond"));
	}

	/**
	 * Obtains the annotations for a given input text. For a better performance,
	 * the annotations are stored into a static variable and the NCBO Annotator
	 * service is invoked only when it is necessary
	 * 
	 * @param input
	 * @param inputType
	 * @param ontologyUris
	 * @return The ontology uris of the ontologies that annotate the input and
	 *         their corresponding annotations. If ontologyUris is null, the
	 *         annotations for all the ontologies in BioPortal are returned
	 */
	public static HashMap<String, List<AnnotationTO>> getAnnotations(
			String input, int inputType, List<String> ontologyUris) {
		// NOTE: including the option
		// "include=prefLabel,synonym,definition,properties" is necessary to
		// perform the evaluation of detail of the ontology concepts but makes
		// the call to the Annotator service considerably slower
		String options = "&max_level=0&include_synonyms=true&include=prefLabel,synonym,definition,properties";
		// If it is the first call to the service, or if it is a new call (and
		// the input text has changed or the input type has changed) the
		// annotations are stored into a static variable to improve performance
		if ((input.compareTo(storedInput) != 0)
				|| (inputType != storedInputType)) {
			annotations = getAnnotationsFromService(input, null, options);
			// If the input are keywords separated instead of plain text, it is
			// necessary to filter the annotations obtained to keep just those
			// of them that annotate whole keywords
			if (inputType == Recommender2Facade.INPUT_TYPE_KEYWORDS) {
				keepOnlyAnnotationsForKeywords(input, ',');
			}
			storedInput = input;
			storedInputType = inputType;
		}
		// Return annotations for a specific list of ontologies
		if ((ontologyUris != null) && (ontologyUris.size() > 0)) {
			HashMap<String, List<AnnotationTO>> specificAnnotations = new HashMap<String, List<AnnotationTO>>();
			for (String uri : ontologyUris) {
				specificAnnotations.put(uri, annotations.get(uri));
			}
			return specificAnnotations;
		}
		// Return annotations for all the ontologies in BioPortal
		else {
			return annotations;
		}
	}

	/**
	 * Iterates through all the annotations (stored in a static variable) to
	 * remove those of them that do not annotate whole keywords
	 * 
	 * @param input
	 *            Input keywords
	 * @param separator
	 *            Character used to separate keywords
	 */
	private static void keepOnlyAnnotationsForKeywords(String input,
			char separator) {
		List<String> keywordsPositions = getKeywordPositions(input, separator);
		HashMap<String, List<AnnotationTO>> newAnnotationsMap = new HashMap<String, List<AnnotationTO>>();

		for (Map.Entry<String, List<AnnotationTO>> entry : annotations
				.entrySet()) {
			List<AnnotationTO> currentAnnotations = entry.getValue();
			List<AnnotationTO> newAnnotations = new ArrayList<AnnotationTO>();
			for (AnnotationTO annotation : currentAnnotations) {
				String position = annotation.getFrom() + "-"
						+ annotation.getTo();
				if (keywordsPositions.contains(position)) {
					newAnnotations.add(annotation);
				}
			}
			if (newAnnotations.size() > 0) {
				newAnnotationsMap.put(entry.getKey(), newAnnotations);
			}
		}
		annotations = newAnnotationsMap;
	}

	/**
	 * Obtains the position of all the input keywords
	 * 
	 * @param keywords
	 * @param separator
	 * @return A list of keyword positions. Each String in the list represents a
	 *         start-end position of each input keyword (example: 1-13).
	 */
	private static List<String> getKeywordPositions(String keywords,
			char separator) {
		ArrayList<String> positions = new ArrayList<String>();
		String position = "";
		int i = 0;
		int startPosition = 0;
		int endPosition = 0;
		boolean searchingForStart = true;

		while (i < keywords.length()) {
			char c = keywords.charAt(i);
			// Chars to ignore
			if ((c == ' ') || (c == '\n') || (c == '\t')) {
				// Do nothing
			}
			// A separator has been found
			else if (c == separator) {
				if (searchingForStart == true) {
					// Do nothing
				}
				// It was searching for a separator ending a keyword and it was
				// found
				else {
					position = position + "-" + (Integer.toString(endPosition));
					positions.add(position);
					searchingForStart = true;
				}
			}
			// A word character has been found
			else if (searchingForStart == true) {
				startPosition = i + 1;
				searchingForStart = false;
				position = Integer.toString(startPosition);
			}
			// It is searching for an end
			else {
				endPosition = i + 1;
			}
			i++;
			// End of input
			if ((i == keywords.length()) && (searchingForStart == false)) {
				position = position + "-" + (i);
				positions.add(position);
				searchingForStart = true;
			}
		}
		return positions;
	}

	/**
	 * Invokes the NCBO Annotator service to obtain the annotations for the
	 * given input text
	 * 
	 * @param text
	 * @param ontologyUris
	 * @return A list of ontology uris with their corresponding annotations
	 */
	private static HashMap<String, List<AnnotationTO>> getAnnotationsFromService(
			String text, List<String> ontologyUris, String options) {
		String url = "";
		String acronyms = "";
		if ((ontologyUris != null) && (ontologyUris.size() > 0)) {
			acronyms = "&ontologies=";
			for (String uri : ontologyUris) {
				acronyms += OntologyUtil.getOntologyAcronymFromUri(uri) + ",";
			}
			acronyms = acronyms.substring(0, acronyms.length() - 1);
		}

		try {
			url = annotatorUrl + "?apikey=" + apiKey + options + "&text="
					+ URLEncoder.encode(text, "utf-8") + acronyms;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String json = CommonOperations.callService(url, maxCallsPerSecond);
		Type collectionType = new TypeToken<List<AnnotatorData>>() {
		}.getType();
		List<AnnotatorData> annotatorData = new Gson().fromJson(json,
				collectionType);
		HashMap<String, List<AnnotationTO>> annotationsForOntologies = new HashMap<String, List<AnnotationTO>>();
		for (AnnotatorData data : annotatorData) {			
			String ontologyUri = data.getAnnotatedClass().getLinks()
					.getOntology();
			List<AnnotationTO> annotations = null;
			// If the ontologyUri is not in the HashMap yet
			if (annotationsForOntologies.get(ontologyUri) == null) {
				annotations = new ArrayList<AnnotationTO>();
			}
			// If the ontologyUri is in the HashMap already
			else {
				annotations = annotationsForOntologies.get(ontologyUri);
			}
			String conceptUri = data.getAnnotatedClass().getLinks().getUi();
			int numberOfDefinitions = data.getAnnotatedClass().getDefinitions()!=null ? data.getAnnotatedClass().getDefinitions().size() : 0;
			int numberOfSynonyms = data.getAnnotatedClass().getSynonyms()!=null ? data.getAnnotatedClass().getSynonyms().size() : 0;
			int numberOfProperties = data.getAnnotatedClass().getProperties()!=null ? data.getAnnotatedClass().getProperties().size() : 0;

			// The annotations are stored
			for (int i = 0; i < data.getAnnotations().size(); i++) {				
				AnnotationTO annotation = new AnnotationTO(
						Integer.parseInt(data.getAnnotations().get(i).getFrom()),
						Integer.parseInt(data.getAnnotations().get(i).getTo()),
						data.getAnnotations().get(i).getMatchType(), data
								.getAnnotations().get(i).getText(),
						ontologyUri, conceptUri,
						numberOfDefinitions, numberOfSynonyms, numberOfProperties);
				annotations.add(annotation);
			}
			annotationsForOntologies.put(ontologyUri, annotations);
		}

		return annotationsForOntologies;
	}

	/*** ANNOTATOR OUTPUT DATA MODEL ***/
	class AnnotatorData {
		@SerializedName("annotatedClass")
		private AnnotatedClass annotatedClass;
		@SerializedName("annotations")
		private List<Annotation> annotations;

		public AnnotatedClass getAnnotatedClass() {
			return annotatedClass;
		}

		public void setAnnotatedClass(AnnotatedClass annotatedClass) {
			this.annotatedClass = annotatedClass;
		}

		public List<Annotation> getAnnotations() {
			return annotations;
		}

		public void setAnnotations(List<Annotation> annotations) {
			this.annotations = annotations;
		}

	}

	class AnnotatedClass {
		@SerializedName("synonym")
		private List<String> synonyms;

		@SerializedName("definition")
		private List<String> definitions;

		@SerializedName("properties")
		private HashMap<String, List<String>> properties;

		@SerializedName("links")
		private Links links;

		public List<String> getSynonyms() {
			return synonyms;
		}

		public void setSynonyms(List<String> synonyms) {
			this.synonyms = synonyms;
		}

		public List<String> getDefinitions() {
			return definitions;
		}

		public void setDefinitions(List<String> definitions) {
			this.definitions = definitions;
		}

		public HashMap<String, List<String>> getProperties() {
			return properties;
		}

		public void setProperties(HashMap<String, List<String>> properties) {
			this.properties = properties;
		}

		public Links getLinks() {
			return links;
		}

		public void setLinks(Links links) {
			this.links = links;
		}


	}

	class Links {
		@SerializedName("ontology")
		private String ontology;

		@SerializedName("ui")
		private String ui;

		@SerializedName("self")
		private String self;

		public String getOntology() {
			return ontology;
		}

		public void setOntology(String ontology) {
			this.ontology = ontology;
		}

		public String getUi() {
			return ui;
		}

		public String getSelf() {
			return self;
		}

	}

	class Annotation {
		@SerializedName("from")
		private String from;

		@SerializedName("to")
		private String to;

		@SerializedName("matchType")
		private String matchType;

		@SerializedName("text")
		private String text;

		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}

		public String getMatchType() {
			return matchType;
		}

		public String getText() {
			return text;
		}

	}

	public static void main(String args[]) {
		HashMap<String, List<AnnotationTO>> annotations = AnnotatorAdapter
				.getAnnotations("melanoma", 1, null);
		for (Map.Entry<String, List<AnnotationTO>> entry : annotations
				.entrySet()) {
			System.out.println(entry.getValue());
		}

	}

}
