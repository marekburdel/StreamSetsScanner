package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IColumnNameComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public interface IJDBCProducerStage extends IStage {

    /**
     *
     * @return JDBC connection string
     */
    String getJdbcUrl();

    /**
     *
     * @return username
     */
    String getUsername();

    /**
     *
     * @return schema name
     */
    String getSchema();

    /**
     *
     * @return table name
     */
    String getTable();

    /**
     *
     * @return Optionally specified additional field mappings when input field name and column name didn't match.
     */
    List<? extends IColumnNameComponent> getColumnNamesComponents();

    /**
     *
     * @return operation to perform
     */
    EOperation getOperation();

}
