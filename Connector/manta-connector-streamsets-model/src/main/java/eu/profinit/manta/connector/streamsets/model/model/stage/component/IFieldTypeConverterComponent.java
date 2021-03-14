package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldTypeConverterDataType;

import java.util.List;

/**
 * Component of Field Type Converter Stage
 * @author mburdel
 */
public interface IFieldTypeConverterComponent extends IStageComponent {

    /**
     *
     * @return list of fields to convert
     */
    List<String> getFields();

    /**
     *
     * @return type to convert
     */
    EFieldTypeConverterDataType getDataType();
}
