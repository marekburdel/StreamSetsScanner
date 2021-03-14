package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.EOperation;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.ColumnNameComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.JDBCProducerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mburdel
 */
public class JDBCProducerProcessor extends AbstractStageProcessor {

    private String jdbcUrl;
    private String username;
    private String schema;
    private String table;
    private List<ColumnNameComponent> columnNamesComponents = new ArrayList<>();
    private EOperation operation;

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new JDBCProducerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes, jdbcUrl,
                username, schema, table, columnNamesComponents, operation);
    }

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "hikariConfigBean.connectionString":
                jdbcUrl = (String) getValue(jsonObject);
                break;
            case "hikariConfigBean.username":
                username = (String) getValue(jsonObject);
                break;
            case "schema":
                schema = (String) getValue(jsonObject);
                break;
            case "tableNameTemplate":
                table = (String) getValue(jsonObject);
                break;
            case "columnNames":
                processColumnNames((JSONArray) getValue(jsonObject));
                break;
            case "defaultOperation":
                operation = EOperation.valueOf((String) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    private void processColumnNames(JSONArray value) {
        for (Object jsonObject : value) {
            JSONObject json = (JSONObject) jsonObject;
            columnNamesComponents
                    .add(new ColumnNameComponent((String) json.get("paramValue"), (String) json.get("dataType"),
                            (String) json.get("columnName"), (String) json.get("field"),
                            (String) json.get("defaultValue")));
        }
    }

}
