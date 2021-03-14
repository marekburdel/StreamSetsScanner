package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldExpressionComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IExpressionEvaluatorStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.*;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.generator.streamsets.helper.data.object.FieldExpression;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.Dependence;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import eu.profinit.manta.dataflow.model.AttributeNames;
import eu.profinit.manta.dataflow.model.Edge;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class ExpressionEvaluatorAnalyzer extends AbstractStageAnalyzer<IExpressionEvaluatorStage> {

    @Override public void analyzeFields(IExpressionEvaluatorStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeFieldExpressions(stage, ctx);
    }

    @Override public void createNodes(IExpressionEvaluatorStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();

        Node stageNode = gh.getNode(stage);

        int expressionNumber = 0;
        for (Field expressionField : setting.getInnerFields()) {
            Node expressionNode;
            if (expressionNumber == 0) {
                expressionNode = gh.addNode(Constants.INPUT, NodeType.STREAMSETS_GROUP_FIELD, stageNode);
            } else {
                expressionNode = gh
                        .addNode(Constants.EXPRESSION + expressionNumber, NodeType.STREAMSETS_GROUP_FIELD, stageNode);
            }
            expressionField.createNodes(expressionNode, gh);
            ++expressionNumber;
        }

        createFieldAttributes(stage, ctx);
    }

    /**
     * Method analyzes field expressions. Every new output field is added to previousFieldExpressions container.
     * When currently analyzing field depends on previous field expression(s), then for previous field expression(s)
     * is created new root field (new field group).
     * @param stage stage
     * @param ctx pipeline's context
     */
    private void analyzeFieldExpressions(IExpressionEvaluatorStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        ExpressionLanguageHelper helper = getParserService().getExpressionLanguageHelper();

        Field currentField;
        Field previousField = setting.getInputRootField();
        List<? extends IFieldExpressionComponent> expressionComponents = stage.getFieldExpressions();

        List<FieldExpression> previousFieldExpressions = new ArrayList<>();

        int expressionComponentsSize = expressionComponents.size();

        for (int expressionNumber = 0; expressionNumber < expressionComponents.size(); ++expressionNumber) {

            List<Dependence> currentDependencies = helper
                    .getDependenciesFromEL(expressionComponents.get(expressionNumber).getExpression());
            IFieldExpressionComponent currentExpressionComponent = expressionComponents.get(expressionNumber);
            FieldExpression currentFieldExpression = new FieldExpression(currentExpressionComponent.getFieldToSet(),
                    currentDependencies, currentExpressionComponent.getExpression());

            if (currentDependenciesDependsOnPreviousExpressionComponents(currentDependencies,
                    previousFieldExpressions)) {
                currentField = new Field("/", EFieldType.IO, getParserService());
                setting.addInnerField(currentField);

                setting.addOtherFieldBinding(new Binding(previousField, currentField, EFieldBindingMode.MERGE));
                setting.addFieldFlow(new FieldFlow(previousField, currentField, Edge.Type.DIRECT));

                analyzePreviousFieldExpressions(stage, currentField, previousField, previousFieldExpressions, ctx);

                previousField = currentField;
                previousFieldExpressions.clear();
            }
            previousFieldExpressions.add(currentFieldExpression);
            // if stage's configuration has next expression component, it creates last field group
            if (!hasNextExpressionComponent(expressionNumber, expressionComponentsSize)) {
                currentField = new Field("/", EFieldType.IO, getParserService());
                setting.addInnerField(currentField);

                setting.addOtherFieldBinding(new Binding(previousField, currentField, EFieldBindingMode.MERGE));
                setting.addFieldFlow(new FieldFlow(previousField, currentField, Edge.Type.DIRECT));

                analyzePreviousFieldExpressions(stage, currentField, previousField, previousFieldExpressions, ctx);
            }
        }

    }

    /**
     * Method adds all previous field expression to current field. If in previous field expressions
     * are found fields from EL resolving, method adds them into previous field.
     * @param stage stage
     * @param currentField current root field
     * @param previousField previous root field
     * @param previousFieldExpressions previous field expression
     * @param ctx pipeline's context
     */
    private void analyzePreviousFieldExpressions(IExpressionEvaluatorStage stage, Field currentField,
            Field previousField, List<FieldExpression> previousFieldExpressions, PipelineContext ctx) {
        for (FieldExpression fieldExpression : previousFieldExpressions) {
            analyzeFieldExpression(stage, currentField, previousField, fieldExpression, ctx);
        }
    }

    private void analyzeFieldExpression(IExpressionEvaluatorStage stage, Field currentField, Field previousField,
            FieldExpression fieldExpression, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        Field outputField;
        List<Dependence> dependencies = fieldExpression.getDependencies();
        if (dependencies.isEmpty()) {
            outputField = currentField.add(fieldExpression.getFieldPath(), EFieldType.BEGIN);
            setting.addFieldAttribute(
                    new FieldAttribute(outputField, AttributeNames.NODE_EXPRESSION, fieldExpression.getExpression()));
        }
        for (Dependence dependence : dependencies) {
            if (!fieldExpression.getFieldPath().equals("/") || !dependence.getFieldPath().equals("/")) {
                outputField = currentField.add(fieldExpression.getFieldPath(), EFieldType.BEGIN);
                setting.addFieldAttribute(new FieldAttribute(outputField, AttributeNames.NODE_EXPRESSION,
                        fieldExpression.getExpression()));
                Field dependenceField = previousField.add(dependence.getFieldPath(), EFieldType.IO);
                setting.addOtherFieldBinding(new Binding(dependenceField, outputField, EFieldBindingMode.MERGE_EL));
                setting.addFieldFlow(new FieldFlow(dependenceField, outputField, dependence.getFlowType()));
            }
        }
    }

    /**
     *
     * @param currentDependencies current dependencies from single field expression
     * @param previousFieldExpressions previous dependencies from all field expressions that were analyzed before
     * @return <code>true</code> if current dependencies contain previously created output field path in same stage
     * else <code>false</code>
     */
    private boolean currentDependenciesDependsOnPreviousExpressionComponents(List<Dependence> currentDependencies,
            List<FieldExpression> previousFieldExpressions) {
        for (FieldExpression previousFieldExpression : previousFieldExpressions) {
            String previousOutputFieldPath = previousFieldExpression.getFieldPath();
            for (Dependence currentDependence : currentDependencies) {
                if (StringUtils.contains(currentDependence.getFieldPath(), previousOutputFieldPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param expressionNumber expression number
     * @param expressionComponentsSize expression component size
     * @return <code>true</code> if stage's expression component has next expression to resolve else <code>false</code>
     */
    private boolean hasNextExpressionComponent(int expressionNumber, int expressionComponentsSize) {
        return (expressionNumber + 1) < expressionComponentsSize;
    }

}


