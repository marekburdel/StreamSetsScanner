package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldRemoverStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 * @author mburdel
 */
public class FieldRemoverAnalyzer extends AbstractStageAnalyzer<IFieldRemoverStage> {

    @Override public void analyzeFields(IFieldRemoverStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        super.analyzeFields(stage, ctx);
        switch (stage.getFilterOperation()) {
        case KEEP:
            analyzeKeepOperation(stage, setting);
            break;
        case REMOVE:
            analyzeRemoveOperation(stage, setting);
            break;
        default:
            analyzeDefaultOperation(stage, setting);
        }
    }

    private void analyzeKeepOperation(IFieldRemoverStage stage, StageAnalyzerSetting setting) {
        for (String fieldPath : stage.getFields()) {
            setting.getInputRootField().add(fieldPath, EFieldType.IO);
        }
    }

    private void analyzeRemoveOperation(IFieldRemoverStage stage, StageAnalyzerSetting setting) {
        for (String fieldPath : stage.getFields()) {
            setting.getInputRootField().add(fieldPath, EFieldType.I);
        }
    }

    private void analyzeDefaultOperation(IFieldRemoverStage stage, StageAnalyzerSetting setting) {
        for (String fieldPath : stage.getFields()) {
            setting.getInputRootField().add(fieldPath, EFieldType.IO);
        }
    }
}
