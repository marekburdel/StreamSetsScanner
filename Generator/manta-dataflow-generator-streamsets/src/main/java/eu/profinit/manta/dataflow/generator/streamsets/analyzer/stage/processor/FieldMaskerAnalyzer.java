package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldMaskComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldMaskerStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldAttribute;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 * @author mburdel
 */
public class FieldMaskerAnalyzer extends AbstractStageAnalyzer<IFieldMaskerStage> {

    @Override public void analyzeFields(IFieldMaskerStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        super.analyzeFields(stage, ctx);
        analyzeMask(stage, setting);
    }

    private void analyzeMask(IFieldMaskerStage stage, StageAnalyzerSetting setting) {
        for (IFieldMaskComponent component : stage.getFieldMasks()) {
            for (String fieldPath : component.getFields()) {
                Field fieldToMask = setting.getInputRootField().add(fieldPath, EFieldType.IO);
                analyzeAttributes(fieldToMask, component, setting);
            }
        }
    }

    private void analyzeAttributes(Field fieldToMask, IFieldMaskComponent component, StageAnalyzerSetting setting) {
        setting.addFieldAttribute(
                new FieldAttribute(fieldToMask, Constants.ATTRIBUTE_MASK_TYPE, component.getMaskType().name()));
        switch (component.getMaskType()) {
        case FIXED_LENGTH:
        case VARIABLE_LENGTH:
            break;
        case CUSTOM:
            setting.addFieldAttribute(
                    new FieldAttribute(fieldToMask, Constants.ATTRIBUTE_CUSTOM_MASK, component.getMask()));
            break;
        case REGEX:
            setting.addFieldAttribute(
                    new FieldAttribute(fieldToMask, Constants.ATTRIBUTE_REGULAR_EXPRESSION, component.getRegex()));
            setting.addFieldAttribute(
                    new FieldAttribute(fieldToMask, Constants.ATTRIBUTE_GROUPS_TO_SHOW, component.getGroupsToShow()));
            break;
        default: // do nothing
        }
    }
}
