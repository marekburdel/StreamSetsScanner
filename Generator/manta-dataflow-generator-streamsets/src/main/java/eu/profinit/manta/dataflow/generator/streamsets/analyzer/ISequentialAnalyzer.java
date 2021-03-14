package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 * Sequential Analyzer analyzes StreamSets Object step by step.
 *
 * @author mburdel
 */
public interface ISequentialAnalyzer<T> {

    /**
     * Pipeline context initialization. Method creates for each stage's analyzer and initializes expansion map.
     *
     * @param object of StreamSets
     * @param ctx context
     */
    void init(T object, PipelineContext ctx);

    /**
     * Method invokes methods, which recursively call fields' analysis in each stage's analyzer.
     * After this method all known fields(gained from model) are stored in stage's analyzer's setting.
     *
     * @param object of StreamSets
     * @param ctx context
     */
    void analyzeFields(T object, PipelineContext ctx);

    /**
     * Method analyzes stage's input lanes and store it into stage's analyzer's settings as bindings.
     *
     * @param object of StreamSets
     * @param ctx context
     */
    void analyzeBindings(T object, PipelineContext ctx);

    /**
     * Method expands analyzed fields to all object until object(pipeline/topology/fragment) aren't fully expanded.
     * After this method are all fields, that are connected to each others, fully merged according to expansion rules.
     *
     * @param object of StreamSets
     * @param ctx context
     */
    void expandFields(T object, PipelineContext ctx);

    /**
     * Method creates nodes and flows according to certain rules.
     * After this method output graph is created for chosen pipeline/topology.
     *
     * @param object of StreamSets
     * @param ctx context
     */
    void createNodesAndFlows(T object, PipelineContext ctx);
}
