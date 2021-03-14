package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.HiveMetadataStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class HiveMetadataProcessor extends AbstractStageProcessor {

    private String hiveJDBCUrl;
    private String username;
    private String dbNameEL;
    private String tableNameEL;
    private boolean useCredentials;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hiveConfigBean.hiveJDBCUrl":
                hiveJDBCUrl = (String) getValue(jsonObject);
                break;
            case "dbNameEL":
                dbNameEL = (String) getValue(jsonObject);
                break;
            case "tableNameEL":
                tableNameEL = (String) getValue(jsonObject);
                break;
            case "hiveConfigBean.username":
                username = (String) getValue(jsonObject);
                break;
            case "hiveConfigBean.useCredentials":
                useCredentials = (boolean) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new HiveMetadataStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                hiveJDBCUrl, username, dbNameEL, tableNameEL, useCredentials);
    }
}
