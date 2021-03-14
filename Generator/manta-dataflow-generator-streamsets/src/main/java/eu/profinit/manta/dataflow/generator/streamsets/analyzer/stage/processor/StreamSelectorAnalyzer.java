package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ILanePredicateComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IStreamSelectorStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.Dependence;
import eu.profinit.manta.dataflow.model.Edge;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class StreamSelectorAnalyzer extends AbstractStageAnalyzer<IStreamSelectorStage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamSelectorAnalyzer.class);

    @Override public void analyzeFields(IStreamSelectorStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzePredicates(stage, ctx);
    }

    @Override public void createNodes(IStreamSelectorStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();

        Node stageNode = gh.getNode(stage);
        Node inputNode = gh.addNode(Constants.INPUT, NodeType.STREAMSETS_GROUP_FIELD, stageNode);
        setting.getInputRootField().createNodes(inputNode, gh);

        List<Pair<Field, String>> outputPairs = new ArrayList<>();
        List<? extends ILanePredicateComponent> predicateComponents = stage.getLanePredicates();
        for (int conditionNumber = 1; conditionNumber <= predicateComponents.size(); ++conditionNumber) {
            Field outputConditionField = new Field("/", EFieldType.IO, getParserService());
            setting.getInputRootField().merge(outputConditionField);
            outputPairs.add(new ImmutablePair<>(outputConditionField,
                    predicateComponents.get(conditionNumber - 1).getOutputLane()));

            Node outputNode = gh
                    .addNode(Constants.OUTPUT + conditionNumber, NodeType.STREAMSETS_GROUP_FIELD, stageNode);
            outputConditionField.createNodes(outputNode, gh);
            outputNode.addAttribute(Constants.ATTRIBUTE_CONDITION,
                    predicateComponents.get(conditionNumber - 1).getPredicate());

            setting.addFieldFlow(new FieldFlow(setting.getInputRootField(), outputConditionField, Edge.Type.DIRECT));
        }
        setting.setOutputPairs(outputPairs);
    }

    @Override public void createFlows(IStreamSelectorStage stage, PipelineContext ctx) {
        createOtherStageFlows(stage, ctx);
        createOutputStageFlows(stage, ctx);
    }

    @Override protected void createOutputStageFlows(IStreamSelectorStage stage, PipelineContext ctx) {
        List<Pair<Field, String>> outputPairs = ctx.getSetting(stage).getOutputPairs();

        Field outputField;
        String outputLane;
        for (Pair<Field, String> outputPair : outputPairs) {
            outputField = outputPair.getKey();
            outputLane = outputPair.getValue();

            List<Field> leafs = outputField.getOutputLeafs();
            for (IStage targetStage : ctx.getLanes().get(outputLane).getTargetStages()) {

                Field targetRootField = ctx.getSetting(targetStage).getInputRootField();
                if (targetRootField == null) {
                    LOGGER.error("Target Stage not found while creating output field flows.");
                } else {
                    for (Field leaf : leafs) {
                        Field targetLeaf = targetRootField.findFieldByPath(leaf.getFieldPath());
                        if (targetLeaf != null) {
                            createDirectFlow(leaf, targetLeaf, ctx.getGraphHelper());
                        } else {
                            LOGGER.debug("Target leaf doesn't exist. Source stage analyzer: {}, "
                                            + "Target stage: {}, Leaf path: {}, Leaf type: {}", getClass().getSimpleName(),
                                    targetStage.getStageName(), leaf.getFieldPath(), leaf.getFieldType());
                        }
                    }
                }
            }
        }
    }

    /**
     * Method analyses JSP 2.0 Expression Language in stages' predicates(conditions)
     * @param stage stage
     * @param ctx context
     */
    private void analyzePredicates(IStreamSelectorStage stage, PipelineContext ctx) {
        for (ILanePredicateComponent component : stage.getLanePredicates()) {
            String predicate = component.getPredicate();
            List<Dependence> dependencies = getParserService().getExpressionLanguageHelper()
                    .getDependenciesFromEL(predicate);
            for (Dependence dependence : dependencies) {
                ctx.getSetting(stage).getInputRootField().add(dependence.getFieldPath(), EFieldType.IO);
            }
        }
    }

}
