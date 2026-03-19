package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.runtime.BrightnessOverlayRenderer;
import inf1009.p63.flappyearth.game.util.FontUtils;

public class CreditsScene extends Scene {

    private static final float BUTTON_BASE_WIDTH = 420f;
    private static final float BUTTON_BASE_HEIGHT = 118f;
    private static final float BACK_BUTTON_Y_RATIO = 0.06f;

    private static final float PANEL_X_RATIO = 0.18f;
    private static final float PANEL_Y_RATIO = 0.16f;
    private static final float PANEL_W_RATIO = 0.64f;
    private static final float PANEL_H_RATIO = 0.58f;
    private static final float PANEL_PADDING_BASE = 14f;

    private static final float BASE_SCROLL_SPEED = 380f;
    private static final float EXTRA_SCROLL_SPEED = 430f;
    private static final float WHEEL_SCROLL_STEP = 72f;

    private final SceneManager sceneManager;
    private final EngineContext context;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private Texture bgTexture;
    private Texture back1;
    private Texture back2;

    private BitmapFont creditsFont;
    private final GlyphLayout layout;

    private String[] creditsLines;
    private float scrollOffset;
    private float maxScrollOffset;
    private float pendingScrollAmount;
    private InputProcessor previousInputProcessor;
    private InputProcessor mouseWheelInputProcessor;

    public CreditsScene(SceneManager sceneManager, EngineContext context) {
        this.sceneManager = sceneManager;
        this.context = context;

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();

        this.layout = new GlyphLayout();
        this.creditsLines = new String[0];
    }

    @Override
    public void onEnter() {
        bgTexture = context.getAssetManager().get(AssetKeys.CREDITS_PAGE_BG, Texture.class);
        back1 = context.getAssetManager().get("textures/ui/buttons/back_1.png", Texture.class);
        back2 = context.getAssetManager().get("textures/ui/buttons/back_2.png", Texture.class);

        if (creditsFont == null) {
            creditsFont = FontUtils.loadFontOrDefault("fonts/BoldPixels.ttf", 18);
            creditsFont.setUseIntegerPositions(false);
        }

        loadCreditsText();
        scrollOffset = 0f;
        maxScrollOffset = 0f;
        pendingScrollAmount = 0f;

        previousInputProcessor = Gdx.input.getInputProcessor();
        mouseWheelInputProcessor = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (isMouseOverPanel(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())) {
                    pendingScrollAmount += amountY;
                    return true;
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(mouseWheelInputProcessor);
    }

    @Override
    public void update(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float panelW = screenW * PANEL_W_RATIO;
        float panelH = screenH * PANEL_H_RATIO;
        float scale = screenH / 1080f;
        float padding = PANEL_PADDING_BASE * scale;
        float contentW = panelW - (2f * padding);
        float contentH = panelH - (2f * padding);

        maxScrollOffset = Math.max(0f, computeContentHeight(contentW, scale) - contentH);

        float deltaOffset = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            deltaOffset += BASE_SCROLL_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            deltaOffset -= BASE_SCROLL_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
            deltaOffset += EXTRA_SCROLL_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
            deltaOffset -= EXTRA_SCROLL_SPEED * delta;
        }

        deltaOffset += pendingScrollAmount * WHEEL_SCROLL_STEP;
        pendingScrollAmount = 0f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)) {
            scrollOffset = 0f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.END)) {
            scrollOffset = maxScrollOffset;
        }

        scrollOffset = clamp(scrollOffset + deltaOffset, 0f, maxScrollOffset);

        float btnW = BUTTON_BASE_WIDTH * scale;
        float btnH = BUTTON_BASE_HEIGHT * scale;
        float btnX = (screenW - btnW) * 0.5f;
        float btnY = screenH * BACK_BUTTON_Y_RATIO;

        if (isButtonClicked(btnX, btnY, btnW, btnH, screenH) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            sceneManager.switchTo(GameSceneId.MENU.id());
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float scale = screenH / 1080f;
        float panelX = screenW * PANEL_X_RATIO;
        float panelY = screenH * PANEL_Y_RATIO;
        float panelW = screenW * PANEL_W_RATIO;
        float panelH = screenH * PANEL_H_RATIO;
        float padding = PANEL_PADDING_BASE * scale;
        float contentX = panelX + padding;
        float contentY = panelY + padding;
        float contentW = panelW - (2f * padding);
        float contentH = panelH - (2f * padding);

        float btnW = BUTTON_BASE_WIDTH * scale;
        float btnH = BUTTON_BASE_HEIGHT * scale;
        float btnX = (screenW - btnW) * 0.5f;
        float btnY = screenH * BACK_BUTTON_Y_RATIO;

        batch.begin();

        if (bgTexture != null) {
            batch.draw(bgTexture, 0f, 0f, screenW, screenH);
        }

        drawScrollableCredits(contentX, contentY, contentW, contentH, scale);
        drawScrollHint(panelX, panelY, panelW, panelH, scale);
        drawBackButton(btnX, btnY, btnW, btnH, screenH);

        batch.end();

        batch.begin();
        brightnessOverlayRenderer.render(batch, context.getBrightness());
        batch.end();
    }

    @Override
    public void onExit() {
        if (Gdx.input.getInputProcessor() == mouseWheelInputProcessor) {
            Gdx.input.setInputProcessor(previousInputProcessor);
        }
        mouseWheelInputProcessor = null;
        previousInputProcessor = null;
    }

    @Override
    public void disposeResources() {
        if (batch != null) {
            batch.dispose();
        }
        brightnessOverlayRenderer.dispose();
    }

    private void drawScrollableCredits(float textX,
                                       float panelY,
                                       float panelW,
                                       float panelH,
                                       float scale) {
        if (creditsFont == null || creditsLines == null) {
            return;
        }

        Rectangle clipBounds = new Rectangle(textX, panelY, panelW, panelH);
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);

        batch.flush();
        if (!ScissorStack.pushScissors(scissors)) {
            return;
        }

        float lineGap = 12f * scale;
        // Positive scrollOffset should move content upward, revealing lower lines.
        float y = panelY + panelH + scrollOffset;

        for (String line : creditsLines) {
            if (line == null) {
                continue;
            }

            if (line.trim().isEmpty()) {
                y -= lineGap;
                continue;
            }

            creditsFont.setColor(Color.WHITE);
            layout.setText(creditsFont, line, Color.WHITE, panelW, Align.center, true);
            creditsFont.draw(batch, layout, textX, y);
            y -= layout.height + lineGap;
        }

        batch.flush();
        ScissorStack.popScissors();
    }

    private void drawBackButton(float bx, float by, float bw, float bh, float screenH) {
        if (back1 == null || back2 == null) {
            return;
        }

        boolean hovered = isHovered(bx, by, bw, bh, screenH);
        boolean pressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        batch.draw(pressed ? back2 : back1, bx, by, bw, bh);
    }

    private void drawScrollHint(float panelX, float panelY, float panelW, float panelH, float scale) {
        if (creditsFont == null || maxScrollOffset <= 0f) {
            return;
        }

        String hint = "Scroll: Mouse Wheel / W,S / PgUp,PgDn / Home,End";
        creditsFont.setColor(1f, 1f, 1f, 0.88f);
        layout.setText(creditsFont, hint);
        float hintX = panelX + (panelW - layout.width) * 0.5f;
        float hintY = panelY + (26f * scale);
        creditsFont.draw(batch, layout, hintX, hintY);
        creditsFont.setColor(Color.WHITE);
    }

    private float computeContentHeight(float panelW, float scale) {
        if (creditsFont == null || creditsLines == null) {
            return 0f;
        }

        float totalHeight = 0f;
        float lineGap = 12f * scale;

        for (String line : creditsLines) {
            if (line == null || line.trim().isEmpty()) {
                totalHeight += lineGap;
                continue;
            }

            layout.setText(creditsFont, line, Color.WHITE, panelW, Align.center, true);
            totalHeight += layout.height + lineGap;
        }

        return totalHeight;
    }

    private void loadCreditsText() {
        String content;
        try {
            content = Gdx.files.internal("credits.txt").readString("UTF-8");
        } catch (Exception ex) {
            content = "Credits file could not be loaded.";
        }

        creditsLines = content.split("\\r?\\n", -1);
    }

    private boolean isHovered(float bx, float by, float bw, float bh, float screenH) {
        float mx = Gdx.input.getX();
        float my = screenH - Gdx.input.getY();
        return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
    }

    private boolean isButtonClicked(float bx, float by, float bw, float bh, float screenH) {
        return isHovered(bx, by, bw, bh, screenH)
                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    private boolean isMouseOverPanel(float screenW, float screenH) {
        float panelX = screenW * PANEL_X_RATIO;
        float panelY = screenH * PANEL_Y_RATIO;
        float panelW = screenW * PANEL_W_RATIO;
        float panelH = screenH * PANEL_H_RATIO;
        return isHovered(panelX, panelY, panelW, panelH, screenH);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
