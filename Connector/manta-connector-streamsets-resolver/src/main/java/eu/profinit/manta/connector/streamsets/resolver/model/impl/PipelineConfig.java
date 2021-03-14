package eu.profinit.manta.connector.streamsets.resolver.model.impl;

import eu.profinit.manta.connector.streamsets.model.model.IConfiguration;
import eu.profinit.manta.connector.streamsets.model.model.ILane;
import eu.profinit.manta.connector.streamsets.model.model.IPipelineConfig;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;
import java.util.Map;

/**
 * Implementation of Pipeline Configuration.
 *
 * @author mburdel
 */
public class PipelineConfig implements IPipelineConfig {

    private String pipelineId;

    private String title;

    private String description;

    private IConfiguration configuration;

    private List<IStage> stages;

    private Map<String, ILane> lanes;

    public PipelineConfig(String pipelineId, String title, String description, IConfiguration configuration,
            List<IStage> stages) {
        this.pipelineId = pipelineId;
        this.title = title;
        this.description = description;
        this.configuration = configuration;
        this.stages = stages;
    }

    @Override public String getPipelineId() {
        return pipelineId;
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public IConfiguration getConfiguration() {
        return configuration;
    }

    @Override public List<IStage> getStages() {
        return stages;
    }

    @Override public Map<String, ILane> getLanes() {
        return lanes;
    }

    public void setLanes(Map<String, ILane> lanes) {
        this.lanes = lanes;
    }

    @Override public ILane getLane(String laneId) {
        return lanes.get(laneId);
    }
}
