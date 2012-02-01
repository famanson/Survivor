package geometry;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import light.LoopingList;
import light.Vec2;

public class Geometry {
	public static boolean left(Vec2 v1, Vec2 v2, Vec2 v3)
	{
		// return whether v3 is to the left of v1v2
		// find the signed area using the determinant method:
		float area = v1.x*v2.y - v1.y*v2.x + v2.x*v3.y - v2.y*v3.x + v3.x*v1.y - v3.y*v1.x;
		return area > 0;
	}
	
	public static float distance (Vec2 pos, Vec2 end1, Vec2 end2)
	{
		// Find the distance from pos to line end1-end2:
		// 1. find (2x)area of the triangle constructed by the 3 points:
		// (which is given by the determinant)
		float doubleArea = Math.abs(pos.x*end1.y - pos.y*end1.x + end1.x*end2.y - end1.y*end2.x + end2.x*pos.y - end2.y*pos.x);
		
		// 2. find distance from end1 to end2:
		Vec2 distVector = end1.sub(end2);
		float dist = distVector.length();
		
		return (doubleArea/dist);
	}
	
	public static boolean inBetween(float p, float e1, float e2)
	{
		if (p < e1)
		{
			return p >= e2;
		}
		else
		{
			return p < e2;
		}
	}
	
	public static List<Vec2> cluster(List<Vec2> points)
	{
		// create a convex hull out of the given points using the incremental algorithm
		// assuming these points have relative coordinates
		// assuming these hulls overlap
		// deal with trivial cases:
		if (points.size() == 0)
			return null;
		else if (points.size() <= 3)
			return points;
		
		// First sort the points in the order of x-coordinates
		Set<Vec2> allPoints = new TreeSet<Vec2>(new PointComparator());
		allPoints.addAll(points);
		Vec2[] s = (Vec2[]) allPoints.toArray();
		LoopingList<Vec2> newPoints = new LoopingList<Vec2>();
		int n = allPoints.size();
		int i = 3;
		// add the first 3 points to the list:
		newPoints.add(s[0]);
		newPoints.add(s[1]);
		newPoints.add(s[2]);
		
		while (i < n)
		{
			int j = newPoints.getLastInserted();
			int k = newPoints.getLastInserted();
			while (!Geometry.left(s[i], newPoints.get(j), newPoints.get(j+1)))
			{
				// remember: j is advanced counter-clockwise
				j++;
			}
			while (!Geometry.left(newPoints.get(k-1), newPoints.get(k),s[i]))
			{
				// remember: k is advanced clockwise
				k--;
			}
			// now we've reached the boundaries:
			newPoints.insertBetween(j, k, s[i]);
			i++;
		}
		return newPoints;
	}
	
}
