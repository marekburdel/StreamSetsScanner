package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface ISchemaGeneratorStage extends IStage {

    /**
     *
     * @return schema name
     */
    String getSchemaName();

    /**
     *
     * @return header attribute name
     */
    String getAttributeName();
}
