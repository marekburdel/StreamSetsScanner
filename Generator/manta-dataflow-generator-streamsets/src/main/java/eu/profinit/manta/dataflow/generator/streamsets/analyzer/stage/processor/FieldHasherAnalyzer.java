package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IInPlaceFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ITargetFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldHasherStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldAttribute;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Edge;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldHasherAnalyzer extends AbstractStageAnalyzer<IFieldHasherStage> {

    @Override public void analyzeFields(IFieldHasherStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);

        analyzeHashInPlace(stage, setting);
        analyzeHashToTarget(stage, setting);
        analyzeHashRecord(stage, setting);
    }

    @Override public void createFlows(IFieldHasherStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        analyzeHashRecordFlows(stage, setting);
        super.createFlows(stage, ctx);
    }

    private void analyzeHashInPlace(IFieldHasherStage stage, StageAnalyzerSetting setting) {
        for (IInPlaceFieldHasherComponent component : stage.getInPlaceFieldHasherComponents()) {
            for (String fieldPath : component.getSourceFieldsToHash()) {
                Field fieldToHash = setting.getInputRootField().add(fieldPath, EFieldType.IO);
                setting.addFieldAttribute(
                        new FieldAttribute(fieldToHash, Constants.ATTRIBUTE_HASH_TYPE, component.getHashType().name()));
            }
        }
    }

    private void analyzeHashToTarget(IFieldHasherStage stage, StageAnalyzerSetting setting) {
        for (ITargetFieldHasherComponent component : stage.getTargetFieldHasherComponents()) {
            Field targetField;
            for (String fieldPath : component.getSourceFieldsToHash()) {
                if (fieldPath.equals(component.getTargetField())) {
                    setting.getInputRootField().add(component.getTargetField(), EFieldType.IO);
                }
            }
            targetField = setting.getInputRootField().add(component.getTargetField(), EFieldType.BEGIN);

            setting.addFieldAttribute(
                    new FieldAttribute(targetField, Constants.ATTRIBUTE_HASH_TYPE, component.getHashType().name()));
            for (String fieldPath : component.getSourceFieldsToHash()) {
                Field fieldToHash = setting.getInputRootField().add(fieldPath, EFieldType.IO);
                setting.addFieldFlow(new FieldFlow(fieldToHash, targetField, Edge.Type.DIRECT));
            }
        }
    }

    private void analyzeHashRecord(IFieldHasherStage stage, StageAnalyzerSetting setting) {
        if (stage.getHashEntireRecord()) {
            Field targetField = setting.getInputRootField().add(stage.getHashEntireRecordTargetField(), EFieldType.O);
            setting.addFieldAttribute(new FieldAttribute(targetField, Constants.ATTRIBUTE_HASH_TYPE,
                    stage.getHashEntireRecordHashType().name()));
        }
    }

    private void analyzeHashRecordFlows(IFieldHasherStage stage, StageAnalyzerSetting setting) {
        if (stage.getHashEntireRecord()) {
            List<Field> leafs = setting.getInputRootField().getOutputLeafs();
            Field inputRootField = setting.getInputRootField();
            for (Field leaf : leafs) {
                Field targetField = inputRootField.findFieldByPath(stage.getHashEntireRecordTargetField());
                // without direct flow to itself
                if (targetField != null && !leaf.getFieldPath().equals(targetField.getFieldPath())) {
                    setting.addFieldFlow(new FieldFlow(leaf, targetField, Edge.Type.DIRECT));
                }
            }
        }
    }
}
