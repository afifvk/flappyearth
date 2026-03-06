package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.game.events.FunFactRequestedEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;

import java.util.HashMap;
import java.util.Map;

public class EcoFactPopupManager {

    private static final float POPUP_DURATION = 4f;

    private final BitmapFont   font = new BitmapFont();
    private final GlyphLayout  layout = new GlyphLayout();
    private final Map<String, String> facts = new HashMap<>();

    private String  currentFact   = null;
    private float   popupTimer    = 0f;

    public EcoFactPopupManager(EventManager eventManager) {
        // Initialize environmental facts
        facts.put("industrial_pollution",   "Factories emit 30% of global CO2. Support clean energy!");
        facts.put("ocean_plastic",          "8M tonnes of plastic enter oceans every year. Reduce, reuse, recycle!");
        facts.put("oil_pollution",          "A single litre of oil can contaminate 1M litres of drinking water.");
        facts.put("vehicle_emissions",      "Transport accounts for 24% of global CO2. Walk or cycle when you can!");
        facts.put("recycling_facts",        "Recycling one aluminium can saves enough energy to power a TV for 3 hrs.");
        facts.put("solar_energy_facts",     "Solar panels can cut household carbon emissions by up to 1 tonne/yr.");
        facts.put("deforestation_facts",    "1 tree absorbs ~22 kg of CO2 per year. Plant more trees!");
        facts.put("single_use_plastic_facts","Single-use plastics take 400+ years to decompose.");

        // Listen for fact requests and display them
        eventManager.subscribe(GameEvents.FACT_REQUESTED, data -> {
            if (!(data instanceof FunFactRequestedEvent)) return;
            String topic = ((FunFactRequestedEvent) data).topic;
            String fact  = facts.getOrDefault(topic, "Every small action for the planet counts!");
            showFact(fact);
        });
    }

    private void showFact(String fact) {
        currentFact = fact;
        popupTimer  = POPUP_DURATION;
    }

    public void update(float delta) {
        if (popupTimer > 0) {
            popupTimer -= delta;
            if (popupTimer <= 0) currentFact = null;
        }
    }

    public void render(SpriteBatch batch, float screenW, float screenH) {
        if (currentFact == null) return;
        layout.setText(font, currentFact);
        float x = (screenW - layout.width) / 2f;
        font.draw(batch, layout, x, 60f);
    }

    public void dispose() {
        if (font != null) font.dispose();
    }
}
