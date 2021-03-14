package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

/**
 * Class to store field's attribute information.
 * @author mburdel
 */
public class FieldAttribute {
    private Field field;
    private String key;
    private String value;

    public FieldAttribute(Field field, String key, String value) {
        this.field = field;
        this.key = key;
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
