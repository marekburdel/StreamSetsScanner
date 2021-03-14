package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.dataflow.generator.modelutils.AbstractGraphTask;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.StreamSetsServerAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.Graph;

/**
 * @author mburdel
 */
public class StreamSetsDataflowTask extends AbstractGraphTask<IStreamSetsServer> {

    private StreamSetsServerAnalyzer serverAnalyzer;

    @Override protected void doExecute(IStreamSetsServer input, Graph outputGraph) {
        StreamSetsGraphHelper graphHelper = new StreamSetsGraphHelper(outputGraph, getScriptResource(), input);
        serverAnalyzer.analyze(input, graphHelper);
    }

    /**
     *
     * @param serverAnalyzer Main analyzer
     */
    public void setServerAnalyzer(StreamSetsServerAnalyzer serverAnalyzer) {
        this.serverAnalyzer = serverAnalyzer;
    }

}
