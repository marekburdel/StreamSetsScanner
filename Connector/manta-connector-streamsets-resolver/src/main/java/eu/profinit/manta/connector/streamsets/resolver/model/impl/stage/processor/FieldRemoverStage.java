package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFilterOperation;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldRemoverStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldRemoverStage extends Stage implements IFieldRemoverStage {

    private EFilterOperation filterOperation;
    private List<String> removeFields;
    private String constant;

    public FieldRemoverStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, EFilterOperation filterOperation,
            List<String> removeFields, String constant) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.filterOperation = filterOperation;
        this.removeFields = removeFields;
        this.constant = constant;
    }

    @Override public EFilterOperation getFilterOperation() {
        return filterOperation;
    }

    @Override public List<String> getFields() {
        return removeFields;
    }

    @Override public String getConstant() {
        return constant;
    }
}
