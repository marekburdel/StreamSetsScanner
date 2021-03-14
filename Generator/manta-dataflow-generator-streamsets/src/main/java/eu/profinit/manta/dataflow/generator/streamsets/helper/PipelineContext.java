package eu.profinit.manta.dataflow.generator.streamsets.helper;

import eu.profinit.manta.connector.streamsets.model.model.ILane;
import eu.profinit.manta.connector.streamsets.model.model.IPipeline;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;

import java.util.Map;

/**
 * Main class to store all information about pipeline with stages' settings.
 * @author mburdel
 */
public final class PipelineContext {

    /**
     * Graph helper that is helping to build nodes and flows.
     */
    private final StreamSetsGraphHelper gh;

    /**
     * Analyzed pipeline in this context.
     */
    private IPipeline pipeline;

    /**
     * Expansion map for Fields' Expansion phase.
     */
    private Map<IStage, Boolean> expansionMap;

    /**
     * Settings map for every analyzed stage.
     */
    private Map<IStage, StageAnalyzerSetting> settings;

    /**
     * Stages' lanes mapped by lane's Id.
     */
    private Map<String, ILane> lanes;

    public PipelineContext(IPipeline pipeline, StreamSetsGraphHelper gh) {
        this.pipeline = pipeline;
        this.gh = gh;
    }

    public void setSettings(Map<IStage, StageAnalyzerSetting> settings) {
        this.settings = settings;
    }

    public StreamSetsGraphHelper getGraphHelper() {
        return gh;
    }

    public Map<IStage, Boolean> getExpansionMap() {
        return expansionMap;
    }

    public void setExpansionMap(Map<IStage, Boolean> expansionMap) {
        this.expansionMap = expansionMap;
    }

    public StageAnalyzerSetting getSetting(IStage stage) {
        return settings.get(stage);
    }

    public Map<String, ILane> getLanes() {
        return lanes;
    }

    public void setLanes(Map<String, ILane> lanes) {
        this.lanes = lanes;
    }

    public IPipeline getPipeline() {
        return pipeline;
    }
}
