package edu.stanford.bmir.ncbo.recommender;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.bmir.ncbo.recommender.Recommender2Facade;
import edu.stanford.bmir.ncbo.util.PropertiesManager;

/**
 * This is a testing class that invokes the Ontology Recommender for a
 * predefined input and prints the output on the Console.
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 02/06/2014 11:59:12
 */
public class Test {

	public static void main(String[] args) {
		// Disable logging
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		// Coverage evaluation settings
		double wCoverage = PropertiesManager.getPropertyDouble("wCoverage");
		double prefScore = PropertiesManager.getPropertyDouble("prefScore");
		double synScore = PropertiesManager.getPropertyDouble("synScore");
		double multiTermScore = PropertiesManager
				.getPropertyDouble("multiTermScore");

		// Specialization evaluation settings
		double wSpecialization = PropertiesManager
				.getPropertyDouble("wSpecialization");

		// Acceptance evaluation settings
		double wAcceptance = PropertiesManager.getPropertyDouble("wAcceptance");
		double wPageviews = PropertiesManager.getPropertyDouble("wPageviews");
		double wUmls = PropertiesManager.getPropertyDouble("wUmls");
		double wPubmed = PropertiesManager.getPropertyDouble("wPubmed");

		// Detail of Knowledge settings
		double wDetail = PropertiesManager.getPropertyDouble("wDetail");

		/*** Input examples ***/
		String input = "Backpain, White blood cell, Carcinoma, Cavity of stomach, Ductal Carcinoma in Situ, Adjuvant chemotherapy, Axillary lymph node staging, Mastectomy, tamoxifen, serotonin reuptake inhibitors, Invasive Breast Cancer, hormone receptor positive breast cancer, ovarian ablation, premenopausal women, surgical management, biopsy of breast tumor, Fine needle aspiration, entinel lymph node, breast preservation, adjuvant radiation therapy, prechemotherapy, Inflammatory Breast Cancer, ovarian failure, Bone scan, lumpectomy, brain metastases, pericardial effusion, aromatase inhibitor, postmenopausal, Palliative care, Guidelines, Stage IV breast cancer disease, Trastuzumab, Breast MRI examination";
//		String input =
//		 "lichenification, herpetiform, petechiae, atrophy, abscess, bulla, cyst, furuncle, exfoliation, powdry, phenol peel, bursitis, keratoses";
		// String input =
		// "head, cavity of stomach, white blood cell, back pain";
//		 String input =
//		"Adjuvant chemotherapy";
		//String input = "white blood cell";
		//String input = "Primary treatment of DCIS now includes 3 options: lumpectomy without lymph node surgery plus whole breast radiation (category 1); total mastectomy with or without sentinel node biopsy with or without reconstruction (category 2A); lumpectomy without lymph node surgery without radiation (category 2B). Workup for patients with clinical stage l, llA, llB, or T3,N1,M0 disease was reorganized to distinguish optional additional studies from those recommended for all of these patients. Recommendation for locoregional treatment for patients with clinical stage l, llA, llB, or T3,N1,M0 disease with 1-3 positive axillary nodes following total mastectomy was changed from \"Consider\" to \"Strongly consider\" postmastectomy radiation therapy. For patients with hormone receptor-positive, HER2-negative tumors that are 0.6-1.0 cm and moderately/poorly differentiated or with unfavorable features, or > 1 cm, the recommendation for use of a 21-gene RT-PCR assay (category 2B) was added to the systemic adjuvant treatment decision pathway as an option for guiding chemotherapy treatment decisions. Systemic adjuvant treatment for patients with tubular or colloid tumors that are hormone receptor-positive and node-positive was changed from \"adjuvant hormonal therapy + adjuvant chemotherapy\" to \"adjuvant hormonal therapy adjuvant chemotherapy\". For hormone receptor-positive, node negative tubular/colloid tumors that are 1 cm, the recommendation for use or consideration of adjuvant chemotherapy was removed. The heading for workup for patients with locally advanced invasive cancer was modified to specify \"Noninflammatory\" disease and reorganized to distinguish optional additional studies from those recommended for all of these patients.";
		//String input = "cavity of stomach,  Platysma";
		
		int inputType = Recommender2Facade.INPUT_TYPE_TEXT;
		// int inputType = Recommender2Facade.INPUT_TYPE_KEYWORDS;	

		// Ontologies to evaluate. If the list is empty, all the ontologies in
		// BioPortal will be evaluated
		List<String> ontologyUris = new ArrayList<String>();		
		
		// outputType = 1 -> Single Ranking (Ontologies)
		// outputType = 2 -> Combined Ranking (Ontology sets)
		int outputType = 1;
		
		System.out.println("Input: ");
		System.out.println(input);
		System.out.println("Input type: " + inputType);
		System.out.println("Output type: " + outputType);

		// Max number of ontologies to "combine" (only for Combined Ranking)
		int maxElementsInCombination = 3;

		String rankingJson = Recommender2Facade.getRankingJson(input,
				inputType, outputType, maxElementsInCombination, ontologyUris,
				wCoverage, prefScore, synScore, multiTermScore, wDetail,
				wSpecialization, wAcceptance, wPageviews, wUmls, wPubmed);

		System.out.println("*** Single results ***");
		//System.out.println(rankingJson);

	}
}
