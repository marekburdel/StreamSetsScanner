package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IReplaceRuleComponent;

import java.util.List;

/**
 * The Field Replacer replaces values in fields with nulls or with new values.
 *
 * @author mburdel
 */
public interface IFieldReplacerStage extends IStage {

    /**
     *
     * @return replace rule component
     */
    List<? extends IReplaceRuleComponent> getReplaceRules();
}
