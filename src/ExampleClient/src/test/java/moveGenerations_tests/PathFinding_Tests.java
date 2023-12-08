/*
package moveGenerations_tests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Utils;
import map.fullMap.FullMapGetter;
import map.fullMap.FullMapEntity;
import map.utils.EField;
import map.utils.Coordinates;
import moving.generation.PathFinder;

public class PathFinding_Tests {

	private FullMapGetter fma;
	private PathFinder pf;

	@BeforeEach
	public void generateFullMapAccesser() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		fma = new FullMapGetter(new FullMapEntity(testMap, new HashMap<>()));
	}

	@BeforeEach
	public void generatePathFinder() {
		pf = new PathFinder();
	}

	@Test
	public void PathFinder_StartNodeEqualsEndNode_shouldReturnEmptyQ() {

		Queue<Coordinates> res = pf.pathTo(new Coordinates(0, 0), new Coordinates(0, 0), fma);

		Assertions.assertEquals(res.size(), 0);
	}

	@Test
	public void PathFinder_StartIs00EndIs01_shouldReturn01() {

		Queue<Coordinates> res = pf.pathTo(new Coordinates(0, 0), new Coordinates(0, 1), fma);

		Assertions.assertEquals(res.size(), 1);

		Queue<Coordinates> compare = new LinkedList<Coordinates>();
		compare.add(new Coordinates(0, 1));

		Assertions.assertEquals(res, compare);
	}

	@Test
	public void PathFinder_StartIs00EndIs015_shouldReturnStraightLine() {

		Queue<Coordinates> res = pf.pathTo(new Coordinates(0, 0), new Coordinates(0, 15), fma);

		Assertions.assertEquals(res.size(), 15);

		Queue<Coordinates> compare = new LinkedList<Coordinates>();
		for (int i = 1; i < 16; ++i)
			compare.add(new Coordinates(0, i));

		Assertions.assertEquals(res, compare);
	}

	@Test
	public void PathFinder_StartIs11EndIs02_shouldNotGoOverMountain() {

		Queue<Coordinates> res = pf.pathTo(new Coordinates(1, 1), new Coordinates(0, 2), fma);

		Assertions.assertEquals(res.size(), 2);

		Queue<Coordinates> compare = new LinkedList<Coordinates>();
		compare.add(new Coordinates(0, 1));
		compare.add(new Coordinates(0, 2));

		Assertions.assertEquals(res, compare);
	}

}
*/
