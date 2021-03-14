package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldTypeConverterDataType;

/**
 * @author mburdel
 */
public interface IWholeTypeConverterComponent extends IStageComponent {

    /**
     *
     * @return source type
     */
    EFieldTypeConverterDataType getSourceType();

    /**
     *
     * @return target type
     */
    EFieldTypeConverterDataType getTargetType();
}
