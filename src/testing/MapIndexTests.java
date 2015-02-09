package testing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import framework.MapIndex;
import framework.Tile;
import framework.World;

public class MapIndexTests {
	
	private World theMap;
	
	@Before
	public void MaxIndexInit() {
		theMap = new World();
	}
	
	
	@Test
	public void testSingleTile() {
		Tile tileOne = new Tile(0, 0, null, null, null);
		theMap.addTile(tileOne);
		theMap.generateLookupTable();
		assertEquals(theMap.getMapTile(0, 0), tileOne);
	}
	
	@Test
	public void testTwoTilesOrdered() {
		Tile tileOne = new Tile(0, 0, null, null, null);
		Tile tileTwo = new Tile(1, 0, null, null, null);
		theMap.addTile(tileOne);
		theMap.addTile(tileTwo);
		theMap.generateLookupTable();
		assertEquals(theMap.getMapTile(0, 0), tileOne);
		assertEquals(theMap.getMapTile(1, 0), tileTwo);
		List<MapIndex> theTable = theMap.getLookupTable();
		assertEquals(theTable.size(), 2);
		assertEquals(theTable.get(0).getX(), 0);
		assertEquals(theTable.get(0).getY(), 0);
		assertEquals(theTable.get(1).getX(), 1);
		assertEquals(theTable.get(1).getY(), 0);
	}
	
	@Test
	public void testTwoTilesUnordered() {
		Tile tileOne = new Tile(0, 0, null, null, null);
		Tile tileTwo = new Tile(1, 0, null, null, null);
		theMap.addTile(tileTwo);
		theMap.addTile(tileOne);
		theMap.generateLookupTable();
		assertEquals(theMap.getMapTile(0, 0), tileOne);
		assertEquals(theMap.getMapTile(1, 0), tileTwo);
		List<MapIndex> theTable = theMap.getLookupTable();
		assertEquals(theTable.size(), 2);
		assertEquals(theTable.get(0).getX(), 0);
		assertEquals(theTable.get(0).getY(), 0);
		assertEquals(theTable.get(1).getX(), 1);
		assertEquals(theTable.get(1).getY(), 0);
	}
	
	@Test
	public void testTwoTilesSeperated() {
		Tile tileOne = new Tile(0, 0, null, null, null);
		Tile tileTwo = new Tile(1, 1, null, null, null);
		theMap.addTile(tileTwo);
		theMap.addTile(tileOne);
		theMap.generateLookupTable();
		assertEquals(theMap.getMapTile(0, 0), tileOne);
		assertEquals(theMap.getMapTile(1, 1), tileTwo);
		List<MapIndex> theTable = theMap.getLookupTable();
		assertEquals(theTable.size(), 4);
		assertEquals(theTable.get(0).getX(), 0);
		assertEquals(theTable.get(0).getY(), 0);
		assertEquals(theTable.get(1).getX(), 1);
		assertEquals(theTable.get(1).getY(), 0);
		assertEquals(theTable.get(2).getX(), 0);
		assertEquals(theTable.get(2).getY(), 1);
		assertEquals(theTable.get(3).getX(), 1);
		assertEquals(theTable.get(3).getY(), 1);
	}
	
	@Test
	public void testFourTilesUnordered() {
		Tile tileOne = new Tile(0, 0, null, null, null);
		Tile tileTwo = new Tile(1, 0, null, null, null);
		Tile tileThree = new Tile(0, 1, null, null, null);
		Tile tileFour = new Tile(1, 1, null, null, null);
		theMap.addTile(tileThree);
		theMap.addTile(tileTwo);
		theMap.addTile(tileFour);
		theMap.addTile(tileOne);
		theMap.generateLookupTable();
		assertEquals(theMap.getMapTile(0, 0), tileOne);
		assertEquals(theMap.getMapTile(1, 0), tileTwo);
		assertEquals(theMap.getMapTile(0, 1), tileThree);
		assertEquals(theMap.getMapTile(1, 1), tileFour);
		List<MapIndex> theTable = theMap.getLookupTable();
		assertEquals(theTable.size(), 4);
		assertEquals(theTable.get(0).getX(), 0);
		assertEquals(theTable.get(0).getY(), 0);
		assertEquals(theTable.get(1).getX(), 1);
		assertEquals(theTable.get(1).getY(), 0);
		assertEquals(theTable.get(2).getX(), 0);
		assertEquals(theTable.get(2).getY(), 1);
		assertEquals(theTable.get(3).getX(), 1);
		assertEquals(theTable.get(3).getY(), 1);
	}

}
