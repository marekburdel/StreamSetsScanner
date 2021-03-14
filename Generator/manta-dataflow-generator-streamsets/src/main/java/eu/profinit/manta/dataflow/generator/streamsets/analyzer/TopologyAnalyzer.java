package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.connector.streamsets.model.model.ITopology;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;

/** UNSUPPORTED - TBA blocked by Control Hub license
 * Topology analyzer
 *
 * @author mburdel
 */
public class TopologyAnalyzer implements IAnalyzer<ITopology> {

    @Override public void analyze(ITopology topology, StreamSetsGraphHelper gh) {
        throw new UnsupportedOperationException("Topology's analyzer is not supported!");
    }

}
