package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.ITrashStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class TrashStage extends Stage implements ITrashStage {

    public TrashStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
    }
}