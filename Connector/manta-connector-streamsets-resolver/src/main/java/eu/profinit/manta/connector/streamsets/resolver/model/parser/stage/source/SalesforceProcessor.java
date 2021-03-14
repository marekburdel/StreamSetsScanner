package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source.SalesforceStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class SalesforceProcessor extends AbstractStageProcessor {

    private String soqlQuery;
    private String username;
    private String authEndpoint;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "forceConfig.soqlQuery":
                soqlQuery = (String) getValue(jsonObject);
                break;
            case "forceConfig.username":
                username = (String) getValue(jsonObject);
                break;
            case "forceConfig.authEndpoint":
                authEndpoint = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new SalesforceStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes, soqlQuery,
                username, authEndpoint);
    }
}
