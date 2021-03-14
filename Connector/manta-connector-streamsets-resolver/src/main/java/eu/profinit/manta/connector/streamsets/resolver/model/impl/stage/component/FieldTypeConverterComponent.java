package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldTypeConverterDataType;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldTypeConverterComponent implements IFieldTypeConverterComponent {

    private List<String> fields;
    private EFieldTypeConverterDataType dataType;

    public FieldTypeConverterComponent(List<String> fields, EFieldTypeConverterDataType dataType) {
        this.fields = fields;
        this.dataType = dataType;
    }

    @Override public List<String> getFields() {
        return fields;
    }

    @Override public EFieldTypeConverterDataType getDataType() {
        return dataType;
    }
}
