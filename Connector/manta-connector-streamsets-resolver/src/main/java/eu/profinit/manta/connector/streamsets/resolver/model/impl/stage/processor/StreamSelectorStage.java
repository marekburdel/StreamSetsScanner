package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ILanePredicateComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IStreamSelectorStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.LanePredicateComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class StreamSelectorStage extends Stage implements IStreamSelectorStage {

    private List<LanePredicateComponent> lanePredicates;

    public StreamSelectorStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, List<LanePredicateComponent> lanePredicates) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.lanePredicates = lanePredicates;
    }

    @Override public List<? extends ILanePredicateComponent> getLanePredicates() {
        return lanePredicates;
    }
}
