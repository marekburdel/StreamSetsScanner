package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IWholeTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EConvertBy;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldTypeConverterStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldAttribute;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;

/**
 * @author mburdel
 */
public class FieldTypeConverterAnalyzer extends AbstractStageAnalyzer<IFieldTypeConverterStage> {

    @Override public void analyzeFields(IFieldTypeConverterStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);
        if (stage.getConvertBy() == EConvertBy.BY_FIELD) {
            analyzeFieldType(stage, setting);
        } // else nothing
    }

    private void analyzeFieldType(IFieldTypeConverterStage stage, StageAnalyzerSetting setting) {
        for (IFieldTypeConverterComponent component : stage.getFieldTypeConverters()) {
            for (String fieldPath : component.getFields()) {
                Field field = setting.getInputRootField().add(fieldPath, EFieldType.IO);
                setting.addFieldAttribute(new FieldAttribute(field, Constants.ATTRIBUTE_CONVERT_TO_TYPE,
                        component.getDataType().toString()));
            }
        }
    }

    @Override public void createNodes(IFieldTypeConverterStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addAttributes(stage, ctx);
    }

    private void addAttributes(IFieldTypeConverterStage stage, PipelineContext ctx) {
        if (stage.getConvertBy() == EConvertBy.BY_TYPE) {
            Node stageNode = ctx.getGraphHelper().getNode(stage);
            for (IWholeTypeConverterComponent component : stage.getWholeTypeConverters()) {
                String conversionAttribute = component.getSourceType() + " to " + component.getTargetType();
                stageNode.addAttribute(Constants.ATTRIBUTE_CONVERSION, conversionAttribute);
            }
        }
    }
}
