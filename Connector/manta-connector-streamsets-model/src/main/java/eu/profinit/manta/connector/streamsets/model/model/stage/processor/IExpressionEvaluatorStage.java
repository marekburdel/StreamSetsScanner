package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHeaderAttributeExpressionComponent;

import java.util.List;

/**
 * The Expression Evaluator performs calculations and writes the results to new or existing fields.
 *
 * @author mburdel
 */
public interface IExpressionEvaluatorStage extends IStage {

    /**
     *
     * @return components with output field's path and expression
     */
    List<? extends IFieldExpressionComponent> getFieldExpressions();

    List<? extends IHeaderAttributeExpressionComponent> getHeaderAttributeExpressions();

    List<? extends IFieldAttributeExpressionComponent> getFieldAttributeExpressions();

}
