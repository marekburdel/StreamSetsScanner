package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHeaderAttributeExpressionComponent;

/**
 * @author mburdel
 */
public class HeaderAttributeExpressionComponent implements IHeaderAttributeExpressionComponent {

    private String attributeToSet;
    private String headerAttributeExpression;

    public HeaderAttributeExpressionComponent(String attributeToSet, String expressionLanguage) {
        this.attributeToSet = attributeToSet;
        this.headerAttributeExpression = expressionLanguage;
    }

    @Override public String getAttributeToSet() {
        return attributeToSet;
    }

    @Override public String getHeaderAttributeExpression() {
        return headerAttributeExpression;
    }
}
