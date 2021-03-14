package eu.profinit.manta.connector.streamsets.resolver.model.impl;

import eu.profinit.manta.connector.streamsets.model.model.IPipeline;
import eu.profinit.manta.connector.streamsets.model.model.IPipelineConfig;

/**
 * Implementation of StreamSets Pipeline.
 *
 * @author mburdel
 */
public class Pipeline implements IPipeline {

    /**
     * Pipeline Configuration contains Pipeline's Id, Configuration and Stages.
     */
    private IPipelineConfig pipelineConfig;

    public Pipeline(IPipelineConfig pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
    }

    @Override public IPipelineConfig getPipelineConfig() {
        return pipelineConfig;
    }

}
