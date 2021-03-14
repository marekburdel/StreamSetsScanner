package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * @author mburdel
 */
public interface ILanePredicateComponent extends IStageComponent {

    /**
     *
     * @return output's lane Id
     */
    String getOutputLane();

    /**
     *
     * @return predicate(condition) for the output's lane
     */
    String getPredicate();
}
