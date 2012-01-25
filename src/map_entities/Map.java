package map_entities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import light.ConvexHull;
import light.FrameBufferObject;
import light.Light;
import light.Vec2;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import entities.Survivor;
import entities.SurvivorListener;

public class Map implements SurvivorListener,RenderedMap {
	List<Block> blocks = new ArrayList<Block>();
	List<StreetLamp> lamps;
	public List<Light> lights = new ArrayList<Light>();
	Survivor survivor;
	Dimension dim;
	Rectangle currentView;
	public Map(Dimension dim, List<Block> blocks, List<StreetLamp> lamps, Survivor survivor)
	{
		this.dim = dim;
		this.blocks = blocks;
		Random random = new Random();
		for (int i = 0; i < 40000; i++)
        {
			blocks.add(new Block(new Vec2(random.nextInt(10240),random.nextInt(7680)), 50, 0.1f, Color.white));
        }
		blocks.add(new Block(new Vec2(500,600), 50, 0.1f, Color.white));
		blocks.add(new Block(new Vec2(450,600), 50, 0.1f, Color.white));
		this.lamps = lamps;
		this.survivor = survivor;
		this.survivor.addListener(this);
		
		lights.add(survivor);
		lights.addAll(lamps);
		
		currentView = new Rectangle(0, 0, 1024, 768);
	}
	
	public void moveCurrentView(float x, float y)
	{
		currentView.x += x;
		currentView.y += y;
	}
	
	public void update(Input input)
	{
		for (Block block : blocks)
		{
			// Collision check, record the collided block
			if (survivor.collide(block))
				break;
			
		}
		
		survivor.move(input);
	}
	
	public List<Light> lightsToRender()
	{
		List<Light> lightsToRender = new ArrayList<Light>(); 
		for (Light light : lights)
		{
			if (currentView.contains(light.getPos().x, light.getPos().y))
			{
				Light newLight = light.toRender(new Vec2(light.getPos().x - currentView.x, light.getPos().y - currentView.y),
						light.getRadius(),light.getDepth(),light.getColor());
				lightsToRender.add(newLight);
			}
		}
		return lightsToRender;
	}
	
	public List<Block> blocksToRender()
	{
		List<Block> blocksToRender = new ArrayList<Block>(); 
		for (Block block : blocks)
		{
			if (currentView.contains(block.getPos().x, block.getPos().y))
			{
				Block newBlock = new Block(new Vec2(block.getPos().x - currentView.x, block.getPos().y - currentView.y),
						block.getSide(),block.getDepth(),block.getColor());
				blocksToRender.add(newBlock);
			}
		}
		return blocksToRender;
	}
	
	protected ConvexHull cluster(Block[] blocks)
	{
		// create a convex hull out of the given blocks
		// deal with trivial cases:
		if (blocks.length == 0)
			return null;
		else if (blocks.length == 1)
			return blocks[0];
		
		// First sort the points in the order of x-coordinates
		Set<Vec2> allPoints = new TreeSet<Vec2>(new PointComparator());
		for (Block block : blocks)
		{
			allPoints.addAll(block.getPoints());
		}
		Vec2[] s = (Vec2[]) allPoints.toArray();
		// Unoptimised - still using a simple list:
		List<Vec2> hull = new ArrayList<Vec2>();
		int n = allPoints.size();
		int i = 3;
		// add the first 3 points to the list:
		hull.add(s[0]);
		hull.add(s[1]);
		hull.add(s[2]);
		
		while (i < n)
		{
			int j = hull.size();
			int k = hull.size();
			
		}
		return null;
	}
	
	private class PointComparator implements Comparator<Vec2>
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
	
	public List<Block> allBlocks()
	{
		return blocks;
	}
	
	public Vec2 getSurvivorPos()
	{
		return survivor.getPos();
	}
	
	@Override
	public void survivorMoved(float x, float y) {
		moveCurrentView(x, y);
	}
}
