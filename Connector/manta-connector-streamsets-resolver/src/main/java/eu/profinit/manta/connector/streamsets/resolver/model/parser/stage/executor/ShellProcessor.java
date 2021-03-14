package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.executor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.EnvironmentVariableComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.executor.ShellStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mburdel
 */
public class ShellProcessor extends AbstractStageProcessor {

    private String script;
    private List<EnvironmentVariableComponent> environmentVariableComponents = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "config.script":
                script = (String) getValue(jsonObject);
                break;
            case "config.environmentVariables":
                processEnvironmentVariables((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processEnvironmentVariables(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            environmentVariableComponents
                    .add(new EnvironmentVariableComponent((String) json.get("key"), (String) json.get("value")));
        }
    }

    @Override protected IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new ShellStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes, script,
                environmentVariableComponents);
    }
}
