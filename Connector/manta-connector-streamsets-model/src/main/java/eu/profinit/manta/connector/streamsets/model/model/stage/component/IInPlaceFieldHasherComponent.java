package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;

import java.util.List;

/**
 * @author mburdel
 */
public interface IInPlaceFieldHasherComponent extends IStageComponent {

    /**
     *
     * @return field(s) to hash
     */
    List<String> getSourceFieldsToHash();

    /**
     *
     * @return hash type
     */
    EHashType getHashType();
}
