package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.HTTPHeadersComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.HTTPClientStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class HTTPClientProcessor extends AbstractStageProcessor {

    private String resourceUrl;
    private String username;
    private List<HTTPHeadersComponent> headers = new ArrayList<>();
    private String httpMethod;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "conf.resourceUrl":
                resourceUrl = (String) getValue(jsonObject);
                break;
            case "conf.client.basicAuth.username":
                username = (String) getValue(jsonObject);
                break;
            case "conf.headers":
                processHeaders((JSONArray) getValue(jsonObject));
                break;
            case "conf.httpMethod":
                httpMethod = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    private void processHeaders(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            headers.add(new HTTPHeadersComponent((String) json.get("key"), (String) json.get("value")));
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new HTTPClientStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                resourceUrl, username, headers, httpMethod);
    }
}
