package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;

/**
 * @author mburdel
 */
public interface IFieldOrderStage extends IStage {

    /**
     *
     * @return ordered fields
     */
    List<String> getOrderFields();

    /**
     *
     * @return extra field action
     */
    EExtraFieldAction getExtraFieldAction();

    /**
     *
     * @return fields to discard
     */
    List<String> getDiscardFields();

    enum EExtraFieldAction {
        DISCARD, TO_ERROR
    }
}
