package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IInPlaceFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ITargetFieldHasherComponent;

import java.util.List;

/**
 * The Field Hasher analyzer uses an algorithm to encode data. Field Hasher provides several methods to hash data.
 *
 * @author mburdel
 */
public interface IFieldHasherStage extends IStage {

    /**
     * Method - Hash Record
     *
     * @return <code>true</code> if hash record is used else <code>false</code>
     */
    boolean getHashEntireRecord();

    /**
     * Method - Hash Record
     *
     * @return target field for hash record
     */
    String getHashEntireRecordTargetField();

    /**
     * Method - Hash Record
     *
      * @return hash type
     */
    EHashType getHashEntireRecordHashType();

    /**
     * Method - Hash in Place
     *
     * @return Hash in Place component
     */
    List<? extends IInPlaceFieldHasherComponent> getInPlaceFieldHasherComponents();

    /**
     * Method - Hash to Target
     *
     * @return Hash to Target component
     */
    List<? extends ITargetFieldHasherComponent> getTargetFieldHasherComponents();
}
