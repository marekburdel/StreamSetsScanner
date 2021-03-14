package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IReplaceRuleComponent;

/**
 * @author mburdel
 */
public class ReplaceRuleComponent implements IReplaceRuleComponent {

    private boolean setToNull;
    private String field;
    private String replacement;

    public ReplaceRuleComponent(boolean setToNull, String field) {
        this.setToNull = setToNull;
        this.field = field;
    }

    public ReplaceRuleComponent(boolean setToNull, String fields, String replacement) {
        this.setToNull = setToNull;
        this.field = fields;
        this.replacement = replacement;
    }

    @Override public boolean getSetToNull() {
        return setToNull;
    }

    @Override public String getField() {
        return field;
    }

    @Override public String getReplacement() {
        return replacement;
    }
}
