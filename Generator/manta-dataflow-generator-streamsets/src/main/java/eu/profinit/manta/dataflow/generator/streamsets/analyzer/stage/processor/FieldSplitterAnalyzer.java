package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldAction;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.ETooManySplitsAction;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldSplitterStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Edge;
import eu.profinit.manta.dataflow.model.Node;

/**
 * @author mburdel
 */
public class FieldSplitterAnalyzer extends AbstractStageAnalyzer<IFieldSplitterStage> {

    @Override public void analyzeFields(IFieldSplitterStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeSplits(stage, ctx);
    }

    private void analyzeSplits(IFieldSplitterStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        Field rootField = setting.getInputRootField();

        Field fieldForSplit;
        if (stage.getFieldAction() == EFieldAction.KEEP) {
            fieldForSplit = rootField.add(stage.getField(), EFieldType.IO);
        } else {
            fieldForSplit = rootField.add(stage.getField(), EFieldType.I);
        }

        for (String splitFieldPath : stage.getFieldsForSplits()) {
            Field splitField = setting.getInputRootField().add(splitFieldPath, EFieldType.BEGIN);
            setting.addFieldFlow(new FieldFlow(fieldForSplit, splitField, Edge.Type.DIRECT));
        }
        // Field for Remaining Splits
        if (stage.getTooManySplitAction() == ETooManySplitsAction.TO_LIST) {
            Field splitField = setting.getInputRootField().add(stage.getFieldForRemainingSplits(), EFieldType.BEGIN);
            setting.addFieldFlow(new FieldFlow(fieldForSplit, splitField, Edge.Type.DIRECT));
        }
    }

    @Override public void createNodes(IFieldSplitterStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    private void addStageAttributes(IFieldSplitterStage stage, PipelineContext ctx) {
        Node stageNode = ctx.getGraphHelper().getNode(stage);

        stageNode.addAttribute(Constants.ATTRIBUTE_FIELD_TO_SPLIT, stage.getField());
        stageNode.addAttribute(Constants.ATTRIBUTE_SEPARATOR, stage.getSeparator());
    }
}
