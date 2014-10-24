package edu.stanford.bmir.ncbo.recommender.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * This is a DropWizard class that specifies environment-specific parameters for
 * the web service
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 21/05/2014 20:21:23
 */
public class Recommender2Configuration extends Configuration {
	//@NotEmpty
	private String inputText;
	private int inputType;
	private int outputType;
	private int maxElementsInCombination;
	private double wc;
	private double ws;
	private double wa;
	
	@JsonProperty
	public String getInputText() {
		return inputText;
	}

	@JsonProperty
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	@JsonProperty
	public int getInputType() {
		return inputType;
	}

	@JsonProperty
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	@JsonProperty
	public int getOutputType() {
		return outputType;
	}

	@JsonProperty
	public void setOutputType(int outputType) {
		this.outputType = outputType;
	}

	@JsonProperty
	public int getMaxElementsInCombination() {
		return maxElementsInCombination;
	}

	@JsonProperty
	public void setMaxElementsInCombination(int maxElementsInCombination) {
		this.maxElementsInCombination = maxElementsInCombination;
	}
	
	@JsonProperty
	public double getWc() {
		return wc;
	}

	@JsonProperty
	public void setWc(double wc) {
		this.wc = wc;
	}

	@JsonProperty
	public double getWs() {
		return ws;
	}

	@JsonProperty
	public void setWs(double ws) {
		this.ws = ws;
	}

	@JsonProperty
	public double getWa() {
		return wa;
	}

	@JsonProperty
	public void setWa(double wa) {
		this.wa = wa;
	}

}
