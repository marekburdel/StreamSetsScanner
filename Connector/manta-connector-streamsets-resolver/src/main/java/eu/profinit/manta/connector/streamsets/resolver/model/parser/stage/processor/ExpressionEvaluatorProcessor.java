package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldExpressionComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.HeaderAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.ExpressionEvaluatorStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class ExpressionEvaluatorProcessor extends AbstractStageProcessor {
    private List<FieldExpressionComponent> expressionProcessors = new ArrayList<>();
    private List<HeaderAttributeExpressionComponent> headerAttributes = new ArrayList<>();
    private List<FieldAttributeExpressionComponent> fieldAttributes = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "expressionProcessorConfigs":
                processExpressionProcessorConfigs((JSONArray) getValue(jsonObject));
                break;
            case "headerAttributeConfigs":
                processHeaderAttributeConfigs((JSONArray) getValue(jsonObject));
                break;
            case "fieldAttributeConfigs":
                processFieldAttributeConfigs((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processFieldAttributeConfigs(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            fieldAttributes.add(new FieldAttributeExpressionComponent((String) json.get("fieldToSet"),
                    (String) json.get("attributeToSet"), (String) json.get("fieldAttributeExpression")));
        }
    }

    private void processHeaderAttributeConfigs(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            headerAttributes.add(new HeaderAttributeExpressionComponent((String) json.get("attributeToSet"),
                    (String) json.get("headerAttributeExpression")));
        }
    }

    private void processExpressionProcessorConfigs(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            String fieldToSet = (String) json.get("fieldToSet");
            expressionProcessors.add(new FieldExpressionComponent(fieldToSet, (String) json.get("expression")));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new ExpressionEvaluatorStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                expressionProcessors, headerAttributes, fieldAttributes);
    }
}
