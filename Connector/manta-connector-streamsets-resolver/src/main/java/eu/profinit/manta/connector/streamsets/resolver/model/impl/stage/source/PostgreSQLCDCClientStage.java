package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IPostgreSQLCDCClientStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.SchemaTableComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class PostgreSQLCDCClientStage extends Stage implements IPostgreSQLCDCClientStage {

    private String connectionString;

    private String username;

    private List<SchemaTableComponent> schemaTablesComponents;

    public PostgreSQLCDCClientStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, String connectionString,
            String username, List<SchemaTableComponent> schemaTablesComponents) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.connectionString = connectionString;
        this.username = username;
        this.schemaTablesComponents = schemaTablesComponents;
    }

    @Override public String getJdbcConnectionString() {
        return connectionString;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public List<SchemaTableComponent> getSchemaTableComponents() {
        return schemaTablesComponents;
    }

}
