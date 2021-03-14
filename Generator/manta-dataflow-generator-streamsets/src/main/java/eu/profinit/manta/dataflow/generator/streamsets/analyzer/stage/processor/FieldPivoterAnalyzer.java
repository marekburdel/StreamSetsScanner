package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldPivoterStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Edge;

/**
 *
 * @author mburdel
 */
public class FieldPivoterAnalyzer extends AbstractStageAnalyzer<IFieldPivoterStage> {

    @Override public void analyzeFields(IFieldPivoterStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);
        Field rootField = setting.getInputRootField();

        if (stage.getCopyFields() && stage.getSaveOriginalFieldName()) {
            Field fieldToPivot = rootField.add(stage.getFieldToPivot(), EFieldType.I);
            Field pivotedItemsPath = rootField.add(stage.getPivotedItemsPath(), EFieldType.BEGIN);
            Field originalFieldNamePath = rootField.add(stage.getOriginalFieldNamePath(), EFieldType.BEGIN);

            setting.addFieldFlow(new FieldFlow(fieldToPivot, pivotedItemsPath, Edge.Type.DIRECT));
            setting.addFieldFlow(new FieldFlow(fieldToPivot, originalFieldNamePath, Edge.Type.DIRECT));
        } else if (stage.getCopyFields()) {
            Field fieldToPivot = rootField.add(stage.getFieldToPivot(), EFieldType.I);
            Field pivotedItemsPath = rootField.add(stage.getPivotedItemsPath(), EFieldType.BEGIN);

            setting.addFieldFlow(new FieldFlow(fieldToPivot, pivotedItemsPath, Edge.Type.DIRECT));
        } else {
            rootField.add(stage.getFieldToPivot(), EFieldType.IO);
        }
    }

}
