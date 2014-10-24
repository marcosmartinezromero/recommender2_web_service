package edu.stanford.bmir.ncbo.ontologyevaluator.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 01/10/2014 16:25:11
 */
public class BioPortalPageviewsUtilTest {

	/**
	 * Test method for {@link edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalPageviewsUtil#getPosition(java.lang.String)}.
	 */
	@Test
	public void testGetPosition() {
		String ontologyUri = "http://data.bioontology.org/ontologies/NCIT";			
		assertTrue(BioPortalPageviewsUtil.getPosition(ontologyUri) == 5);		
	}
	
	/**
	 * Test method for {@link edu.stanford.bmir.ncbo.ontologyevaluator.util.BioPortalPageviewsUtil#getRankingSize()}.
	 */
	@Test
	public void testGetRankingSize() {
		assertTrue(BioPortalPageviewsUtil.getRankingSize() > 350);
	}

}
