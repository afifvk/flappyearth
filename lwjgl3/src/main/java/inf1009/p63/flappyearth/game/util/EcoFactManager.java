package inf1009.p63.flappyearth.game.util;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EcoFactManager {
    private final List<String> ecoFacts;
    private final Random random;

    public EcoFactManager(String filePath) {
        ecoFacts = new ArrayList<>();
        random = new Random();
        loadFacts(filePath);
    }

    private void loadFacts(String filePath) {
        try {
            String factsText = Gdx.files.internal(filePath).readString("UTF-8");
            String[] lines = factsText.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) ecoFacts.add(trimmed);
            }
        } catch (Exception e) {
            // Fallback for legacy path usage.
            try {
                String factsText = Gdx.files.internal("assets/eco_facts.txt").readString("UTF-8");
                String[] lines = factsText.split("\n");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) ecoFacts.add(trimmed);
                }
            } catch (Exception ignored) {
                Gdx.app.error("EcoFactManager", "Unable to load eco facts from '" + filePath + "' or 'assets/eco_facts.txt'.", e);
            }
        }
    }

    public String getRandomFact() {
        if (ecoFacts.isEmpty()) return "Eco fact unavailable.";
        return ecoFacts.get(random.nextInt(ecoFacts.size()));
    }

    public List<String> getAllFacts() {
        return Collections.unmodifiableList(ecoFacts);
    }
}
