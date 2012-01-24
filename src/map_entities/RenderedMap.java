package map_entities;

import java.util.List;
import light.Light;

public interface RenderedMap {
	public List<Light> lightsToRender();
	public List<Block> blocksToRender();
}
