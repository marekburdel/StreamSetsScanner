package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldAction;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.ETooManySplitsAction;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldSplitterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldSplitterStage extends Stage implements IFieldSplitterStage {

    private String fieldPath;
    private String separator;
    private List<String> fieldPathsForSplits;
    private ETooManySplitsAction tooManySplitsAction;
    private String remainingSplitsPath;
    private EFieldAction fieldAction;

    public FieldSplitterStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String fieldPath, String separator,
            List<String> fieldPathsForSplits, ETooManySplitsAction tooManySplitsAction, String remainingSplitsPath,
            EFieldAction fieldAction) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.fieldPath = fieldPath;
        this.separator = separator;
        this.fieldPathsForSplits = fieldPathsForSplits;
        this.tooManySplitsAction = tooManySplitsAction;
        this.remainingSplitsPath = remainingSplitsPath;
        this.fieldAction = fieldAction;
    }

    @Override public String getField() {
        return fieldPath;
    }

    @Override public String getSeparator() {
        return separator;
    }

    @Override public List<String> getFieldsForSplits() {
        return fieldPathsForSplits;
    }

    @Override public ETooManySplitsAction getTooManySplitAction() {
        return tooManySplitsAction;
    }

    @Override public String getFieldForRemainingSplits() {
        return remainingSplitsPath;
    }

    @Override public EFieldAction getFieldAction() {
        return fieldAction;
    }
}
