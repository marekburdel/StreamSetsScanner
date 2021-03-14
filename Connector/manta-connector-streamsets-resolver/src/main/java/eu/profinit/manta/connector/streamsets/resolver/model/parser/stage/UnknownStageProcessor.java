package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import org.json.simple.JSONArray;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class UnknownStageProcessor extends AbstractStageProcessor {

    @Override protected IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        return new Stage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
    }

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        // nothing to process
    }
}
