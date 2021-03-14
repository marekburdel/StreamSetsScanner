package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.ILanePredicateComponent;

/**
 * @author mburdel
 */
public class LanePredicateComponent implements ILanePredicateComponent {

    private String outputLane;
    private String predicate;

    public LanePredicateComponent(String outputLane, String predicate) {
        this.outputLane = outputLane;
        this.predicate = predicate;
    }

    @Override public String getOutputLane() {
        return outputLane;
    }

    @Override public String getPredicate() {
        return predicate;
    }
}
