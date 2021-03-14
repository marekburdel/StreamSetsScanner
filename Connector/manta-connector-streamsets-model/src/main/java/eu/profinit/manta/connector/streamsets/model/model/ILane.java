package eu.profinit.manta.connector.streamsets.model.model;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;

/**
 * StreamSets Lane(or Stream) - Connection between stages. Implementation of lane in StreamSets Data Collector
 * is defined as stream that is going from source(Stage) into multiple targets(Stages).
 *
 * @author mburdel
 */

public interface ILane extends Comparable<ILane> {

    /**
     *
     * @return lane id
     */
    String getLaneId();

    /**
     *
     * @return source Stage
     */
    IStage getSourceStage();

    /**
     *
     * @return target Stages
     */
    List<IStage> getTargetStages();
}
