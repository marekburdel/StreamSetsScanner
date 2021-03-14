package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;

/**
 * Implementation of UI Stage Information. Contains label(defined stage's name by user) and stage type.
 *
 * @author mburdel
 */
public class StageUIInfo implements IStageUIInfo {

    /** Stage name defined by user(shown in StreamSets UI pipeline) */
    private String label;

    private EStageType stageType;

    public StageUIInfo(String label, EStageType stageType) {
        this.label = label;
        this.stageType = stageType;
    }

    @Override public String getLabel() {
        return label;
    }

    @Override public EStageType getStageType() {
        return stageType;
    }
}
