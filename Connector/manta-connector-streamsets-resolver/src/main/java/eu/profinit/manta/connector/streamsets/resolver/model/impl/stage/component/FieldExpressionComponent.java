package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldExpressionComponent;

/**
 * @author mburdel
 */
public class FieldExpressionComponent implements IFieldExpressionComponent {

    private String fieldToSet;
    private String expression;

    public FieldExpressionComponent(String fieldToSet, String expressionLanguage) {
        this.fieldToSet = fieldToSet;
        this.expression = expressionLanguage;
    }

    @Override public String getFieldToSet() {
        return fieldToSet;
    }

    @Override public String getExpression() {
        return expression;
    }
}
