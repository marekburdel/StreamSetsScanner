package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldAction;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.ETooManySplitsAction;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldSplitterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class FieldSplitterProcessor extends AbstractStageProcessor {

    private String fieldPath;
    private String separator;
    private List<String> fieldPathsForSplits = new ArrayList<>();
    private ETooManySplitsAction tooManySplitsAction;
    private String remainingSplitsPath;
    private EFieldAction fieldAction;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "fieldPath":
                fieldPath = (String) getValue(jsonObject);
                break;
            case "separator":
                separator = (String) getValue(jsonObject);
                break;
            case "fieldPathsForSplits":
                processFieldPathsForSplits((JSONArray) getValue(jsonObject));
                break;
            case "tooManySplitsAction":
                tooManySplitsAction = ETooManySplitsAction.valueOf((String) getValue(jsonObject));
                break;
            case "remainingSplitsPath":
                remainingSplitsPath = (String) getValue(jsonObject);
                break;
            case "originalFieldAction":
                fieldAction = EFieldAction.valueOf((String) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processFieldPathsForSplits(JSONArray value) {
        for (Object jsonObject : value) {
            fieldPathsForSplits.add((String) jsonObject);
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldSplitterStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                fieldPath, separator, fieldPathsForSplits, tooManySplitsAction, remainingSplitsPath, fieldAction);
    }
}
