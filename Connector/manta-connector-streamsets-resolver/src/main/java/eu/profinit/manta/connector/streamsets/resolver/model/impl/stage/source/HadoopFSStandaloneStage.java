package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IHadoopFSStandaloneStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class HadoopFSStandaloneStage extends Stage implements IHadoopFSStandaloneStage {

    private String hdfsUri;
    private String directoryPath;
    private String filePattern;

    public HadoopFSStandaloneStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, String hdfsUri,
            String directoryPath, String filePattern) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.hdfsUri = hdfsUri;
        this.directoryPath = directoryPath;
        this.filePattern = filePattern;
    }

    @Override public String getHdfsUri() {
        return hdfsUri;
    }

    @Override public String getDirectoryPath() {
        return directoryPath;
    }

    @Override public String getFilePattern() {
        return filePattern;
    }

}
