/*
package moveGenerations_tests;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import map.utils.Coordinates;
import moving.generation.PathAlgorithm;

public class PathAlgorithm_Tests {

	@Test
	public void ShortestPathExtractor_SendValidPositions_ShouldReturnAllSentNodes() {

		Set<Coordinates> toVisit = new HashSet<>();

		toVisit.add(new Coordinates(0, 1));
		toVisit.add(new Coordinates(3, 1));
		toVisit.add(new Coordinates(2, 4));
		toVisit.add(new Coordinates(1, 1));
		toVisit.add(new Coordinates(10, 2));

		PathAlgorithm spe = new PathAlgorithm();

		Queue<Coordinates> inOrder = spe.visitInOrder(toVisit, new Coordinates(0, 0));

		// remove any potential duplicates
		Set<Coordinates> inOrderNoDup = inOrder.stream().collect(Collectors.toSet());

		Assertions.assertEquals(inOrderNoDup.size(), toVisit.size());
		Assertions.assertEquals(inOrderNoDup.stream().allMatch(ele -> toVisit.contains(ele)), true);
	}

}
*/
