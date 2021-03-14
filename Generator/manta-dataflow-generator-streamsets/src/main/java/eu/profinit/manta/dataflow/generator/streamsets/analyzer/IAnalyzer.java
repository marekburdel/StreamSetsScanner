package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;

/**
 *
 * @author mburdel
 */
public interface IAnalyzer<T> {

    /**
     * Method analyzes the StreamSets Object and constructs the Graph.
     *
     * StreamSets Dataflow Generator phases:
     *
     * 1. Fields' Analysis
     * 2. Fields' Expansion
     * 3. Creation of nodes and flows
     *
     * @param object object for analysis
     * @param gh graph helper
     */
    void analyze(T object, StreamSetsGraphHelper gh);
}
