package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHTTPHeadersComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHTTPClientStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.HTTPHeadersComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class HTTPClientStage extends Stage implements IHTTPClientStage {

    private String resourceUrl;
    private String username;
    private List<HTTPHeadersComponent> headers;
    private String httpMethod;

    public HTTPClientStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String resourceUrl, String username,
            List<HTTPHeadersComponent> headers, String httpMethod) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.resourceUrl = resourceUrl;
        this.username = username;
        this.headers = headers;
        this.httpMethod = httpMethod;
    }

    @Override public String getResourceUrl() {
        return resourceUrl;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public List<? extends IHTTPHeadersComponent> getHeaders() {
        return headers;
    }

    @Override public String getHttpMethod() {
        return httpMethod;
    }

}
