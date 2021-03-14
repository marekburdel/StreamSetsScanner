package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.ILocalFSStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class LocalFSStage extends Stage implements ILocalFSStage {

    private String uniquePrefix;
    private String fileNameSuffix;
    private String dirPathTemplate;

    public LocalFSStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String uniquePrefix, String fileNameSuffix,
            String dirPathTemplate) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.uniquePrefix = uniquePrefix;
        this.fileNameSuffix = fileNameSuffix;
        this.dirPathTemplate = dirPathTemplate;
    }

    @Override public String getUniquePrefix() {
        return uniquePrefix;
    }

    @Override public String getFileNameSuffix() {
        return fileNameSuffix;
    }

    @Override public String getDirPathTemplate() {
        return dirPathTemplate;
    }
}