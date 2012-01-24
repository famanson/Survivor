/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import light.FrameBufferObject;
import light.Light;
import light.Vec2;
import map_entities.Block;
import map_entities.Map;
import map_entities.MapOverlay;
import map_entities.RenderedMap;
import map_entities.StreetLamp;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import entities.Survivor;

public class LightTest extends BasicGame {

    public static List<Light> lights;
    private boolean addLight=true;
    public static final float DEFAULT_INTENSITY = 1.0f;
    private FrameBufferObject fbo;
    
    Map map;
    MapOverlay mapOverlay;
    RenderedMap renderedMap;
    
    public LightTest(String name) {
        super(name);
        lights = new ArrayList<Light>();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AppGameContainer contain = new AppGameContainer(new LightTest("Game"));
            contain.setDisplayMode(1024, 768, false);
            contain.setTargetFrameRate(60);
            contain.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        fbo = new FrameBufferObject(new Dimension(1024, 768));
        List<Block> blocks = new ArrayList<Block>();
        
        List<StreetLamp> lamps = new ArrayList<StreetLamp>();
        Survivor survivor = new Survivor(new Vec2(400,400), 200, 10, 
			  	   new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()));
        
        map = new Map(new Dimension(10240,7680), blocks, lamps, survivor);
        mapOverlay = new MapOverlay(map , survivor);
        renderedMap = map;
    }
    //float intensity = DEFAULT_INTENSITY;
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyPressed(Input.KEY_M))
        {
        	if (renderedMap == map)
        	{
        		// Switch to overlay
        		renderedMap = mapOverlay;
        	}
        	else
        	{
        		renderedMap = map;
        	}
        }
        if (renderedMap == map)
        	map.update(input);
    }
    
    Font font=new Font( "Arial", Font.PLAIN, 20);
	UnicodeFont f = new UnicodeFont(font,20, false, true);
	
    public void render(GameContainer container, Graphics g) throws SlickException {
        renderScene(g);
        
		f.addAsciiGlyphs();
		f.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		f.loadGlyphs();
		g.setFont(f);
        g.drawString("FPS: " + Integer.toString(container.getFPS()), 50, 50);
    }

    public void renderScene(Graphics g)
    {
        //# Clear the color buffer
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        //# Use less-than or equal depth testing
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        fbo.enable();
        
        //# Clear the fbo, and z-buffer
        clearFbo();

        //# fill z-buffer
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glColorMask(false, false, false, false);
        for (Block block : renderedMap.blocksToRender()) {
            block.render();
        }
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (Light light: renderedMap.lightsToRender()) {
            //# Clear the alpha channel of the framebuffer to 0.0
            clearFramebufferAlpha();

            //# Write new framebuffer alpha
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColorMask(false, false, false, true);
            light.render();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ZERO);
            //# Draw shadow geometry, but only when the main game map is present
            if (renderedMap == map)
	            for (Block block : renderedMap.blocksToRender()) {
	            		block.drawShadowGeometry(light);
	            }
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            //# Draw geometry
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE);
            GL11.glColorMask(true, true, true, false);
            if(addLight) {
                for (Light light1: renderedMap.lightsToRender()) {
                    light1.render();
                }
            }
            for (Block block: renderedMap.blocksToRender()) {
                block.render();
            }
        }

        fbo.disable();

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        
        //# Render the fbo on top of the color buffer
        fbo.render(1.0f);
    }

    private void clearFbo() {
        GL11.glClearDepth(1.1);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
    }

    private void clearFramebufferAlpha() {
        GL11.glColorMask(false, false, false, true);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }
    
    

}
