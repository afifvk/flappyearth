package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.controllers.StageProgressController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;
import inf1009.p63.flappyearth.game.systems.GameplayLoop;

/**
 * Orchestrates the main gameplay update flow.
 * Single Responsibility: sequence frame updates and delegate to specialized controllers.
 */
public class GameplayUpdateCoordinator {

    private static final float INTRO_DURATION_SECONDS = 3.0f;

    private final SceneManager sceneManager;
    private final EngineContext context;
    private final StageTransitionController stageTransitionController;
    private final GameplaySession gameplayRuntimeSession;
    private final GameplayPauseController pauseController;

    private Player player;
    private float introTimer;
    private boolean showIntroText;

    public GameplayUpdateCoordinator(SceneManager sceneManager,
                                     EngineContext context,
                                     GameSession gameSession,
                                     StagePlan stagePlan,
                                     StageTransitionController stageTransitionController,
                                     GameplaySession gameplayRuntimeSession) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.stageTransitionController = stageTransitionController;
        this.gameplayRuntimeSession = gameplayRuntimeSession;
        this.pauseController = new GameplayPauseController(sceneManager, context, gameSession, stagePlan);
    }

    public void onEnter(boolean startFadeIn, Player initialPlayer) {
        this.player = initialPlayer;
        this.showIntroText = true;
        this.introTimer = INTRO_DURATION_SECONDS;
        this.pauseController.reset();
        this.stageTransitionController.onEnter(startFadeIn);
    }

    public void update(float delta,
                       GameplayRuntimeContext runtimeContext,
                       EndingController endingSceneController,
                       HelpOverlayController helpOverlayController,
                       PauseOverlayController pauseOverlayController,
                       GameplayLoop gameLoopManager,
                       DeathController deathController,
                       CameraController cameraController,
                       StageProgressController stageController,
                       GameSession gameSession) {
        if (stageTransitionController.update(delta, targetSceneId -> {
            gameSession.requestFadeIn();
            sceneManager.switchTo(targetSceneId);
        })) {
            return;
        }

        if (introTimer > 0f) {
            introTimer = Math.max(0f, introTimer - delta);
        }

        GameState gameState = gameSession.getGameState();
        player = runtimeContext.player();

        pauseController.updateInstructionsOverlay(helpOverlayController);
        if (pauseController.isShowingInstructionsOverlay()) {
            return;
        }

        pauseController.updatePauseToggle(gameState, endingSceneController);

        if (pauseController.handlePauseMenuInput(pauseOverlayController)) {
            return;
        }

        if (endingSceneController.update(
                delta,
                player,
                context.getInputManager(),
                sceneManager,
                gameState,
                context.getAudioManager())) {
            return;
        }

        GameplaySession.UpdateResult updateResult = gameplayRuntimeSession.update(
                delta,
                runtimeContext,
                gameState,
                gameLoopManager,
                deathController,
                cameraController,
                endingSceneController,
                stageController
        );
        player = updateResult.player;

        if (updateResult.routedToGameOver || updateResult.stageTransitioning) {
            return;
        }

        if (updateResult.pendingTransitionTarget != null) {
            stageTransitionController.requestTransition(updateResult.pendingTransitionTarget);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPaused() {
        return pauseController.isPaused();
    }

    public boolean isShowingInstructionsOverlay() {
        return pauseController.isShowingInstructionsOverlay();
    }

    public boolean isShowIntroText() {
        return showIntroText;
    }

    public float getIntroTimer() {
        return introTimer;
    }

    public float getTransitionAlpha() {
        return stageTransitionController.getAlpha();
    }
}
