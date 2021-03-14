package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IRenameMappingComponent;

import java.util.List;

/**
 * Use the Field Renamer to rename fields in a record.
 *
 * @author mburdel
 */
public interface IFieldRenamerStage extends IStage {

    /**
     *
     * @return rename mapping component
     */
    List<? extends IRenameMappingComponent> getRenameMapping();
}
