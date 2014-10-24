package edu.stanford.bmir.ncbo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Combinations;

/**
 * Useful combinatorical operations
 * 
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 27/07/2014 23:37:51
 */
public class CombinatoricalOperations {

	// Combinaciones sin repetición de los elementos "elements", con un número
	// máximo de elementos "maxElements"
	/**
	 * @param elements
	 * @param maxElements
	 *            Maximum number of items in each combination
	 * @return Combinations with repetition of "elements"
	 */
	public static ArrayList<ArrayList<String>> getCombinations(
			List<String> elements, int maxElements) {
		ArrayList<ArrayList<String>> allCombinations = new ArrayList<ArrayList<String>>();

		if (maxElements > elements.size())
			maxElements = elements.size();
		
		for (int i = 1; i < maxElements + 1; i++) {
			Combinations c = new Combinations(elements.size(), i);
			Iterator<int[]> it = c.iterator();
			while (it.hasNext()) {
				int[] combinationIndexes = it.next();
				// System.out.println(Arrays.toString(combinationIndexes));
				ArrayList<String> combination = new ArrayList<String>();
				for (int j = 0; j < combinationIndexes.length; j++) {
					combination.add(elements.get(combinationIndexes[j]));
				}
				allCombinations.add(combination);
				// System.out.println(combination);
			}
		}
		return allCombinations;
	}

	/*** Test code ***/
	public static void main(String[] args) {
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("cat");
		elements.add("dog");
		elements.add("bird");
		elements.add("lion");

		ArrayList<ArrayList<String>> combinations = getCombinations(elements, 1);
		// ArrayList<ArrayList<String>> combinations1 =
		// getCombinations1(elements, 1);

		System.out.println(combinations);
		System.out.println(combinations.size());
		// System.out.println(combinations1);
		// System.out.println(combinations1.size());
	}
}
