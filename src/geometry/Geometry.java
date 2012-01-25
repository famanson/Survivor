package geometry;

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
}
