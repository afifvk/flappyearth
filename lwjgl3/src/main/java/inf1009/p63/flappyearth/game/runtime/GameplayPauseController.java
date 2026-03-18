package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

/**
 * Handles pause state management and pause menu input.
 * Single Responsibility: manage paused state transitions and pause menu actions.
 */
public class GameplayPauseController {

    private final SceneManager sceneManager;
    private final EngineContext context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;

    private boolean paused;
    private boolean showingInstructionsOverlay;

    public GameplayPauseController(SceneManager sceneManager,
                                   EngineContext context,
                                   GameSession gameSession,
                                   StagePlan stagePlan) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.gameSession = gameSession;
        this.stagePlan = stagePlan;
        this.paused = false;
        this.showingInstructionsOverlay = false;
    }

    public void reset() {
        this.paused = false;
        this.showingInstructionsOverlay = false;
    }

    /**
     * Check pause toggle input and update pause state.
     */
    public void updatePauseToggle(GameState gameState,
                                   EndingController endingSceneController) {
        if (!gameState.isDeathSequenceActive()
                && !endingSceneController.isActive()
                && (Gdx.input.isKeyJustPressed(Input.Keys.P)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            paused = !paused;
            if (paused) {
                context.getAudioManager().pauseMusic();
            } else {
                context.getAudioManager().resumeMusic();
            }
        }
    }

    /**
     * Handle instructions overlay dismiss and state transitions.
     */
    public void updateInstructionsOverlay(HelpOverlayController helpOverlayController) {
        if (showingInstructionsOverlay) {
            if (helpOverlayController != null && helpOverlayController.isDismissRequested()) {
                showingInstructionsOverlay = false;
                paused = true;
            }
        }
    }

    /**
     * Process pause menu input and route to appropriate action.
     */
    public boolean handlePauseMenuInput(PauseOverlayController pauseOverlayController) {
        if (!paused) {
            return false;
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        PauseOverlayController.Action action = pauseOverlayController != null
                ? pauseOverlayController.handleInput(screenW, screenH)
                : PauseOverlayController.Action.NONE;

        if (action == PauseOverlayController.Action.NONE) {
            return true;
        }

        context.getAudioManager().playSound(AudioKeys.UI_CLICK);

        switch (action) {
            case RESUME:
                paused = false;
                context.getAudioManager().resumeMusic();
                break;
            case HELP:
                paused = false;
                showingInstructionsOverlay = true;
                break;
            case RESTART:
                paused = false;
                context.getAudioManager().resumeMusic();
                gameSession.resetForNewRun();
                gameSession.prepareForStageEntry();
                sceneManager.switchTo(stagePlan.getInitialStageId());
                break;
            case QUIT:
                paused = false;
                context.getAudioManager().resumeMusic();
                gameSession.resetForNewRun();
                sceneManager.switchTo(GameSceneId.MENU.id());
                break;
        }

        return true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isShowingInstructionsOverlay() {
        return showingInstructionsOverlay;
    }

    public void setShowingInstructionsOverlay(boolean showing) {
        this.showingInstructionsOverlay = showing;
    }
}
