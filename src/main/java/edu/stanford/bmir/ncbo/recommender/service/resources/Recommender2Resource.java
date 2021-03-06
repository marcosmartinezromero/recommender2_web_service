package edu.stanford.bmir.ncbo.recommender.service.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yammer.metrics.annotation.Timed;

import edu.stanford.bmir.ncbo.recommender.Recommender2Facade;
import edu.stanford.bmir.ncbo.recommender.result.RecommendationResultTO;
import edu.stanford.bmir.ncbo.recommender.service.core.Output;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * @author Marcos Mart�nez-Romero (marcosmartinez@udc.es)
 * @version 21/05/2014 20:23:24
 */
@Path("/recommender2")
@Produces(MediaType.APPLICATION_JSON)
public class Recommender2Resource {
	private int inputType;
	private int outputType;
	private int maxOntologiesInSet;
	private double wc;
	private double ws;
	private double wa;
	private double wd;

	public Recommender2Resource(int inputType, int outputType,
			int maxOntologiesInSet, double wc, double ws, double wa, double wd) {
		this.inputType = inputType;
		this.outputType = outputType;
		this.maxOntologiesInSet = maxOntologiesInSet;
		this.wc = wc;
		this.ws = ws;
		this.wa = wa;
		this.wd = wd;
	}

	@GET
	@Timed
	public Object getRecommendations(@QueryParam("callback") String callback,
			@QueryParam("text") String text,
			@QueryParam("inputType") Integer inputType,
			@QueryParam("outputType") Integer outputType,
			@QueryParam("maxOntologiesInSet") Integer maxOntologiesInSet,
			@QueryParam("wc") Double wc, @QueryParam("ws") Double ws,
			@QueryParam("wa") Double wa, @QueryParam("wd") Double wd,
			@Context HttpServletRequest request) throws IOException {

		Logger logger = LoggerFactory.getLogger("Recommender2Resource");
		logger.info("\n\n----------------------------- NEW REQUEST --------------------------------");
		logger.info("IP: " + request.getRemoteAddr());
		// If the client did not provide an input text a HTTP ERROR 400 is
		// returned
		if ((text == null) || (text.trim().length() == 0)) {
			logger.error("Bad request: no input text provided");
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		if (inputType == null)
			inputType = this.inputType;
		if (outputType == null)
			outputType = this.outputType;
		if ((maxOntologiesInSet == null) || (maxOntologiesInSet < 2)
				|| (maxOntologiesInSet > 4))
			maxOntologiesInSet = this.maxOntologiesInSet;

		logger.info("Input text:\n" + text);

		if (((wc == null) || (wc < 0)) || ((ws == null) || (ws < 0))
				|| ((wa == null) || (wa < 0)) || ((wd == null) || (wd < 0))) {
			wc = this.wc;
			wa = this.wa;
			wd = this.wd;
			ws = this.ws;
			logger.info("Some weights are null or lower than 0. Default values have been used");
		}

		logger.info("Input type: " + inputType);
		logger.info("Output type: " + outputType);
		logger.info("MaxOntologiesInSet: " + maxOntologiesInSet);
		logger.info("wc = " + wc);
		logger.info("wa = " + wa);
		logger.info("wd = " + wd);
		logger.info("ws = " + ws);

		// Coverage settings
		// double wCoverage = PropertiesManager.getPropertyDouble("wCoverage");
		double wCoverage = wc;
		double prefScore = PropertiesManager.getPropertyDouble("prefScore");
		double synScore = PropertiesManager.getPropertyDouble("synScore");
		double multiTermScore = PropertiesManager
				.getPropertyDouble("multiTermScore");

		// Specialization settings
		// double wSpecialization =
		// PropertiesManager.getPropertyDouble("wSpecialization");
		double wSpecialization = ws;

		// Acceptance settings
		// double wAcceptance =
		// PropertiesManager.getPropertyDouble("wAcceptance");
		double wAcceptance = wa;
		double wPageviews = PropertiesManager.getPropertyDouble("wPageviews");
		double wUmls = PropertiesManager.getPropertyDouble("wUmls");
		double wPubmed = PropertiesManager.getPropertyDouble("wPubmed");

		// Detail of Knowledge settings
		// double wDetail = PropertiesManager.getPropertyDouble("wDetail");
		double wDetail = wd;
		int definitionsForMaxScore = PropertiesManager
				.getPropertyInt("definitionsForMaxScore");
		int synonymsForMaxScore = PropertiesManager
				.getPropertyInt("synonymsForMaxScore");
		int propertiesForMaxScore = PropertiesManager
				.getPropertyInt("propertiesForMaxScore");

		// Ontologies to evaluate. If the list is empty, all the ontologies in
		// BioPortal will be evaluated
		List<String> ontologyUris = new ArrayList<String>();

		List<RecommendationResultTO> results = Recommender2Facade.getRanking(
				text, inputType, outputType, maxOntologiesInSet, ontologyUris,
				wCoverage, prefScore, synScore, multiTermScore,
				wSpecialization, wAcceptance, wPageviews, wUmls, wPubmed,
				wDetail, definitionsForMaxScore, synonymsForMaxScore,
				propertiesForMaxScore);
		if (results.size() > 0)
			logger.info("First result: " + results.get(0).getOntologyAcronyms());

		// If the data are requested from a server in the same domain name than
		// the Recommender service then it is not necessary to use JSONP (and
		// the callback parameter)
		if (callback != null)
			return new JSONPObject(callback, new Output(results));
		else
			return new Output(results);

	}
}
