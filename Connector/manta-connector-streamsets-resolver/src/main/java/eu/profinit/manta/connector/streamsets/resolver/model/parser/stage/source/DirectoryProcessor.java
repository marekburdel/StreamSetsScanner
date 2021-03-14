package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source.DirectoryStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class DirectoryProcessor extends AbstractStageProcessor {

    private String directoryPath;

    private String filePattern;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "conf.spoolDir":
                directoryPath = (String) getValue(jsonObject);
                continue;
            case "conf.filePattern":
                filePattern = (String) getValue(jsonObject);
                continue;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new DirectoryStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                directoryPath, filePattern);
    }
}
