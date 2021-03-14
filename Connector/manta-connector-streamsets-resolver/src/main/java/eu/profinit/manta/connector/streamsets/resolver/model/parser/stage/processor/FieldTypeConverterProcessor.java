package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EConvertBy;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldTypeConverterDataType;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.WholeTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldTypeConverterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldTypeConverterProcessor extends AbstractStageProcessor {
    private EConvertBy convertBy;
    private List<FieldTypeConverterComponent> fieldTypeConverters = new ArrayList<>();
    private List<WholeTypeConverterComponent> wholeTypeConverters = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "convertBy":
                convertBy = EConvertBy.valueOf((String) getValue(jsonObject));
                break;
            case "fieldTypeConverterConfigs":
                processFieldTypeConverters((JSONArray) getValue(jsonObject));
                break;
            case "wholeTypeConverterConfigs":
                processWholeTypeConverters((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processFieldTypeConverters(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            fieldTypeConverters.add(new FieldTypeConverterComponent(processFields((JSONArray) json.get("fields")),
                    EFieldTypeConverterDataType.valueOf((String) json.get("targetType"))));
        }
    }

    private void processWholeTypeConverters(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            wholeTypeConverters.add(new WholeTypeConverterComponent(
                    EFieldTypeConverterDataType.valueOf((String) json.get("sourceType")),
                    EFieldTypeConverterDataType.valueOf((String) json.get("targetType"))));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldTypeConverterStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                convertBy, fieldTypeConverters, wholeTypeConverters);
    }
}
