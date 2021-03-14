package eu.profinit.manta.connector.streamsets.model.model;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;
import java.util.Map;

/**
 * StreamSets Pipeline Configuration.
 *
 * @author mburdel
 */

public interface IPipelineConfig {
    /**
     *
     * @return pipeline id
     */
    String getPipelineId();

    /**
     *
     * @return title of Pipeline defined by user
     */
    String getTitle();

    /**
     *
     * @return user's description of Pipeline
     */
    String getDescription();

    /**
     *
     * @return configuration for Pipeline
     */
    IConfiguration getConfiguration();

    /**
     *
     * @return Pipeline's Stages
     */
    List<IStage> getStages();

    /**
     *
     * @return Pipeline's Lanes
     */
    Map<String, ILane> getLanes();

    /**
     *
     * @param laneId lane's Id
     * @return lane
     */
    ILane getLane(String laneId);

}
