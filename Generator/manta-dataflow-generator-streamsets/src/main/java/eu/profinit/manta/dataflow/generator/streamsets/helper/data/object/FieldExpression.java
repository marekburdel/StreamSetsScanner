package eu.profinit.manta.dataflow.generator.streamsets.helper.data.object;

import eu.profinit.manta.dataflow.generator.streamsets.helper.el.Dependence;

import java.util.List;

/**
 * Object to store field's path and its dependencies for Expression Evaluator Analyzer
 * @author mburdel
 */
public class FieldExpression {

    /**
     * Field's path
     */
    private String fieldPath;

    /**
     * List of dependencies from resolved expression
     */
    private List<Dependence> dependencies;

    /**
     * Expression
     */
    private String expression;

    public FieldExpression(String fieldPath, List<Dependence> dependencies, String expression) {
        this.fieldPath = fieldPath;
        this.dependencies = dependencies;
        this.expression = expression;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public List<Dependence> getDependencies() {
        return dependencies;
    }

    public String getExpression() {
        return expression;
    }
}
