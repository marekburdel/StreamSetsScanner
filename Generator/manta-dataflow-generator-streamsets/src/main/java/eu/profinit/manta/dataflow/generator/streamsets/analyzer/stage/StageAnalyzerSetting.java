package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryResult;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Binding;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldAttribute;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.model.Node;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class to store all information for each stage while data flow generation
 * @author mburdel
 */
public class StageAnalyzerSetting {

    /**
     * Input lanes represented as bindings
     */
    private List<Binding> inputBindings = new ArrayList<>();

    /**
     * Output lanes represented as bindings
     */
    private List<Binding> outputBindings = new ArrayList<>();

    /**
     * Bindings that are representing inner connection and describes stage's logic
     */
    private List<Binding> otherBindings = new ArrayList<>();

    /**
     * Analyzed flows during fields' analysis.
     */
    private List<FieldFlow> fieldFlows = new ArrayList<>();

    /**
     * Fields' attributes
     */
    private List<FieldAttribute> fieldAttributes = new ArrayList<>();

    /**
     * Inner fields in order
     */
    private List<Field> innerFields = new ArrayList<>();

    /**
     * Pairs for StreamSelectorAnalyzer
     */
    private List<Pair<Field, String>> outputPairs;

    /**
     * Node and Field pairs mapping (database's operation only)
     */
    private List<Pair<Node, Field>> nodeAndFieldPairs = new ArrayList<>();

    /**
     * QueryResult for analyzed stage (database's operation only)
     */
    private DataflowQueryResult queryResult;

    /**
     * Connection for analyzed stages (database's operation only)
     */
    private Connection connection;

    public List<Field> getInnerFields() {
        return innerFields;
    }

    public List<Binding> getInputBindings() {
        return inputBindings;
    }

    List<Binding> getOutputBindings() {
        return outputBindings;
    }

    List<Binding> getOtherBindings() {
        return otherBindings;
    }

    List<FieldFlow> getFieldFlows() {
        return fieldFlows;
    }

    List<FieldAttribute> getFieldAttributes() {
        return fieldAttributes;
    }

    public void addInputBinding(Binding binding) {
        inputBindings.add(binding);
    }

    public void addOutputBinding(Binding binding) {
        outputBindings.add(binding);
    }

    public void addOtherFieldBinding(Binding binding) {
        if (binding.getSourceField() == null || binding.getTargetField() == null) {
            return;
        }
        otherBindings.add(binding);
    }

    public void addFieldFlow(FieldFlow fieldFlow) {
        if (fieldFlow.getSourceField() == null || fieldFlow.getTargetField() == null) {
            return;
        }
        fieldFlows.add(fieldFlow);
    }

    public void addFieldAttribute(FieldAttribute fieldAttribute) {
        if (fieldAttribute.getField() == null) {
            return;
        }
        fieldAttributes.add(fieldAttribute);
    }

    public void addInnerField(Field field) {
        innerFields.add(field);
    }

    public Field getInputRootField() {
        if (!innerFields.isEmpty()) {
            return innerFields.get(0);
        }
        return null;
    }

    public Field getOutputRootField() {
        if (!innerFields.isEmpty()) {
            return innerFields.get(innerFields.size() - 1);
        }
        return null;
    }

    public List<Pair<Field, String>> getOutputPairs() {
        return outputPairs;
    }

    public void setOutputPairs(List<Pair<Field, String>> outputPairs) {
        this.outputPairs = outputPairs;
    }

    public DataflowQueryResult getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(DataflowQueryResult queryResult) {
        this.queryResult = queryResult;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<Pair<Node, Field>> getNodeAndFieldPairs() {
        return nodeAndFieldPairs;
    }

    public void addNodeAndFieldPair(Node node, Field field) {
        if (field == null) {
            return;
        }
        nodeAndFieldPairs.add(new ImmutablePair<>(node, field));
    }
}
