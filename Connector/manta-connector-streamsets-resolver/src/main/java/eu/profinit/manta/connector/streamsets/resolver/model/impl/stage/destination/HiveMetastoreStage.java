package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHiveMetastoreStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class HiveMetastoreStage extends Stage implements IHiveMetastoreStage {

    private String hiveJDBCUrl;
    private String username;
    private boolean useCredentials;

    public HiveMetastoreStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String hiveJDBCUrl, String username,
            boolean useCredentials) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.hiveJDBCUrl = hiveJDBCUrl;
        this.username = username;
        this.useCredentials = useCredentials;
    }

    @Override public String getHiveJDBCUrl() {
        return hiveJDBCUrl;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public boolean getUseCredentials() {
        return useCredentials;
    }
}
