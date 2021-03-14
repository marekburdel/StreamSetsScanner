package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IHiveMetadataStage extends IStage {

    /**
     *
     * @return Hive JDBC Url
     */
    String getHiveJDBCUrl();

    /**
     *
     * @return username (if credentials are used)
     */
    String getUsername();

    /**
     *
     * @return Database name expressed with EL
     */
    String getDbNameEL();

    /**
     *
     * @return Table name expressed with EL
     */
    String getTableNameEL();

    /**
     *
     * @return use credentials
     */
    boolean getUseCredentials();
}
