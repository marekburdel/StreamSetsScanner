package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldPivoterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class FieldPivoterProcessor extends AbstractStageProcessor {

    private String fieldToPivot;
    private boolean copyFields;
    private String pivotedItemsPath;
    private boolean saveOriginalFieldName;
    private String originalFieldNamePath;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "listPath":
                fieldToPivot = (String) getValue(jsonObject);
                break;
            case "copyFields":
                copyFields = (boolean) getValue(jsonObject);
                break;
            case "newPath":
                pivotedItemsPath = (String) getValue(jsonObject);
                break;
            case "saveOriginalFieldName":
                saveOriginalFieldName = (boolean) getValue(jsonObject);
                break;
            case "originalFieldNamePath":
                originalFieldNamePath = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override protected IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldPivoterStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                fieldToPivot, copyFields, pivotedItemsPath, saveOriginalFieldName, originalFieldNamePath);
    }
}
