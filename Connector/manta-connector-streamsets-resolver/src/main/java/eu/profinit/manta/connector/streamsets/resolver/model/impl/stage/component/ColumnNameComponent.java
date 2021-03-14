package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IColumnNameComponent;

/**
 *
 * @author mburdel
 */
public class ColumnNameComponent implements IColumnNameComponent {

    private String paramValue;

    private String dataType;

    private String columnName;

    private String fieldName;

    private String defaultValue;

    public ColumnNameComponent(String paramValue, String dataType, String columnName, String fieldName,
            String defaultValue) {
        this.paramValue = paramValue;
        this.dataType = dataType;
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.defaultValue = defaultValue;
    }

    @Override public String getParamValue() {
        return paramValue;
    }

    @Override public String getDataType() {
        return dataType;
    }

    @Override public String getColumnName() {
        return columnName;
    }

    @Override public String getFieldName() {
        return fieldName;
    }

    @Override public String getDefaultValue() {
        return defaultValue;
    }

}
