package eu.profinit.manta.connector.streamsets.resolver.model.impl;

import eu.profinit.manta.connector.streamsets.model.model.ILane;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of Lane(Stream) - Connection between 2 stages.
 *
 * @author mburdel
 */
public class Lane implements ILane {

    private String laneId;

    private IStage sourceStage;

    private List<IStage> targetStages = new ArrayList<>();

    public Lane(String laneId) {
        this.laneId = laneId;
    }

    @Override public String getLaneId() {
        return laneId;
    }

    @Override public IStage getSourceStage() {
        return sourceStage;
    }

    /**
     *
     * @param sourceStage lane's source(always starts from one stage)
     */
    public void setSourceStage(IStage sourceStage) {
        this.sourceStage = sourceStage;
    }

    @Override public List<IStage> getTargetStages() {
        return targetStages;
    }

    @Override public int compareTo(ILane o) {
        return this.laneId.compareTo(o.getLaneId());
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lane lane = (Lane) o;
        return getLaneId().equals(lane.getLaneId());
    }

    @Override public int hashCode() {
        return Objects.hash(getLaneId());
    }

    /**
     *
     * @param stage where the lane is ending
     */
    public void addTargetStage(IStage stage) {
        targetStages.add(stage);
    }
}
