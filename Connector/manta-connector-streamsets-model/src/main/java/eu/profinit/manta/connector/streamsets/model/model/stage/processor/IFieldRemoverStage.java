package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;

/**
 * The Field Remover analyzer removes fields from records.
 *
 * @author mburdel
 */
public interface IFieldRemoverStage extends IStage {

    /**
     *
     * @return the action for the Field Remover analyzer to take with those fields
     */
    EFilterOperation getFilterOperation();

    /**
     *
     * @return a list of fields name
     */
    List<String> getFields();

    /**
     *
     * @return field value that results in removal of the field
     */
    String getConstant();
}
