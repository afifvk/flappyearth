package inf1009.p63.flappyearth.game.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StagePlan {

    private static final int CHECKPOINT_STEP = 1;

    private final List<StageConfig> stages;
    private final Map<String, Integer> indexBySceneId;

    public StagePlan(List<StageConfig> stages) {
        if (stages == null || stages.isEmpty()) {
            throw new IllegalArgumentException("Stage plan must contain at least one stage");
        }

        this.stages = Collections.unmodifiableList(new ArrayList<>(stages));
        this.indexBySceneId = new HashMap<>();

        for (int i = 0; i < this.stages.size(); i++) {
            StageConfig stage = this.stages.get(i);
            if (indexBySceneId.containsKey(stage.getSceneId())) {
                throw new IllegalArgumentException("Duplicate stage scene ID: " + stage.getSceneId());
            }
            indexBySceneId.put(stage.getSceneId(), i);
        }
    }

    public List<StageConfig> getStages() {
        return stages;
    }

    public String getInitialStageId() {
        return stages.get(0).getSceneId();
    }

    public StageConfig getStageOrThrow(String sceneId) {
        Integer index = indexBySceneId.get(sceneId);
        if (index == null) {
            throw new IllegalArgumentException("Unknown stage ID: " + sceneId);
        }
        return stages.get(index);
    }

    public int getStageIndex(String sceneId) {
        Integer index = indexBySceneId.get(sceneId);
        if (index == null) {
            throw new IllegalArgumentException("Unknown stage ID: " + sceneId);
        }
        return index;
    }

    public boolean isFinalStage(String sceneId) {
        return getStageIndex(sceneId) == stages.size() - 1;
    }

    public String getNextStageId(String currentSceneId) {
        Integer index = indexBySceneId.get(currentSceneId);
        if (index == null) {
            throw new IllegalArgumentException("Unknown stage ID: " + currentSceneId);
        }

        int nextIndex = index + 1;
        if (nextIndex >= stages.size()) {
            return null;
        }
        return stages.get(nextIndex).getSceneId();
    }

    public int getTargetForStage(String sceneId) {
        int transitionCount = Math.max(1, stages.size() - 1);
        int stageIndex = getStageIndex(sceneId);
        int targetStep = Math.min(stageIndex + 1, transitionCount);
        return targetStep * CHECKPOINT_STEP;
    }

    public int getFinalTargetGoodCollectibles() {
        int transitionCount = Math.max(1, stages.size() - 1);
        return transitionCount * CHECKPOINT_STEP;
    }

    public List<Integer> getCheckpointTargets() {
        List<Integer> checkpointTargets = new ArrayList<>();
        int transitionCount = Math.max(1, stages.size() - 1);
        for (int i = 1; i < transitionCount; i++) {
            checkpointTargets.add(i * CHECKPOINT_STEP);
        }
        return checkpointTargets;
    }
}
