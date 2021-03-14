package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageType;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFilterOperation;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldOrderStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldPivoterStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldRemoverStage;

/**
 * Class to store connection between 2 fields or 2 stages with their root fields.
 * @author mburdel
 */
public class Binding {

    private Field sourceField;
    private Field targetField;
    private EFieldBindingMode bindingMode;

    private IStage sourceStage;
    private IStage targetStage;

    public Binding(Field sourceField, Field targetField, EFieldBindingMode bindingMode) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.bindingMode = bindingMode;
    }

    public Binding(Field sourceField, Field targetField, IStage sourceStage, IStage targetStage) {
        this.sourceStage = sourceStage;
        this.targetStage = targetStage;
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.bindingMode = resolveBindingMode(targetStage);
    }

    private EFieldBindingMode resolveBindingMode(IStage targetStage) {
        EFieldBindingMode mode = EFieldBindingMode.MERGE;
        // Field Remover Stage
        if (IStageType.StageName.getStageName(targetStage.getStageName()) == IStageType.StageName.FIELD_REMOVER
                && ((IFieldRemoverStage) targetStage).getFilterOperation() == EFilterOperation.KEEP) {
            mode = EFieldBindingMode.MERGE_REMOVER_KEEP;
        }
        // Field Order Stage
        if (IStageType.StageName.getStageName(targetStage.getStageName()) == IStageType.StageName.FIELD_ORDER
                && ((IFieldOrderStage) targetStage).getExtraFieldAction()
                == IFieldOrderStage.EExtraFieldAction.DISCARD) {
            mode = EFieldBindingMode.MERGE_REMOVER_KEEP;
        }
        // Field Pivoter Stage
        if (IStageType.StageName.getStageName(targetStage.getStageName()) == IStageType.StageName.FIELD_PIVOTER
                && !((IFieldPivoterStage) targetStage).getCopyFields()) {
            mode = EFieldBindingMode.MERGE_REMOVER_KEEP;
        }
        return mode;
    }

    /**
     * Method merges 2 fields according to binding mode.
     * @return <code>true</code> if at least one field was changed (added child) else <code>false</code>
     */
    public boolean merge() {
        if (!sourceField.hasChildren() && !targetField.hasChildren()) {
            return false;
        }
        switch (bindingMode) {
        case MERGE_EL:
            return sourceField.mergeEL(targetField);
        case MERGE_REMOVER_KEEP:
            return sourceField.mergeKeep(targetField);
        case MERGE_RENAMER_REVERSED:
            return sourceField.mergeWithReversedFieldType(targetField);
        case MERGE:
            return sourceField.merge(targetField);
        default:
            throw new IllegalStateException();
        }
    }

    public Field getSourceField() {
        return sourceField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public IStage getSourceStage() {
        return sourceStage;
    }

    public IStage getTargetStage() {
        return targetStage;
    }
}
