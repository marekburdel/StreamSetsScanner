package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 *
 * @author mburdel
 */
public interface IDataParserStage extends IStage {

    /**
     *
     * @return field's path to parse
     */
    String getFieldPathToParse();

    /**
     *
     * @return parsed field's path
     */
    String getParsedFieldPath();

}
