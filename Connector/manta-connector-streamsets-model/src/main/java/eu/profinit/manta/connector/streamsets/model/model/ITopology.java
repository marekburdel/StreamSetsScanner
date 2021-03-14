package eu.profinit.manta.connector.streamsets.model.model;

import java.util.List;

/**
 * The StreamSets Topology defines a topology created in StreamSets Control Hub. It contains pipelines
 * and connection between its.
 *
 * @author mburdel
 */
public interface ITopology {

    /**
     *
     * @return pipelines used in topology
     */
    List<IPipeline> getPipelines();

    // tba lanes between pipelines
}
