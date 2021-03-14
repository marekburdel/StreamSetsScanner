package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldHasherStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.InPlaceFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.TargetFieldHasherComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldHasherStage extends Stage implements IFieldHasherStage {

    private boolean recordHasherConfigHashEntireRecord;
    private EHashType recordHasherConfigHashType;
    private String recordHasherConfigTargetField;

    private List<InPlaceFieldHasherComponent> inPlaceFieldHashers;
    private List<TargetFieldHasherComponent> targetFieldHashers;

    public FieldHasherStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, boolean recordHasherConfigHashEntireRecord,
            EHashType recordHasherConfigHashType, String recordHasherConfigTargetField,
            List<InPlaceFieldHasherComponent> inPlaceFieldHashers,
            List<TargetFieldHasherComponent> targetFieldHashers) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.recordHasherConfigHashEntireRecord = recordHasherConfigHashEntireRecord;
        this.recordHasherConfigHashType = recordHasherConfigHashType;
        this.recordHasherConfigTargetField = recordHasherConfigTargetField;
        this.inPlaceFieldHashers = inPlaceFieldHashers;
        this.targetFieldHashers = targetFieldHashers;
    }

    @Override public boolean getHashEntireRecord() {
        return recordHasherConfigHashEntireRecord;
    }

    @Override public String getHashEntireRecordTargetField() {
        return recordHasherConfigTargetField;
    }

    @Override public EHashType getHashEntireRecordHashType() {
        return recordHasherConfigHashType;
    }

    @Override public List<InPlaceFieldHasherComponent> getInPlaceFieldHasherComponents() {
        return inPlaceFieldHashers;
    }

    @Override public List<TargetFieldHasherComponent> getTargetFieldHasherComponents() {
        return targetFieldHashers;
    }
}




