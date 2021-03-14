package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.SchemaTableComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source.OracleCDCClientStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mburdel
 */
public class OracleCDCClientProcessor extends AbstractStageProcessor {

    private String connectionString;

    private String username;

    private List<SchemaTableComponent> schemaTableComponents = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hikariConf.connectionString":
                connectionString = (String) getValue(jsonObject);
                break;
            case "hikariConf.username":
                username = (String) getValue(jsonObject);
                break;
            case "oracleCDCConfigBean.baseConfigBean.schemaTableConfigs":
                processSchemaTableConfigs((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    @Override protected IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new OracleCDCClientStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                connectionString, username, schemaTableComponents);
    }

    private void processSchemaTableConfigs(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            schemaTableComponents.add(new SchemaTableComponent((String) json.get("schema"), (String) json.get("table"),
                    (String) json.get("excludePattern")));
        }
    }
}
