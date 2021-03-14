package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.LanePredicateComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.StreamSelectorStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class StreamSelectorProcessor extends AbstractStageProcessor {
    private List<LanePredicateComponent> lanePredicates = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "lanePredicates":
                processLanePredicates((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processLanePredicates(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            lanePredicates
                    .add(new LanePredicateComponent((String) json.get("outputLane"), (String) json.get("predicate")));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new StreamSelectorStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                lanePredicates);
    }
}
