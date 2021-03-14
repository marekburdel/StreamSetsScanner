package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.RenameMappingComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldRenamerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldRenamerProcessor extends AbstractStageProcessor {
    private List<RenameMappingComponent> renameMappings = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "renameMapping":
                processRenameMappings((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processRenameMappings(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            String fromFieldExpression = (String) json.get("fromFieldExpression");
            String toFieldExpression = (String) json.get("toFieldExpression");
            renameMappings.add(new RenameMappingComponent(fromFieldExpression, toFieldExpression));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldRenamerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                renameMappings);
    }
}
