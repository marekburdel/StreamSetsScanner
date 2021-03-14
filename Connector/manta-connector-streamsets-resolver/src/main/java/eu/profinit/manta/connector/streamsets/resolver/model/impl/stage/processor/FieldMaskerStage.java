package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldMaskComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldMaskerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldMaskComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldMaskerStage extends Stage implements IFieldMaskerStage {

    private List<FieldMaskComponent> fieldMasks;

    public FieldMaskerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, List<FieldMaskComponent> fieldMasks) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.fieldMasks = fieldMasks;
    }

    @Override public List<? extends IFieldMaskComponent> getFieldMasks() {
        return fieldMasks;
    }
}
