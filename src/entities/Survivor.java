package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import test.LightTest;

import light.Light;
import light.Vec2;
import map_entities.Block;

public class Survivor extends Light {
	static final float SPEED = 5;
	static final String UP = "UP";
	static final String DOWN = "DOWN";
	static final String LEFT = "LEFT";
	static final String RIGHT = "RIGHT";
	
	// direction of collision from a block
	static final int COL_NONE = -1;
	static final int COL_DOWN = 0;
	static final int COL_RIGHT = 1;
	static final int COL_UP = 2;
	static final int COL_LEFT = 3;
	
	List<SurvivorListener> listeners;
	
	Random flicker;
	// This is the playable character
	// Can be controlled by the key board
	// Currently, just a blob of light
	public Survivor(Vec2 pos, float radius, float depth, Color color) {
		super(pos, radius, depth, color);
		listeners = new ArrayList<SurvivorListener>();
		flicker = new Random();
	}
	
	public void addListener(SurvivorListener listener)
	{
		listeners.add(listener);
	}
	
	// Move by changing pos
	float prevX = 0;
	float prevY = 0;
	Vec2 posTest = new Vec2();
	boolean collidedOnce = false;
	public void move(Input input)
	{
		prevX = pos.x;
        prevY = pos.y;
    
        if(input.isKeyDown(Input.KEY_W) & collisionDir != COL_DOWN)
        {
    		prevY+=SPEED;
        }
        	
        if(input.isKeyDown(Input.KEY_S) & collisionDir != COL_UP)
        {
    		prevY-=SPEED;
        }
        
        if(input.isKeyDown(Input.KEY_A) & collisionDir != COL_RIGHT)
        {
    		prevX-=SPEED;
        }
        
        if(input.isKeyDown(Input.KEY_D) & collisionDir != COL_LEFT)
        {
    		prevX+=SPEED;
        }
        
        for (SurvivorListener l : listeners)
        {
        	l.survivorMoved(prevX - pos.x, prevY - pos.y);
        }
        
        pos.set(prevX, prevY);
	}
	
	public boolean collide (Block block)
	{
		return collide(pos, block);
	}
	
	Block collidedBlock;
	int collisionDir;
	int col = 0;
	public boolean collide (Vec2 pos, Block block)
	{
		// General method using signed distance:
		List<Vec2> points = block.getPoints();
		for (int i = 0; i < points.size(); i++)
		{
			if (i == points.size())
				break;
			Vec2 point1 = points.get(i);
			Vec2 point2 = points.get((i+1)%points.size());
			if (distance(pos, point1, point2) < 20)
			{
				if ((point1.x == point2.x & inBetween(pos.y, point1.y, point2.y)) 
						|| (point1.y == point2.y & inBetween(pos.x, point1.x, point2.x)))
				{
					col++;
					collidedBlock = block;
					collisionDir = i;
					//System.out.println(collisionDir);
					return true;
				}
			}
		}
		// No collision detected
		collisionDir = COL_NONE;
		collidedBlock = null;
		return false;
		//float i1 = (a.getY() - c.getY())*(d.getX() - c.getX()) - (a.getX() - c.getX())*(d.getY() - c.getY());
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
	
	float intensity = LightTest.DEFAULT_INTENSITY;
	@Override
	public void render() {
		intensity+=(flicker.nextFloat()-0.5f)/4.0f;
        intensity=Math.min(2.0f, Math.max(-1.0f, intensity));
        //System.out.println(intensity);
		intensity = flicker.nextFloat();
		render(intensity);
	}

	@Override
	public Light toRender(Vec2 pos, float radius, float depth, Color color) {
		return new Survivor(pos, radius, depth, color);
	}
}
