package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;

/**
 * The Field Splitter splits string data based on a regular expression and passes the separated data to new fields.
 * Stage splits data from a single field into multiple fields.
 *
 * @author mburdel
 */
public interface IFieldSplitterStage extends IStage {

    /**
     *
     * @return source field to split
     */
    String getField();

    /**
     *
     * @return split separator
     */
    String getSeparator();

    /**
     *
     * @return multiple fields
     */
    List<String> getFieldsForSplits();

    /**
     *
     * @return type of action that decides what to do with more split values
     */
    ETooManySplitsAction getTooManySplitAction();

    /**
     *
     * @return list field used to store any remaining splits
     */
    String getFieldForRemainingSplits();

    /**
     *
     * @return field action that decides what to do with original field
     */
    EFieldAction getFieldAction();
}
