package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldAttributeExpressionComponent;

/**
 * @author mburdel
 */
public class FieldAttributeExpressionComponent implements IFieldAttributeExpressionComponent {

    private String fieldToSet;
    private String attributeToSet;
    private String fieldAttributeExpression;

    public FieldAttributeExpressionComponent(String fieldToSet, String attributeToSet,
            String fieldAttributeExpression) {
        this.fieldToSet = fieldToSet;
        this.attributeToSet = attributeToSet;
        this.fieldAttributeExpression = fieldAttributeExpression;
    }

    @Override public String getFieldToSet() {
        return fieldToSet;
    }

    @Override public String getAttributeToSet() {
        return attributeToSet;
    }

    @Override public String getFieldAttributeExpression() {
        return fieldAttributeExpression;
    }
}
