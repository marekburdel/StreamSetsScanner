package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IHiveMetadataStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class HiveMetadataStage extends Stage implements IHiveMetadataStage {

    private String hiveJDBCUrl;
    private String username;
    private String dbNameEL;
    private String tableNameEL;
    private boolean useCredentials;

    public HiveMetadataStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String hiveJDBCUrl, String username, String dbNameEL,
            String tableNameEL, boolean useCredentials) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.hiveJDBCUrl = hiveJDBCUrl;
        this.username = username;
        this.dbNameEL = dbNameEL;
        this.tableNameEL = tableNameEL;
        this.useCredentials = useCredentials;
    }

    @Override public String getHiveJDBCUrl() {
        return hiveJDBCUrl;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public String getDbNameEL() {
        return dbNameEL;
    }

    @Override public String getTableNameEL() {
        return tableNameEL;
    }

    @Override public boolean getUseCredentials() {
        return useCredentials;
    }
}
