package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHeaderAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IExpressionEvaluatorStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldAttributeExpressionComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldExpressionComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.HeaderAttributeExpressionComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class ExpressionEvaluatorStage extends Stage implements IExpressionEvaluatorStage {

    private List<FieldExpressionComponent> fieldExpressions;
    private List<HeaderAttributeExpressionComponent> headerAttributesExpressions;
    private List<FieldAttributeExpressionComponent> fieldAttributesExpressions;

    public ExpressionEvaluatorStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes,
            List<FieldExpressionComponent> fieldExpressions,
            List<HeaderAttributeExpressionComponent> headerAttributesExpressions,
            List<FieldAttributeExpressionComponent> fieldAttributesExpressions) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.fieldExpressions = fieldExpressions;
        this.headerAttributesExpressions = headerAttributesExpressions;
        this.fieldAttributesExpressions = fieldAttributesExpressions;
    }

    @Override public List<? extends IFieldExpressionComponent> getFieldExpressions() {
        return fieldExpressions;
    }

    @Override public List<? extends IHeaderAttributeExpressionComponent> getHeaderAttributeExpressions() {
        return headerAttributesExpressions;
    }

    @Override public List<? extends IFieldAttributeExpressionComponent> getFieldAttributeExpressions() {
        return fieldAttributesExpressions;
    }
}
