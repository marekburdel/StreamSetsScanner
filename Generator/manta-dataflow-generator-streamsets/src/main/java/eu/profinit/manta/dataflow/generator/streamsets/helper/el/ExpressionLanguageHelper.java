package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

import java.util.List;
import java.util.Map;

/**
 * Helper analyzes JSP 2.0 Expression Language given from StreamSets' objects.
 *
 * @author mburdel
 */
public class ExpressionLanguageHelper {

    /**
     * Field's Dependencies Resolver
     */
    private ELDependenciesResolver elDependenciesResolver;

    /**
     * Runtime Values Resolver
     */
    private ELRuntimeValuesResolver elRuntimeValuesResolver;

    /**
     * Method replaces
     *      ${PARAMETER},
     *      ${record:value(PARAMETER)},
     *      ${runtime:conf('PROPERTY')},
     *      ${runtime:loadResource("file.txt", true/false)}
     * with value stored in
     *      pipeline's parameters,
     *      .properties file,
     *      resources files
     * and removes other EL.
     * Path to files is in runtimeValuesPath variable.
     * @param expressionString expression
     * @param pipelineParameters pipeline's parameters
     * @param remove true if EL should be remove if Runtime Value is not recognized,
     *               else only Runtime Values will be replaced
     * @return string with replaced Runtime Values with its value stored
     * in pipeline's parameters, .properties file and resources file.
     */
    public String replaceRuntimeValues(String expressionString, Map<String, String> pipelineParameters,
            boolean remove) {
        return elRuntimeValuesResolver.replaceRuntimeValues(expressionString, pipelineParameters, remove);
    }

    /**
     * Method parses JSP 2.0 Expression Language and analyzing functions where field's path can be mentioned.
     * If field's path is found then called methods add it into dependencies' list.
     *
     * @param expressionString JSP 2.0 Expression Language
     * @return dependencies with field's paths and edge's types
     */
    public List<Dependence> getDependenciesFromEL(String expressionString) {
        return elDependenciesResolver.getDependenciesFromEL(expressionString);
    }

    public void setElDependenciesResolver(ELDependenciesResolver elDependenciesResolver) {
        this.elDependenciesResolver = elDependenciesResolver;
    }

    public void setElRuntimeValuesResolver(ELRuntimeValuesResolver elRuntimeValuesResolver) {
        this.elRuntimeValuesResolver = elRuntimeValuesResolver;
    }
}
