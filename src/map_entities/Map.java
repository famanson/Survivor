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
import light.LoopingList;
import light.Vec2;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import entities.Survivor;
import entities.SurvivorListener;
import geometry.Edge;
import geometry.Geometry;

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
		for (int i = 0; i < 2000; i++)
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
	
	
	protected ConvexHull joinHulls(ConvexHull h1, ConvexHull h2)
	{
		List<Vec2> points = new LoopingList<Vec2>();
		// first, find the overlapping points and add them to the new hull:
		List<Edge> edges1 = h1.getEdges();
		List<Edge> edges2 = h2.getEdges();
		for (Edge edge1 : edges1)
		{
			for (Edge edge2 : edges2)
			{
				// this is the intersect on the actual map, but the points stored actually have an offset by pos
				// this means the choice of the new pos vector has to be coherent here.
				Vec2 absoluteIntersect = edge1.cross(edge2);
				if (absoluteIntersect != null)
					points.add(new Vec2(absoluteIntersect.x - h1.getPos().x, absoluteIntersect.y - h1.getPos().y));
			}
		}
		float deltaX = h2.getPos().x - h1.getPos().x;
		float deltaY = h2.getPos().y - h1.getPos().y;
		for (Vec2 point2 : h2.getPoints())
		{
			points.add(new Vec2(point2.x + deltaX, point2.y + deltaY));
		}
		
		points.addAll(h1.getRelativePoints());
		List<Vec2> newPoints = Geometry.cluster(points); // make a valid list of points for a new convex hull
		return new ConvexHull(h1.getPos(), newPoints, h1.getDepth(), Color.white);
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
