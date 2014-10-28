#The Ontology Recommender service
Due to the increasing amount, complexity and variety of existing biomedical ontologies, choosing those to be used in a semantic annotation problem or to design a specific application is cumbersome and time-consuming.

The Ontology Recommender service suggests the best biomedical ontologies for a specific annotation problem. Given an input text or a set of keywords, the Recommender evaluates all [BioPortal ontologies](http://bioportal.bioontology.org/ontologies) according to several evaluation criteria and provides a ranked list of ontologies (or sets of ontologies) in JSON format to annotate the input. 

A web interface for the Recommender is available [here](https://github.com/marcosmartinezromero/recommender2_web_ui).

##Version
This software constitutes a new version of the [NCBO Ontology Recommender](http://bioportal.bioontology.org/recommender), which has been re-developed from scratch. The recommendation algorithm has been redesigned to provide better results in terms of the coverage of the input data and to include additional ontology evaluation criteria. The output has also been changed to provide a more clear explanation about the recommendation results.

##Usage
The Recommender has been created using the [Dropwizard framework](http://dropwizard.io/), which makes it easy to build production-ready RESTful web services. Follow these steps to  execute the service:

1. Generate the .jar file: `mvn package`
2. Run the service: `java -jar target/recommender2_web_service-0.0.1-SNAPSHOT.jar server recommender2.yml`

To check if the service is running correctly, you can execute it for the input text "leukocyte" by navigating to the following url: http://localhost:9090/recommender2?text=leukocyte

##Contact
Marcos Martinez-Romero (<marcosmartinez@udc.es>)