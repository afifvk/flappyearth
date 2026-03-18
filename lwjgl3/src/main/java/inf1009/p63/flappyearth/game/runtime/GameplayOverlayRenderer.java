package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.engine.services.RenderManager;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameplayOverlayRenderer {

    public void renderHearts(SpriteBatch hudBatch,
                             RandomManager randomManager,
                             GameState gameState,
                             Player player,
                             Texture heartFullTexture,
                             Texture heartEmptyTexture,
                             float screenH,
                             float hudScale) {
        if (player == null || heartFullTexture == null || heartEmptyTexture == null || gameState == null) {
            return;
        }

        int maxHealth = gameState.getMaxHearts();
        int currentHealth = gameState.getHearts();
        float shakeTimer = player.getShakeTimer();
        float heartSize = 32f * hudScale;
        float startX = 20f * hudScale;
        float startY = screenH - 60f * hudScale;
        float spacing = 44f * hudScale;

        for (int i = 0; i < maxHealth; i++) {
            float drawX = startX + (i * spacing);
            float drawY = startY;

            if (shakeTimer > 0f) {
                float shakeIntensity = shakeTimer / 0.5f;
                if (i == currentHealth) {
                    float maxShake = 10f;
                    drawX += randomManager.range(-maxShake, maxShake) * shakeIntensity;
                    drawY += randomManager.range(-maxShake, maxShake) * shakeIntensity;
                } else if (i < currentHealth) {
                    float minorShake = 2f;
                    drawX += randomManager.range(-minorShake, minorShake) * shakeIntensity;
                    drawY += randomManager.range(-minorShake, minorShake) * shakeIntensity;
                }
            }

            if (i < currentHealth) {
                hudBatch.draw(heartFullTexture, drawX, drawY, heartSize, heartSize);
            } else {
                hudBatch.draw(heartEmptyTexture, drawX, drawY, heartSize, heartSize);
            }
        }
    }

    public void renderHudTextOverlays(SpriteBatch hudBatch,
                                      BitmapFont introFont,
                                      GlyphLayout introLayout,
                                      float screenW,
                                      float screenH,
                                      float hudScale,
                                      boolean showIntroText,
                                      float introTimer,
                                      String[] introLines,
                                      boolean paused,
                                      boolean showingInstructionsOverlay,
                                      EndingController endingSceneController) {
        if (showIntroText && introTimer > 0f && introLines != null && introLines.length > 0) {
            float lineSpacing = screenH * 0.08f;
            float centerY = screenH * 0.62f;
            float startY = centerY + (lineSpacing * (introLines.length - 1) / 2f);
            for (int i = 0; i < introLines.length; i++) {
                introLayout.setText(introFont, introLines[i]);
                introFont.draw(hudBatch, introLayout,
                        (screenW - introLayout.width) / 2f,
                        startY - (i * lineSpacing));
            }
        }

        if (endingSceneController.isActive() && endingSceneController.isAwaitingContinue()) {
            int revealed = endingSceneController.getRevealedLines();
            float endingBaseY = screenH * 0.72f;
            float endingLineGap = screenH * 0.095f;
            float shadowOffset = Math.max(1.5f, screenH * 0.0018f);

            if (revealed >= 1) {
                drawCenteredOverlayText(hudBatch, introFont, introLayout,
                        "Earth is restored!", screenW, endingBaseY, shadowOffset);
            }
            if (revealed >= 2) {
                drawCenteredOverlayText(hudBatch, introFont, introLayout,
                        "Let us continue to do our part to keep Earth clean.",
                        screenW, endingBaseY - endingLineGap, shadowOffset);
            }
            if (revealed >= 3) {
                drawCenteredOverlayText(hudBatch, introFont, introLayout,
                        "Press SPACE to continue playing",
                        screenW, endingBaseY - (endingLineGap * 2f), shadowOffset);
            }
            if (revealed >= 4) {
                drawCenteredOverlayText(hudBatch, introFont, introLayout,
                        "Press ESC / P to exit",
                        screenW, endingBaseY - (endingLineGap * 3f), shadowOffset);
            }
        }

        if (endingSceneController.isActive() && endingSceneController.isSpawnWarmup()) {
            String warmupText = "Take control in "
                    + (int) Math.ceil(endingSceneController.getSpawnWarmupTimer()) + "...";
            float shadowOffset = Math.max(1.5f, screenH * 0.0018f);
            drawCenteredOverlayText(hudBatch, introFont, introLayout,
                    warmupText, screenW, screenH * 0.50f, shadowOffset);
        }

        if (!paused && !showingInstructionsOverlay && !endingSceneController.isActive()) {
            String pauseHint = "Press P to Pause";
            introLayout.setText(introFont, pauseHint);
            float hintX = screenW - introLayout.width - (20f * hudScale);
            float hintY = 34f * hudScale;
            introFont.draw(hudBatch, introLayout, hintX, hintY);
        }
    }

    public void renderModalAndFinalOverlays(RenderManager renderManager,
                                            GameplayRenderer gameplayRenderer,
                                            BrightnessOverlayRenderer brightnessOverlayRenderer,
                                            PauseOverlayController pauseOverlayController,
                                            HelpOverlayController helpOverlayController,
                                            float screenW,
                                            float screenH,
                                            boolean paused,
                                            boolean showingInstructionsOverlay,
                                            float brightness,
                                            float transitionAlpha) {
        if (showingInstructionsOverlay) {
            if (helpOverlayController != null) {
                helpOverlayController.render(renderManager.getShapeRenderer(), renderManager.getBatch(), screenW, screenH);
            }
        } else if (paused) {
            if (pauseOverlayController != null) {
                pauseOverlayController.render(renderManager.getShapeRenderer(), renderManager.getBatch(), screenW, screenH);
            }
        }

        SpriteBatch batch = renderManager.getBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        batch.begin();
        brightnessOverlayRenderer.render(batch, brightness);
        batch.end();

        gameplayRenderer.renderStageTransitionOverlay(renderManager, screenW, screenH, transitionAlpha);
    }

    private void drawCenteredOverlayText(SpriteBatch batch,
                                         BitmapFont introFont,
                                         GlyphLayout introLayout,
                                         String text,
                                         float screenW,
                                         float y,
                                         float shadowOffset) {
        introLayout.setText(introFont, text);
        float x = (screenW - introLayout.width) / 2f;

        introFont.setColor(0f, 0f, 0f, 0.72f);
        introFont.draw(batch, text, x + shadowOffset, y - shadowOffset);

        introFont.setColor(0.98f, 0.98f, 0.98f, 1f);
        introFont.draw(batch, text, x, y);
        introFont.setColor(Color.WHITE);
    }
}
