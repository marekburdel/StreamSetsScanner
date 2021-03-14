package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

import eu.profinit.manta.dataflow.model.Edge;

/**
 * The result of resolved JSP 2.0 Expression Language.
 * Dependence describes connection between field path and processed expression (JSP 2.0 Expression Language).
 * It stores field's path that was used in expression.
 *
 * @author mburdel
 */
public class Dependence {

    /**
     * Found field's path that was used in expression.
     */
    private String fieldPath;

    /**
     * Flow type for found field's path.
     */
    private Edge.Type flowType;

    public Dependence(String fieldPath, Edge.Type flowType) {
        this.fieldPath = fieldPath;
        this.flowType = flowType;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public Edge.Type getFlowType() {
        return flowType;
    }
}
