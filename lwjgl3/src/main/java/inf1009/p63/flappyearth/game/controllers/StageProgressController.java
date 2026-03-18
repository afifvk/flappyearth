package inf1009.p63.flappyearth.game.controllers;

import inf1009.p63.flappyearth.engine.services.AudioManager;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.state.EnvironmentProgress;

public class StageProgressController {

    private final StagePlan stagePlan;
    private final StageConfig stageConfig;
    private final EnvironmentProgress progress;
    private final AudioManager soundManager;
    private boolean transitioning;
    private String  pendingTransitionSceneId;

    public StageProgressController(StagePlan stagePlan,
                           StageConfig stageConfig,
                           EnvironmentProgress progress,
                           AudioManager soundManager) {
        this.stagePlan = stagePlan;
        this.stageConfig = stageConfig;
        this.progress = progress;
        this.soundManager = soundManager;
    }

    public void onEnter() {
        transitioning = false;
        pendingTransitionSceneId = null;
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public String consumePendingTransition() {
        String id = pendingTransitionSceneId;
        pendingTransitionSceneId = null;
        return id;
    }

    public void update() {
        if (transitioning) return;

        int stageTarget = stagePlan.getTargetForStage(stageConfig.getSceneId());
        if (progress.getGoodCollectiblesCollected() < stageTarget) return;

        String nextSceneId = stagePlan.getNextStageId(stageConfig.getSceneId());
        if (nextSceneId == null) return;

        transitioning = true;

        if (soundManager != null) {
            soundManager.playSound(AudioKeys.STAGE_TRANSITION);
        }

        pendingTransitionSceneId = nextSceneId;
    }
}
