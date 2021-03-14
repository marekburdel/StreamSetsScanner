package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;

import java.util.List;

/**
 * Implementation of Stage
 *
 * @author mburdel
 */
public class Stage implements IStage {

    private String instanceName;

    private String stageName;

    private IStageUIInfo stageUIInfo;

    private List<String> inputLanes;

    private List<String> outputLanes;

    private List<String> eventLanes;

    public Stage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes) {
        this.instanceName = instanceName;
        this.stageName = stageName;
        this.stageUIInfo = stageUIInfo;
        this.inputLanes = inputLanes;
        this.outputLanes = outputLanes;
        this.eventLanes = eventLanes;
    }

    @Override public String getInstanceName() {
        return instanceName;
    }

    @Override public String getStageName() {
        return stageName;
    }

    @Override public IStageUIInfo getStageUIInfo() {
        return stageUIInfo;
    }

    @Override public List<String> getInputLanes() {
        return inputLanes;
    }

    @Override public List<String> getOutputLanes() {
        return outputLanes;
    }

    @Override public List<String> getEventLanes() {
        return eventLanes;
    }
}
