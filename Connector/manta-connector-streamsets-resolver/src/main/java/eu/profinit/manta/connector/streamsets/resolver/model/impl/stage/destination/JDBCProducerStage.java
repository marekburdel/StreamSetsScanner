package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IColumnNameComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.EOperation;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IJDBCProducerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.ColumnNameComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class JDBCProducerStage extends Stage implements IJDBCProducerStage {

    private String jdbcUrl;
    private String username;
    private String schema;
    private String table;
    private List<ColumnNameComponent> columnNamesComponents;
    private EOperation operation;

    public JDBCProducerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String jdbcUrl, String username, String schema,
            String table, List<ColumnNameComponent> columnNamesComponents, EOperation operation) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.schema = schema;
        this.table = table;
        this.columnNamesComponents = columnNamesComponents;
        this.operation = operation;
    }

    @Override public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override public String getUsername() {
        return username;
    }

    @Override public String getSchema() {
        return schema;
    }

    @Override public String getTable() {
        return table;
    }

    @Override public List<? extends IColumnNameComponent> getColumnNamesComponents() {
        return columnNamesComponents;
    }

    @Override public EOperation getOperation() {
        return operation;
    }

}
