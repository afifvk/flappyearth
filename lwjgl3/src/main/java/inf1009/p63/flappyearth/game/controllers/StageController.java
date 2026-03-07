package inf1009.p63.flappyearth.game.controllers;

import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.scenes.StagePlan;
import inf1009.p63.flappyearth.game.scenes.StageConfig;
import inf1009.p63.flappyearth.game.state.EnvironmentProgress;

public class StageController {

    private final SceneManager sceneManager;
    private final StagePlan stagePlan;
    private final StageConfig stageConfig;
    private final EnvironmentProgress progress;
    private boolean transitioning;

    public StageController(SceneManager sceneManager,
                           StagePlan stagePlan,
                           StageConfig stageConfig,
                           EnvironmentProgress progress) {
        this.sceneManager = sceneManager;
        this.stagePlan = stagePlan;
        this.stageConfig = stageConfig;
        this.progress = progress;
    }

    public void onEnter() {
        transitioning = false;
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public void update() {
        if (transitioning) return;

        int stageTarget = stagePlan.getTargetForStage(stageConfig.getSceneId());
        if (progress.getGoodCollectiblesCollected() < stageTarget) return;

        String nextSceneId = stagePlan.getNextStageId(stageConfig.getSceneId());
        if (nextSceneId == null) return;

        transitioning = true;
        sceneManager.switchTo(nextSceneId);
    }
}
