package edu.stanford.bmir.ncbo.recommender.service;

import edu.stanford.bmir.ncbo.recommender.service.resources.Recommender2Resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.ObjectMapperFactory;

/**
 * Recommender web service
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 21/05/2014 20:21:39
 */
public class Recommender2Service extends Service<Recommender2Configuration> {
		public static void main(String[] args) throws Exception {
			new Recommender2Service().run(args);
		}

		@Override
		public void initialize(Bootstrap<Recommender2Configuration> bootstrap) {
			bootstrap.setName("recommender2");
		}

		@Override
		public void run(Recommender2Configuration configuration,
				Environment environment) {
			// Pretty printing
			ObjectMapperFactory factory = environment.getObjectMapperFactory();
		    factory.enable(SerializationFeature.INDENT_OUTPUT);
			environment.addResource(new Recommender2Resource(configuration.getInputType(),
					configuration.getOutputType(), configuration.getMaxElementsInCombination(),
					configuration.getWc(), configuration.getWs(), configuration.getWa(), configuration.getWd()));
		}
}
