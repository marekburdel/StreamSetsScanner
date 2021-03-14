package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.TrashStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class TrashProcessor extends AbstractStageProcessor {

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        // nothing to process
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        return new TrashStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
    }
}
