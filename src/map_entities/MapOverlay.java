package map_entities;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import entities.Survivor;
import entities.SurvivorListener;

import light.GeneralLight;
import light.Light;
import light.Vec2;

public class MapOverlay implements RenderedMap, SurvivorListener{
	Map map;
	Dimension dim;
	float scaleFactor = 0.2f;
	List<Block> minimisedBlocks;
	List<Light> lights;
	Block survivorSignal;
	public MapOverlay(Map map, Survivor survivor)
	{
		this.map = map;
		dim = new Dimension(1024, 768);
		survivor.addListener(this);
		
		//survivorSignal = new GeneralLight (new Vec2(survivor.getPos().x*scaleFactor, survivor.getPos().y*scaleFactor), 
		//								   10, 10, Color.green);
		
		survivorSignal = new Block(new Vec2(survivor.getPos().x*scaleFactor, survivor.getPos().y*scaleFactor), 
									(int) (50*scaleFactor), 0.1f, Color.green);
		lights = new ArrayList<Light>();
		//lights.add(survivorSignal);
		
		lights.add(new GeneralLight (new Vec2(-500,-500),1500,10,Color.white)); 
		lights.add(new GeneralLight (new Vec2(1524,-500),1500,10,Color.white));
		lights.add(new GeneralLight (new Vec2(-500,1268),1500,10,Color.white));
		lights.add(new GeneralLight (new Vec2(1524,1268),1500,10,Color.white));
		
		
		minimisedBlocks = new ArrayList<Block>();
		minimisedBlocks.add(survivorSignal);
		for (Block block : map.allBlocks())
		{
			Block smallBlock = new Block(new Vec2(block.getPos().x*scaleFactor, block.getPos().y*scaleFactor), 
					(int) (block.side*scaleFactor), block.getDepth(), block.getColor());
			minimisedBlocks.add(smallBlock);
			
			//Lit the block on the map
			//Light blockLight = new GeneralLight (new Vec2(block.getPos().x*scaleFactor+block.getSide()*scaleFactor/2, 
														 // block.getPos().y*scaleFactor+block.getSide()*scaleFactor/2), 
														 // block.getSide()*scaleFactor*2, 10, Color.white);
			//lights.add(blockLight);
		}
	}
	@Override
	public List<Light> lightsToRender() {
		return lights;
	}
	@Override
	public List<Block> blocksToRender() {
		return minimisedBlocks;
	}
	@Override
	public void survivorMoved(float x, float y) {
		float curX = survivorSignal.getPos().x;
		float curY = survivorSignal.getPos().y;
		survivorSignal.setPos(new Vec2(curX + x*scaleFactor, curY + y*scaleFactor));
	}
}
