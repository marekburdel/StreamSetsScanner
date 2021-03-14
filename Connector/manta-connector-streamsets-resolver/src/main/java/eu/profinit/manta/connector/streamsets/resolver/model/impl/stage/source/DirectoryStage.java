package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IDirectoryStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class DirectoryStage extends Stage implements IDirectoryStage {

    private String directoryPath;

    private String filePattern;

    public DirectoryStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String directoryPath, String filePattern) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.directoryPath = directoryPath;
        this.filePattern = filePattern;

    }

    @Override public String getDirectoryPath() {
        return directoryPath;
    }

    @Override public String getFilePattern() {
        return filePattern;
    }

}
