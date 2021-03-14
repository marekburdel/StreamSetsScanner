package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

import eu.profinit.manta.dataflow.model.Edge;

/**
 * Class to store information about function argument.
 * @author mburdel
 */
public final class FunctionArg {
    private EFunctionArgType functionArgType;
    private Edge.Type edgeType;

    public FunctionArg(EFunctionArgType createFlow, Edge.Type edgeType) {
        this.functionArgType = createFlow;
        this.edgeType = edgeType;
    }

    public EFunctionArgType getFunctionArgType() {
        return functionArgType;
    }

    public Edge.Type getEdgeType() {
        return edgeType;
    }
}
