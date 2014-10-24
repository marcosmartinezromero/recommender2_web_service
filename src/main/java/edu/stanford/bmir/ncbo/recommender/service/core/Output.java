package edu.stanford.bmir.ncbo.recommender.service.core;

import java.util.List;
import edu.stanford.bmir.ncbo.recommender.result.RecommendationResultTO;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 21/05/2014 20:22:38
 */
public class Output {   
    private final List<RecommendationResultTO> result;

	public Output(List<RecommendationResultTO> result) {
		super();
		this.result = result;
	}

	public List<RecommendationResultTO> getResult() {
		return result;
	}   
}