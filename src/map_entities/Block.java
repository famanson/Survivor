package map_entities;

import java.util.Arrays;
import org.newdawn.slick.Color;

import light.ConvexHull;
import light.Vec2;

public class Block extends ConvexHull {
	// assuming it is a large square for now
	protected int side;
	public Block(Vec2 pos, int side, float depth, Color c)
	{
		super(pos, Arrays.asList(new Vec2[] {new Vec2(0, 0), new Vec2(side, 0), 
											new Vec2(side, side), new Vec2(0, side)}) , depth, c);
		this.side = side;
	}
	
	public int getSide()
	{
		return side;
	}
	
	public void setPos(Vec2 pos)
	{
		float deltaX = pos.x - this.pos.x;
		float deltaY = pos.y - this.pos.y;
		
		for (Vec2 point: points)
		{
			point.x += deltaX;
			point.y += deltaY;
		}
		this.pos = pos;
	}
}
