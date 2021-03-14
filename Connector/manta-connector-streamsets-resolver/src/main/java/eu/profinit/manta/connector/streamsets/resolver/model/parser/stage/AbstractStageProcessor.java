package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.parser.IStageProcessor;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.StageUIInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base for stage processors.
 *
 * @author mburdel
 */
public abstract class AbstractStageProcessor implements IStageProcessor {

    public IStage process(JSONObject jsonStage) {
        return processStage((String) jsonStage.get("instanceName"), (String) jsonStage.get("stageName"),
                processStageUIInfo((JSONObject) jsonStage.get("uiInfo")),
                processLanes((JSONArray) jsonStage.get("inputLanes")),
                processLanes((JSONArray) jsonStage.get("outputLanes")),
                processLanes((JSONArray) jsonStage.get("eventLanes")), (JSONArray) jsonStage.get("configuration"));
    }

    protected abstract IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration);

    protected abstract void processConfiguration(JSONArray jsonConfiguration);

    /**
     *
     * @param jsonObject configuration name
     * @return String value
     */
    protected String getStringName(Object jsonObject) {
        return (String) ((JSONObject) jsonObject).get("name");
    }

    /**
     *
     * @param jsonObject configuration value
     * @return JSONObject value
     */
    protected Object getValue(Object jsonObject) {
        return ((JSONObject) jsonObject).get("value");
    }

    /**
     * Method for process list of fields in components
     *
     * @param value JSONArray
     * @return List of Strings
     */
    protected List<String> processFields(JSONArray value) {
        List<String> fields = new ArrayList<>();
        for (Object jsonObject : value) {
            fields.add((String) jsonObject);
        }
        return fields;
    }

    private IStageUIInfo processStageUIInfo(JSONObject jsonStageUIInfo) {
        return new StageUIInfo((String) jsonStageUIInfo.get("label"),
                StageUIInfo.EStageType.valueOf((String) jsonStageUIInfo.get("stageType")));
    }

    private List<String> processLanes(JSONArray jsonLanes) {
        List<String> lanes = new ArrayList<>();
        for (Object jsonLane : jsonLanes) {
            lanes.add((String) jsonLane);
        }
        return lanes;
    }
}
