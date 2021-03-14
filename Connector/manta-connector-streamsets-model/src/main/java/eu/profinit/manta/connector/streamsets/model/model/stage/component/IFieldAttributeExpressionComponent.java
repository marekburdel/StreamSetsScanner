package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * @author mburdel
 */
public interface IFieldAttributeExpressionComponent extends IStageComponent {

    String getFieldToSet();

    String getAttributeToSet();

    String getFieldAttributeExpression();
}
