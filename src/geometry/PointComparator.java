package geometry;

import java.util.Comparator;

import light.Vec2;

public class PointComparator implements Comparator<Vec2>
{
	@Override
	public int compare(Vec2 v1, Vec2 v2) {
		if (v1.x == v2.x)
			return 0;
		else if (v1.x > v2.x)
			return 1;
		else return -1;
	}
}
