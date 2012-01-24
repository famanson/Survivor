package light;

import org.newdawn.slick.Color;

public class GeneralLight extends Light {

	public GeneralLight(Vec2 pos, float radius, float depth, Color color) {
		super(pos, radius, depth, color);
	}

	@Override
	public void render() {
		render(0.01f);
	}

	@Override
	public Light toRender(Vec2 pos, float radius, float depth, Color color) {
		return this;
	}

}
