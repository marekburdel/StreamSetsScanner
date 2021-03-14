package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ILanePredicateComponent;

import java.util.List;

/**
 * @author mburdel
 */
public interface IStreamSelectorStage extends IStage {

    /**
     *
     * @return components that contain information about predicated lanes
     */
    List<? extends ILanePredicateComponent> getLanePredicates();
}
