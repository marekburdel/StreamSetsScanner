package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * @author mburdel
 */
public interface IReplaceRuleComponent extends IStageComponent {

    /**
     *
     * @return boolean <code>true</code> if the field should be set to null
     */
    boolean getSetToNull();

    /**
     *
     * @return string field's path that should be replaced
     */
    String getField();

    /**
     *
     * @return replacement value for the given field(s)
     */
    String getReplacement();
}
