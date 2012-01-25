package geometry;

import light.Vec2;

public class Edge {
	Vec2 v1;
	Vec2 v2;
	public Edge(Vec2 v1, Vec2 v2)
	{
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Vec2 cross(Edge otherEdge)
	{
		float d = (this.v1.x-this.v2.x)*(otherEdge.v1.y-otherEdge.v2.y)
				-(this.v1.y-this.v2.y)*(otherEdge.v1.x-otherEdge.v2.x);
		
		if (d == 0) return null;
		
		float xi = ((otherEdge.v1.x-otherEdge.v2.x)*(this.v1.x*this.v2.y-this.v1.y*this.v2.x)
				-(this.v1.x-this.v2.x)*(otherEdge.v1.x*otherEdge.v2.y-otherEdge.v1.y*otherEdge.v2.x))/d;
		
		float yi = ((otherEdge.v1.y-otherEdge.v2.y)*(this.v1.x*this.v2.y-this.v1.y*this.v2.x)
				-(this.v1.y-this.v2.y)*(otherEdge.v1.x*otherEdge.v2.y-otherEdge.v1.y*otherEdge.v2.x))/d;
		Vec2 intersection = new Vec2(xi, yi);
		if (Geometry.inBetween(intersection.x, this.v1.x, this.v2.x) &
			Geometry.inBetween(intersection.y, this.v1.y, this.v2.y) &
			Geometry.inBetween(intersection.x, otherEdge.v1.x, otherEdge.v2.x) &
			Geometry.inBetween(intersection.x, otherEdge.v1.y, otherEdge.v2.y))
			return intersection;
		else return null;
	}
}
