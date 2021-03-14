package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldMaskComponent;

import java.util.List;

/**
 * The Field Masker masks string values based on the selected mask type.
 *
 * @author mburdel
 */
public interface IFieldMaskerStage extends IStage {

    /**
     *
     * @return field mask component
     */
    List<? extends IFieldMaskComponent> getFieldMasks();
}
