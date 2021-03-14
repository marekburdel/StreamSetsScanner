package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IJDBCQueryConsumerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class JDBCQueryConsumerStage extends Stage implements IJDBCQueryConsumerStage {

    private String jdbcConnectionString;
    private String sqlQuery;
    private boolean useCredentials;
    private String username;

    public JDBCQueryConsumerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, String jdbcConnectionString,
            String sqlQuery, boolean useCredentials, String username) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.jdbcConnectionString = jdbcConnectionString;
        this.sqlQuery = sqlQuery;
        this.useCredentials = useCredentials;
        this.username = username;
    }

    @Override public String getJdbcConnectionString() {
        return jdbcConnectionString;
    }

    @Override public String getSqlQuery() {
        return sqlQuery;
    }

    @Override public boolean getUseCredentials() {
        return useCredentials;
    }

    @Override public String getUsername() {
        return username;
    }
}
