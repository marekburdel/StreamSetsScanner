package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IRenameMappingComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldRenamerStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.*;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.Edge;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;

/**
 * @author mburdel
 */
public class FieldRenamerAnalyzer extends AbstractStageAnalyzer<IFieldRenamerStage> {

    // regex to field(s)

    @Override public void analyzeFields(IFieldRenamerStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        createOutputRootField(stage, ctx);
        analyzeRenameMapping(stage, ctx);
    }

    private void createOutputRootField(IFieldRenamerStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        setting.addInnerField(new Field("/", EFieldType.IO, getParserService()));

        setting.addOtherFieldBinding(
                new Binding(setting.getInputRootField(), setting.getOutputRootField(), EFieldBindingMode.MERGE));
        setting.addFieldFlow(
                new FieldFlow(setting.getInputRootField(), setting.getOutputRootField(), Edge.Type.DIRECT));
    }

    private void analyzeRenameMapping(IFieldRenamerStage stage, PipelineContext ctx) {

        StageAnalyzerSetting setting = ctx.getSetting(stage);
        Field inputRootField = setting.getInputRootField();
        Field outputRootField = setting.getOutputRootField();

        for (IRenameMappingComponent component : stage.getRenameMapping()) {
            Field fromField = inputRootField.addRenamerField(component.getFromFieldExpression(), EFieldType.I);
            Field toField = outputRootField.addRenamerField(component.getToFieldExpression(), EFieldType.BEGIN);

            setting.addOtherFieldBinding(new Binding(fromField, toField, EFieldBindingMode.MERGE_RENAMER_REVERSED));
            setting.addFieldFlow(new FieldFlow(fromField, toField, Edge.Type.DIRECT));
        }
    }

    @Override public void createNodes(IFieldRenamerStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();

        Node stageNode = gh.getNode(stage);

        Field inputRootField = setting.getInputRootField();
        Field outputRootField = setting.getOutputRootField();

        Node inputNode = gh.addNode(Constants.INPUT, NodeType.STREAMSETS_GROUP_FIELD, stageNode);
        Node outputNode = gh.addNode(Constants.OUTPUT, NodeType.STREAMSETS_GROUP_FIELD, stageNode);

        inputRootField.createNodes(inputNode, gh);
        outputRootField.createNodes(outputNode, gh);

        createFieldAttributes(stage, ctx);
    }

}
