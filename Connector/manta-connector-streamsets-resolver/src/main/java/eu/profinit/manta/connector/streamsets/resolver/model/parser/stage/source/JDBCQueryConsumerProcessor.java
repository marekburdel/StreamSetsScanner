package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source.JDBCQueryConsumerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class JDBCQueryConsumerProcessor extends AbstractStageProcessor {

    private String jdbcConnectionString;
    private String sqlQuery;
    private boolean useCredentials;
    private String username;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hikariConfigBean.connectionString":
                jdbcConnectionString = (String) getValue(jsonObject);
                break;
            case "query":
                sqlQuery = (String) getValue(jsonObject);
                break;
            case "hikariConfigBean.useCredentials":
                useCredentials = (boolean) getValue(jsonObject);
                break;
            case "hikariConfigBean.username":
                username = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new JDBCQueryConsumerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                jdbcConnectionString, sqlQuery, useCredentials, username);
    }
}
