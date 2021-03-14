package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.HadoopFSStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class HadoopFSProcessor extends AbstractStageProcessor {
    private String uniquePrefix;
    private String fileNameSuffix;
    private String hdfsUri;
    private String directoryTemplate;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hdfsTargetConfigBean.uniquePrefix":
                uniquePrefix = (String) getValue(jsonObject);
                break;
            case "hdfsTargetConfigBean.fileNameSuffix":
                fileNameSuffix = (String) getValue(jsonObject);
                break;
            case "hdfsTargetConfigBean.hdfsUri":
                hdfsUri = (String) getValue(jsonObject);
                break;
            case "hdfsTargetConfigBean.dirPathTemplate":
                directoryTemplate = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new HadoopFSStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                uniquePrefix, fileNameSuffix, hdfsUri, directoryTemplate);
    }
}
