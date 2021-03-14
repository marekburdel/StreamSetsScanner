package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.ReplaceRuleComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldReplacerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldReplacerProcessor extends AbstractStageProcessor {
    private List<ReplaceRuleComponent> replaceRules = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "conf.rules":
                processReplaceRules((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processReplaceRules(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            boolean setToNull = (boolean) json.get("setToNull");
            if (setToNull) {
                replaceRules.add(new ReplaceRuleComponent(true, (String) json.get("fields")));
            } else {
                replaceRules.add(new ReplaceRuleComponent(false, (String) json.get("fields"),
                        (String) json.get("replacement")));
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldReplacerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                replaceRules);
    }
}
