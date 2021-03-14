package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;

import java.util.List;

/**
 * @author mburdel
 */
public interface ITargetFieldHasherComponent extends IStageComponent {

    List<String> getSourceFieldsToHash();

    EHashType getHashType();

    String getTargetField();

    String getHeaderAttribute();
}
