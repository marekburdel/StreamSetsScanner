package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldOrderStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class FieldOrderAnalyzer extends AbstractStageAnalyzer<IFieldOrderStage> {

    @Override public void analyzeFields(IFieldOrderStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeOrderFields(stage, ctx);
        analyzeDiscardFields(stage, ctx);
    }

    private void analyzeOrderFields(IFieldOrderStage stage, PipelineContext ctx) {
        Field rootField = ctx.getSetting(stage).getInputRootField();
        for (String fieldName : stage.getOrderFields()) {
            rootField.add(fieldName, EFieldType.IO);
        }
    }

    private void analyzeDiscardFields(IFieldOrderStage stage, PipelineContext ctx) {
        if (stage.getExtraFieldAction() == IFieldOrderStage.EExtraFieldAction.TO_ERROR) {
            Field rootField = ctx.getSetting(stage).getInputRootField();
            for (String fieldName : stage.getDiscardFields()) {
                rootField.add(fieldName, EFieldType.I);
            }
        } // else nothing
    }

}
