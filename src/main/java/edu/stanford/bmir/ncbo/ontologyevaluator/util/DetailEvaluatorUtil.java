package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import edu.stanford.bmir.ncbo.annotator.AnnotatorAdapter;
import edu.stanford.bmir.ncbo.annotator.to.AnnotationTO;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 02/11/2014 12:45:13
 */
public class DetailEvaluatorUtil {

	/*
	 * This method calculates the number of definitions, synonyms and properties
	 * for a large sample of BioPortal concepts. The goal is to use those values
	 * to calculate the thresholds that will be used to assign the maximum score
	 * for each parameter. For example, suppose that the threshold for the
	 * number of synonyms is 4. Then, if an ontology class provides 4 synonyms
	 * or more, it will be assigned the maximum detail score regarding the
	 * synonyms. Each threshold is calculated as the 9th decile of the sample,
	 * so 90% of the sample values are lower than it. The thresholds used can be
	 * found in the configuration file.
	 */
	public static void calculateThresholds() {

		HashMap<String, List<AnnotationTO>> allAnnotations = new HashMap<String, List<AnnotationTO>>();
		List<String> inputs = new ArrayList<String>();
		inputs.add("Ductal Carcinoma in Situ, Adjuvant chemotherapy, Axillary lymph node staging, Mastectomy, tamoxifen, serotonin reuptake inhibitors, Invasive Breast Cancer, hormone receptor positive breast cancer, ovarian ablation, premenopausal women, surgical management, biopsy of breast tumor, Fine needle aspiration, entinel lymph node, breast preservation, adjuvant radiation therapy, prechemotherapy, Inflammatory Breast Cancer, ovarian failure, Bone scan, lumpectomy, brain metastases, pericardial effusion, aromatase inhibitor, postmenopausal, Palliative care, Guidelines, Stage IV breast cancer disease, Trastuzumab, Breast MRI examination, vital signs, mean arterial pressure, cardiac output, pressure venous central, expert systems, decision, serum, quadruple, add, stop, double, ascend, descend, continuous, medical device, pump, monitoring device, pharmacologic substance, vasodilator, nitroprusside, nitroglycerin, adrenaline, inotropic agents, noradrenaline, dobutamine, accepted, lower limit value, upper limit value, value, name, priority, variation, Hospital, Consultant, Patient unit number, NHS number, Forenames, Surname, Name at birth, Address at time of diagnosis, Postal code, Sex, Ethnic origin, Date of birth, Neoplasm location, Morphology, Laterality, Stage, Tumor grading, Basis of diagnosis, Date of diagnosis, Treatment indicators, Vital status, Date of death, Cause and place of death, Post mortem, Dimorphism, Biliary system, Distal, Dorsal, Ectoderm, Electrolyte, Sural nerve, Synapse, Calcaneal tendon, Endocardium, Endoderm, Epithelium, Gonadotropins, Heterophilic, Homeothermic, Squamous, Stratified epithelium, Set of meninges, Paraganglion, Radial nerve, Dopamine receptor, Syncytium, Telogen, Skull, Cavity of stomach, Surface of bone of foot, Cranial cavity, Spinal canal, Thoracic cavity, Gingiva, Abdominopelvic cavity, Spectrin, Pericardial cavity, Gallbladder, Spleen, Kidneys, Set of muscles of abdomen, Basilar artery, Hepatic vein, Uterine tubes, Sagittal plane, Ventral, Vertebral canal, Agger nasi, Allantois, Alveus, Anconeus, Annulus, Hamstrings, Quadrigeminus, Macula, Mammilla, Masseter, Mediastinum, Mesencephalon, Modiolus, Mylohyoid, Pampiniform, Paraesthesia, Parotid, Pedicle, Pennate, Periosteum, Platysma, Proprioceptive, Psoas, Arch of aorta, Axon, Lysosome, Mechanoreceptor, Tibial tuberosity, Perimysium, Musculature of head, Rectus capitis anterior, Popliteus, Midcarpal joint, Obturator canal, Long bone, Skin of elbow, Umbilical vein, Supination");
		// ArrayExpress
		inputs.add("Entomopathogenic nematodes (EPNs) of the genera Heterorhabditis are obligate and lethal insect parasites. In recent years they have been used increasingly as biological control agents. These EPNs are symbiotically associated with bacteria of the genera Photorhabdus. The bacterial symbionts are essential to kill the host (within 24-48 hours) and digest its tissues to provide nutrients for themselves as well for expanding nematodes. Drosophila larvae are suitable insect hosts and part of the tripartite model system we used before to show the importance of haemolymph clotting and eicosanoids during the infection. We used the well-established tripartite model (Drosophila, nematodes, bacteria), DNA chips and bioinformatic tools to compare gene expression in non-infected and infected fly larvae. We focused on the early time point of nematode infection and therefore infected Drosophila larvae using H. bacteriophora harbouring GFP-labelled P. luminescens bacteria. Infected (GFP positive) larvae were collected 6 hours after infection.");
		// PharmGKB
		inputs.add("Vorinostat (rINN) or suberoylanilide hydroxamic acid (SAHA), is a drug currently under investigation for the treatment of cutaneous T cell lymphoma (CTCL), a type of skin cancer, to be used when the disease persists, gets worse, or comes back during or after treatment with other medicines. It is the first in a new class of agents known as histone deacetylase inhibitors. A recent study suggested that vorinostat also possesses some activity against recurrent glioblastoma multiforme, resulting in a median overall survival of 5.7 months (compared to 4 - 4.4 months in earlier studies). Further brain tumor trials are planned in which vorinostat will be combined with other drugs.");
		// PubMed
		inputs.add("The ERG rearrangement is identified in approximately 50% of prostate cancer screened cohorts and is known to be highly specific. This genetic aberration, most commonly leading to the TMPRSS2-ERG fusion, but also SLC45A3-ERG or NDRG1-ERG fusions, all leading to an overexpression of a truncated ERG protein. Most studies have applied in situ hybridization (FISH) methods or mRNA-based assays to investigate the ERG status. Recently, studies showed that ERG protein levels assessed by ERG antibodies can be used as a surrogate marker for ERG rearrangement. In the current study, we investigate ERG status on a series of diagnostic biopsies using DNA-based, mRNA-based, and protein-based assays. We formally compared 3 assay results (ie, FISH, fusion mRNA, and immunohistochemistry) to identify which method could be most appropriate to use when having limited amount of tissue. ERG rearrangement was found in 56% of the cases. Comparing ERG rearrangement status by FISH with ERG overexpression and TMPRSS2-ERG fusion transcript we found 95.1% (154/162, Fisher exact test 9.50E-36) and 85.2% (138/162, Fisher exact test 7.26E-22) concordance, respectively. We show that the ERG antibody highly correlates with the ERG rearrangement with high sensitivity and specificity. We also identified the most common TMPRSS2-ERG isoform in the majority of ERG rearranged cases. These results provide compelling evidence that the ERG antibody can be used to further investigate the role of ERG in prostate cancer.");
		// GEO
		inputs.add("In humans, a primate-specific variable-number tandem-repeat (VNTR) polymorphism (4 or 5 repeats 54 nt in length) in the circadian gene PER3 is associated with differences in sleep timing and homeostatic responses to sleep loss. We investigated the effects of this polymorphism on circadian rhythmicity and sleep homeostasis by introducing the polymorphism into mice and assessing circadian and sleep parameters at baseline and during and after 12 h of sleep deprivation (SD). Microarray analysis was used to measure hypothalamic and cortical gene expression. Circadian behavior and sleep were normal at baseline. The response to SD of 2 electrophysiological markers of sleep homeostasis, electroencephalography (EEG) ? power during wakefulness and d power during sleep, were greater in the Per35/5 mice. During recovery, the Per35/5 mice fully compensated for the SD-induced deficit in d power, but the Per34/4 and wild-type mice did not. Sleep homeostasis-related transcripts (e.g., Homer1, Ptgs2, and Kcna2) were differentially expressed between the humanized mice, but circadian clock genes were not. These data are in accordance with the hypothesis derived from human data that the PER3 VNTR polymorphism modifies the sleep homeostatic response without significantly influencing circadian parameters.-Hasan, S., van der Veen, D. R., Winsky-Sommerer, R., Hogben, A., Laing, E. E., Koentgen, F., Dijk, D.-J., Archer, S. N. A human sleep homeostasis phenotype in mice expressing a primate-specific PER3 variable-number tandem-repeat coding-region polymorphism.");
		// ClinicalTrials
		inputs.add("Patients who sustain a broken lower jaw have traditionally been treated in one of three ways. The first involves having their teeth and jaws being wired together for a period of 4 to 6 weeks in order to allow the broken jaw to heal. The second and third ways involve a surgical procedure that requires exposing the broken bones and stabilizing them with metal plates and screws that allow the patient to be able to function relatively normally during the healing period. One surgical method uses small plates and screws while the alternate method uses large plates and screws. Currently there are two schools of thought with respect to what plates and screws should be used. One group supports the use of large rigid plates due to the increased strength of the plate. The use of the larger and stronger plates is the principle behind the AO technique, which was originally developed in the 1970's in Switzerland and is now the more popular technique in the USA. The other group supports the use of smaller plates and screws which must be placed in certain anatomical positions to allow the natural muscular forces that exist on the jaw to stabilize the break and facilitate complete healing of the broken bone. This technique was developed in France by Maxime Champy in the 1970's and is the standard of care throughout Australasia and parts of Europe. This technique is simpler, quicker and cheaper. The need for patients to maintain a diet with softer foods is considered by many to be important for success if the less rigid and smaller plates are going to be used. Many critics of the Champy technique feel that less compliant patient populations as might be seen in a county hospital make the technique less readily suited to these populations. This is contrary to published data from Europe, Australia and recently the USA. The question of interest is whether the smaller plates and screws are equally as effective in the treatment of broken lower jaws in an urban county hospital? If they are equally effective, then is there any benefit in terms of fewer patient complications and decreased health costs? If the smaller plates and screws are not adequate, then will a modification of the original Champy technique improve their usefulness? Patients who present with a broken lower jaw who require surgery will be treated in one of three ways. Some patients will be treated with the larger plates and screws by an attending surgeon who routinely uses large plates and screws for broken lower jaws. Another group of patients will be treated with the smaller plates and screws using the Champy principles. A third group of patients will also be treated with the smaller plates and screws but using a modification of the original Champy technique that involves the use of additional small plates and screws for added stability. Patients will then be followed over a three month period to evaluate for healing of the broken jaw. The three techniques will be then be compared. Larger plates/ screws and the smaller plates/ screws are both the standard of care. Regional differences throughout the USA has continued to ensure differences of opinion with regard to which technique is better although historically the larger plates/screws has been more popular in the USA.	");
		int inputType = 1;

		List<Double> numberOfDefinitions = new ArrayList<Double>();
		List<Double> numberOfSynonyms = new ArrayList<Double>();
		List<Double> numberOfProperties = new ArrayList<Double>();

		for (String input : inputs) {

			HashMap<String, List<AnnotationTO>> annotations = AnnotatorAdapter
					.getAnnotations(input, inputType, null);

			for (Map.Entry<String, List<AnnotationTO>> annForOntology : annotations
					.entrySet()) {
				for (AnnotationTO ann : annForOntology.getValue()) {
					numberOfDefinitions.add((double) ann
							.getNumberOfDefinitions());
					numberOfSynonyms.add((double) ann.getNumberOfSynonyms());
					numberOfProperties
							.add((double) ann.getNumberOfProperties());
				}
			}

			allAnnotations.putAll(annotations);

		}
		System.out.println(numberOfDefinitions.size());
		System.out.println(numberOfSynonyms.size());
		System.out.println(numberOfProperties.size());

		Collections.sort(numberOfDefinitions);
		System.out.println("Deciles: ");
		System.out.println(Arrays.toString(MathUtil.getDeciles((ArrayUtils
				.toPrimitive(numberOfDefinitions
						.toArray(new Double[numberOfDefinitions.size()]))))));
		System.out.println(Arrays.toString(MathUtil.getDeciles((ArrayUtils
				.toPrimitive((Double[]) numberOfSynonyms
						.toArray(new Double[numberOfSynonyms.size()]))))));
		System.out.println(Arrays.toString(MathUtil.getDeciles((ArrayUtils
				.toPrimitive((Double[]) numberOfProperties
						.toArray(new Double[numberOfProperties.size()]))))));

	}

	public static void main(String[] args) {
		System.out.println("Annotation in progress...");
		calculateThresholds();
	}

}
