package inf1009.p63.flappyearth.game.setup;

import java.util.Arrays;

import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;

public final class GameStagePlanFactory {

    private GameStagePlanFactory() {}

    public static StagePlan createDefaultPlan() {
        return new StagePlan(Arrays.asList(
                new StageConfig(GameSceneId.STAGE_ONE.id(), "A Broken World", "Collect 1 good item", 0.20f, 0.45f, 0.95f, 0.70f, 1f,
                        "Restore the Earth one step at a time", "Collect good collectibles", "Good luck"),
                new StageConfig(GameSceneId.STAGE_TWO.id(), "Glimmer of Hope", "Collect 1 more item", 0.98f, 0.94f, 0.62f, 0.50f, 1.15f,
                        "Good Job!"),
                new StageConfig(GameSceneId.STAGE_THREE.id(), "Nature's Fruition", "Collect 1 more item", 0.70f, 0.86f, 0.58f, 0.25f, 1.22f,
                        "Keep it up!"),
                new StageConfig(GameSceneId.STAGE_FOUR.id(), "A Blooming Renaissance", "Final stage", 0.46f, 0.72f, 0.50f, 0f, 1.30f)
        ));
    }
}
