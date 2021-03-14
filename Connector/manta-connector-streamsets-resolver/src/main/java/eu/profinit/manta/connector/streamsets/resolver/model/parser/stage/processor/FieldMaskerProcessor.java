package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.EMaskType;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldMaskComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldMaskerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldMaskerProcessor extends AbstractStageProcessor {
    private List<FieldMaskComponent> fieldMasks = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "fieldMaskConfigs":
                processFieldMasks((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processFieldMasks(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            fieldMasks.add(new FieldMaskComponent(processFields((JSONArray) json.get("fields")),
                    EMaskType.valueOf((String) json.get("maskType")), (String) json.get("regex"),
                    (String) json.get("groupsToShow"), (String) json.get("mask")));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldMaskerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                fieldMasks);
    }
}
