package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.services.RenderManager;

public class CollectibleSourceRegionBinder {

    public void bind(RenderManager renderManager, CollectibleSourceBoundsResolver boundsResolver) {
        renderManager.setSourceRegionResolver((renderData, texture) ->
                boundsResolver.resolve(renderData.assetKey, texture));
    }
}
