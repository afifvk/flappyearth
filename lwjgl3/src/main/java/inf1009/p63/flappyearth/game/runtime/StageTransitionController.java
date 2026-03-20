package inf1009.p63.flappyearth.game.runtime;

import java.util.function.Consumer;

public class StageTransitionController {

    private enum Phase { NONE, FADE_OUT, FADE_IN }

    private final float fadeSeconds;
    private Phase phase = Phase.NONE;
    private float alpha = 0f;
    private String targetSceneId;

    public StageTransitionController(float fadeSeconds) {
        this.fadeSeconds = fadeSeconds;
    }

    public void onEnter(boolean startFadeIn) {
        phase = startFadeIn ? Phase.FADE_IN : Phase.NONE;
        alpha = startFadeIn ? 1f : 0f;
        targetSceneId = null;
    }

    public void requestTransition(String targetSceneId) {
        if (targetSceneId == null || phase != Phase.NONE) {
            return;
        }
        this.targetSceneId = targetSceneId;
        this.phase = Phase.FADE_OUT;
        this.alpha = 0f;
    }

    public boolean update(float delta, Consumer<String> onFadeOutComplete) {
        if (phase == Phase.FADE_OUT) {
            alpha = Math.min(1f, alpha + (delta / fadeSeconds));
            if (alpha >= 1f && targetSceneId != null) {
                String target = targetSceneId;
                targetSceneId = null;
                phase = Phase.NONE;
                onFadeOutComplete.accept(target);
            }
            return true;
        }

        if (phase == Phase.FADE_IN) {
            alpha = Math.max(0f, alpha - (delta / fadeSeconds));
            if (alpha <= 0f) {
                phase = Phase.NONE;
            }
            return true;
        }

        return false;
    }

    public float getAlpha() {
        return alpha;
    }
}
