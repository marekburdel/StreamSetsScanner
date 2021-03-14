package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldPivoterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class FieldPivoterStage extends Stage implements IFieldPivoterStage {

    private String fieldToPivot;
    private boolean copyFields;
    private String pivotedItemsPath;
    private boolean saveOriginalFieldName;
    private String originalFieldNamePath;

    public FieldPivoterStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String fieldToPivot, boolean copyFields,
            String pivotedItemsPath, boolean saveOriginalFieldName, String originalFieldNamePath) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.fieldToPivot = fieldToPivot;
        this.copyFields = copyFields;
        this.pivotedItemsPath = pivotedItemsPath;
        this.saveOriginalFieldName = saveOriginalFieldName;
        this.originalFieldNamePath = originalFieldNamePath;
    }

    @Override public String getFieldToPivot() {
        return fieldToPivot;
    }

    @Override public boolean getCopyFields() {
        return copyFields;
    }

    @Override public String getPivotedItemsPath() {
        return pivotedItemsPath;
    }

    @Override public boolean getSaveOriginalFieldName() {
        return saveOriginalFieldName;
    }

    @Override public String getOriginalFieldNamePath() {
        return originalFieldNamePath;
    }
}
