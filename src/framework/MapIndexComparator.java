package framework;

import java.util.Comparator;

public class MapIndexComparator implements Comparator<MapIndex> {
	
	@Override
	public int compare(MapIndex o1, MapIndex o2) {
		if (o1.equals(o2)) return 0;
		// o1 has a greater Y (above)
		if (o1.getY() > o2.getY()) {
			return 1;
		// o1 has same Y and greater X (to the left)
		} else if (o1.getY() == o2.getY() && o1.getX() > o2.getX()) {
			return 1;
		}
		return -1;
	}
}
