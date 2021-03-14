package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IJDBCQueryConsumerStage extends IStage {

    /**
     *
     * @return JDBC connection string
     */
    String getJdbcConnectionString();

    /**
     *
     * @return SQL Query
     */
    String getSqlQuery();

    /**
     *
     * @return use credentials
     */
    boolean getUseCredentials();

    /**
     *
     * @return username
     */
    String getUsername();
}
