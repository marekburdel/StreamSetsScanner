package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldOrderStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldOrderStage extends Stage implements IFieldOrderStage {

    private List<String> orderFields;
    private EExtraFieldAction extraFieldAction;
    private List<String> discardFields;

    public FieldOrderStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, List<String> orderFields,
            EExtraFieldAction extraFieldAction, List<String> discardFields) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.orderFields = orderFields;
        this.extraFieldAction = extraFieldAction;
        this.discardFields = discardFields;
    }

    @Override public List<String> getOrderFields() {
        return orderFields;
    }

    @Override public EExtraFieldAction getExtraFieldAction() {
        return extraFieldAction;
    }

    @Override public List<String> getDiscardFields() {
        return discardFields;
    }
}
