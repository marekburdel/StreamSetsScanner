package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ISchemaTableComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public interface IOracleCDCClientStage extends IStage {

    /**
     *
     * @return JDBC connection string
     */
    String getJdbcConnectionString();

    /**
     *
     * @return username
     */
    String getUsername();

    /**
     *
     * @return Tables to track
     */
    List<? extends ISchemaTableComponent> getSchemaTableComponents();
}
