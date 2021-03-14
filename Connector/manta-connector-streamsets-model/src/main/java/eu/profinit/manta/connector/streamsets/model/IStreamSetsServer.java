package eu.profinit.manta.connector.streamsets.model;

import eu.profinit.manta.connector.streamsets.model.model.IPipeline;
import eu.profinit.manta.connector.streamsets.model.model.ITopology;

/**
 * Root element for StreamSets project. Contains one pipeline or topology of pipelines.
 * One of its has <code>null</code> value.
 *
 * @author mburdel
 */
public interface IStreamSetsServer {

    /**
     *
     * @return topology of pipelines if exists or <code>null</code>
     */
    ITopology getTopology();

    /**
     *
     * @return pipeline if exists or <code>null</code>
     */
    IPipeline getPipeline();
}
