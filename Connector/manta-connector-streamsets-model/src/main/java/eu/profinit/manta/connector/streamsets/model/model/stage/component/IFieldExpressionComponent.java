package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * Component of Expression Evaluator Stage
 *
 * @author mburdel
 */
public interface IFieldExpressionComponent extends IStageComponent {

    /**
     * Method returns field from Expression Evaluator Stage
     *
     * @return field string value
     */
    String getFieldToSet();

    /**
     * Method returns expression used on field in Expression Evaluator
     *
     * @return expression string
     */
    String getExpression();
}
