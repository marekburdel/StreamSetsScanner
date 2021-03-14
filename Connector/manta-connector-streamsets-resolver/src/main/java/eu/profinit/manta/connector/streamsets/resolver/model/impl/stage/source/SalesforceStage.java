package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.ISalesforceStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class SalesforceStage extends Stage implements ISalesforceStage {

    private String soqlQuery;
    private String username;
    private String authEndpoint;

    public SalesforceStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String soqlQuery, String username, String authEndpoint) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.soqlQuery = soqlQuery;
        this.username = username;
        this.authEndpoint = authEndpoint;
    }

    @Override public String getSoqlQuery() {
        return soqlQuery;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public String getAuthEndpoint() {
        return authEndpoint;
    }

}
