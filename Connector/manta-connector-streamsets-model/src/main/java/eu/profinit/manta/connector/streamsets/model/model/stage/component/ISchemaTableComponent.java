package eu.profinit.manta.connector.streamsets.model.model.stage.component;

/**
 *
 * @author mburdel
 */
public interface ISchemaTableComponent {

    /**
     *
     * @return schema name
     */
    String getSchemaName();

    /**
     *
     * @return table name
     */
    String getTableName();

    /**
     *
     * @return pattern of the table name to exclude from being read
     */
    String getExcludePattern();
}
