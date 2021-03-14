package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

import eu.profinit.manta.dataflow.model.Edge;

/**
 * Class to store field's flow to process after fields' nodes will be created.
 * @author mburdel
 */
public class FieldFlow {
    private Field sourceField;
    private Field targetField;
    private Edge.Type flowType;

    public FieldFlow(Field sourceField, Field targetField, Edge.Type flowType) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.flowType = flowType;
    }

    public Field getSourceField() {
        return sourceField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public Edge.Type getFlowType() {
        return flowType;
    }
}
