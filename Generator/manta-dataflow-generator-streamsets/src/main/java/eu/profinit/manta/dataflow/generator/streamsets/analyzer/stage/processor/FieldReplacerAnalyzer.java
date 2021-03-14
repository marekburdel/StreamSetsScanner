package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IReplaceRuleComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldReplacerStage;
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
public class FieldReplacerAnalyzer extends AbstractStageAnalyzer<IFieldReplacerStage> {

    @Override public void analyzeFields(IFieldReplacerStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        super.analyzeFields(stage, ctx);
        analyzeReplaceRules(stage, setting);
    }

    private void analyzeReplaceRules(IFieldReplacerStage stage, StageAnalyzerSetting setting) {
        for (IReplaceRuleComponent component : stage.getReplaceRules()) {
            Field addedField = setting.getInputRootField().add(component.getField(), EFieldType.IO);

            if (component.getSetToNull()) {
                setting.addFieldAttribute(new FieldAttribute(addedField, Constants.ATTRIBUTE_SET_TO_NULL, "Yes"));
            } else {
                setting.addFieldAttribute(
                        new FieldAttribute(addedField, Constants.ATTRIBUTE_NEW_VALUE, component.getReplacement()));

            }
        }
    }

}
