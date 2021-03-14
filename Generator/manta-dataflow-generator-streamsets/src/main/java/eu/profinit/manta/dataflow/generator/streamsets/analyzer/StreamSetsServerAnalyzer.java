package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.NodeType;

/**
 * Root analyzer for StreamSets Server.
 *
 * @author mburdel
 */
public class StreamSetsServerAnalyzer implements IAnalyzer<IStreamSetsServer> {

    private String serverName;

    private PipelineAnalyzer pipelineAnalyzer;

    private TopologyAnalyzer topologyAnalyzer;

    @Override public void analyze(IStreamSetsServer server, StreamSetsGraphHelper gh) {
        gh.buildNode(server, serverName, NodeType.SERVER, null);
        if (server.getTopology() == null) {
            pipelineAnalyzer.analyze(server.getPipeline(), gh);
        } else {
            topologyAnalyzer.analyze(server.getTopology(), gh);
        }
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setPipelineAnalyzer(PipelineAnalyzer pipelineAnalyzer) {
        this.pipelineAnalyzer = pipelineAnalyzer;
    }

    public void setTopologyAnalyzer(TopologyAnalyzer topologyAnalyzer) {
        this.topologyAnalyzer = topologyAnalyzer;
    }
}
