package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldOrderStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldOrderStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldOrderProcessor extends AbstractStageProcessor {

    private List<String> orderFields = new ArrayList<>();
    private IFieldOrderStage.EExtraFieldAction extraFieldAction;
    private List<String> discardFields = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "config.fields":
                orderFields = processFields((JSONArray) getValue(jsonObject));
                break;
            case "config.extraFieldAction":
                extraFieldAction = IFieldOrderStage.EExtraFieldAction.valueOf((String) getValue(jsonObject));
                break;
            case "config.discardFields":
                discardFields = processFields((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldOrderStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                orderFields, extraFieldAction, discardFields);
    }

}
