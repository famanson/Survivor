package map_entities;

import java.util.Random;

import org.newdawn.slick.Color;

import light.Light;
import light.Vec2;

public class StreetLamp extends Light {
	public static Random random = new Random();
	public StreetLamp(Vec2 pos, float radius, float depth, Color color) {
		super(pos, radius, depth, color);
		// TODO Auto-generated constructor stub
	}
	float intensity = 1.0f;
	public void flicker()
	{
		intensity+=(new Random().nextFloat()-0.5f)/4.0f;
        intensity=Math.min(2.0f, Math.max(-1.0f, intensity));
		this.render(intensity);
	}
	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
	@Override
	public Light toRender(Vec2 pos, float radius, float depth, Color color) {
		return new StreetLamp(pos, radius, depth, color);
	}
}
