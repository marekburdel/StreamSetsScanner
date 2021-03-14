package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.InPlaceFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.TargetFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldHasherStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldHasherProcessor extends AbstractStageProcessor {
    private boolean recordHasherConfigHashEntireRecord;
    private EHashType recordHasherConfigHashType;
    private String recordHasherConfigTargetField;

    private List<InPlaceFieldHasherComponent> inPlaceFieldHashers = new ArrayList<>();
    private List<TargetFieldHasherComponent> targetFieldHashers = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hasherConfig.recordHasherConfig.hashEntireRecord":
                recordHasherConfigHashEntireRecord = (boolean) getValue(jsonObject);
                break;
            case "hasherConfig.recordHasherConfig.hashType":
                recordHasherConfigHashType = EHashType.valueOf((String) getValue(jsonObject));
                break;
            case "hasherConfig.recordHasherConfig.targetField":
                recordHasherConfigTargetField = (String) getValue(jsonObject);
                break;
            case "hasherConfig.inPlaceFieldHasherConfigs":
                processInPlaceFieldHashers((JSONArray) getValue(jsonObject));
                break;
            case "hasherConfig.targetFieldHasherConfigs":
                processTargetFieldHashers((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processInPlaceFieldHashers(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            inPlaceFieldHashers
                    .add(new InPlaceFieldHasherComponent(processFields((JSONArray) json.get("sourceFieldsToHash")),
                            EHashType.valueOf((String) json.get("hashType"))));
        }
    }

    private void processTargetFieldHashers(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            targetFieldHashers
                    .add(new TargetFieldHasherComponent(processFields((JSONArray) json.get("sourceFieldsToHash")),
                            EHashType.valueOf((String) json.get("hashType")), (String) json.get("targetField"),
                            (String) json.get("headerAttribute")));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldHasherStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                recordHasherConfigHashEntireRecord, recordHasherConfigHashType, recordHasherConfigTargetField,
                inPlaceFieldHashers, targetFieldHashers);
    }
}
