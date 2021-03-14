package eu.profinit.manta.connector.streamsets.resolver.model.impl;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.model.model.IPipeline;
import eu.profinit.manta.connector.streamsets.model.model.ITopology;

/**
 * @author mburdel
 */
public class StreamSetsServer implements IStreamSetsServer {

    private ITopology topology;

    private IPipeline pipeline;

    public StreamSetsServer(ITopology topology) {
        this.topology = topology;
    }

    public StreamSetsServer(IPipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override public ITopology getTopology() {
        return topology;
    }

    @Override public IPipeline getPipeline() {
        return pipeline;
    }
}
