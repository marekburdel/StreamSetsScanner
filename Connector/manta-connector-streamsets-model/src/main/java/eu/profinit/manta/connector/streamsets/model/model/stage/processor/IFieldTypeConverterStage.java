package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IWholeTypeConverterComponent;

import java.util.List;

/**
 * The Field Type Converter analyzer converts the data types of fields to compatible data types.
 *
 * @author mburdel
 */
public interface IFieldTypeConverterStage extends IStage {

    /**
     *
     * @return enum convert by
     */
    EConvertBy getConvertBy();

    /**
     *
     * @return field type converter components
     */
    List<? extends IFieldTypeConverterComponent> getFieldTypeConverters();

    /**
     * Convert the data type of all fields with the specified type.
     *
     * @return whole type converter components
     */
    List<? extends IWholeTypeConverterComponent> getWholeTypeConverters();
}
