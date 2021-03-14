package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHadoopFSStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class HadoopFSStage extends Stage implements IHadoopFSStage {

    private String uniquePrefix;
    private String fileNameSuffix;
    private String hdfsUri;
    private String directoryTemplate;

    // additional configuration

    public HadoopFSStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String uniquePrefix, String fileNameSuffix,
            String hdfsUri, String directoryTemplate) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.uniquePrefix = uniquePrefix;
        this.fileNameSuffix = fileNameSuffix;
        this.hdfsUri = hdfsUri;
        this.directoryTemplate = directoryTemplate;
    }

    @Override public String getUniquePrefix() {
        return uniquePrefix;
    }

    @Override public String getFileNameSuffix() {
        return fileNameSuffix;
    }

    @Override public String getHdfsUri() {
        return hdfsUri;
    }

    @Override public String getDirectoryTemplate() {
        return directoryTemplate;
    }

}