package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.LocalFSStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class LocalFSProcessor extends AbstractStageProcessor {
    private String uniquePrefix;
    private String fileNameSuffix;
    private String dirPathTemplate;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "configs.uniquePrefix":
                uniquePrefix = (String) getValue(jsonObject);
                break;
            case "configs.fileNameSuffix":
                fileNameSuffix = (String) getValue(jsonObject);
                break;
            case "configs.dirPathTemplate":
                dirPathTemplate = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new LocalFSStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes, uniquePrefix,
                fileNameSuffix, dirPathTemplate);
    }
}
