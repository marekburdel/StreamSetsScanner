package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IHiveMetastoreStage extends IStage {

    /**
     *
     * @return Hive JDBC Url
     */
    String getHiveJDBCUrl();

    /**
     *
     * @return username
     */
    String getUsername();

    /**
     *
     * @return use credentials
     */
    boolean getUseCredentials();
}
