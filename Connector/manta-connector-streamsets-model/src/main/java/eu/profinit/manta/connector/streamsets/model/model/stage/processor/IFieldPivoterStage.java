package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 *
 * @author mburdel
 */
public interface IFieldPivoterStage extends IStage {

    /**
     *
     * @return field's path to pivot
     */
    String getFieldToPivot();

    /**
     *
     * @return copy other fields
     */
    boolean getCopyFields();

    /**
     *
     * @return create pivoted items path
     */
    String getPivotedItemsPath();

    /**
     *
     * @return save original field's path
     */
    boolean getSaveOriginalFieldName();

    /**
     *
     * @return original field's path
     */
    String getOriginalFieldNamePath();

}
