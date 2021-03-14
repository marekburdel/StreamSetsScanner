package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.DataParserStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class DataParserProcessor extends AbstractStageProcessor {

    private String fieldPathToParse;
    private String parsedFieldPath;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "configs.fieldPathToParse":
                fieldPathToParse = (String) getValue(jsonObject);
                break;
            case "configs.parsedFieldPath":
                parsedFieldPath = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override protected IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new DataParserStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                fieldPathToParse, parsedFieldPath);
    }
}
