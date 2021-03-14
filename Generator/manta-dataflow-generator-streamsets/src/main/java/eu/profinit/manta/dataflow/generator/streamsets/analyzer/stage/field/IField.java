package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.Node;

import java.util.List;

/**
 * Stage's Field (Column)
 *
 * @author mburdel
 */
public interface IField {

    /**
     * "/" root field
     *
     * @return field path
     */
    String getFieldPath();

    /**
     *
     * @return field name
     */
    String getName();

    /**
     *
     * @return field type
     */
    EFieldType getFieldType();

    /**
     * Adds created field with fieldPath and fieldType into this field. Creates children if needed.
     *
     * Usage only with root stage's field during analyzing fields.
     *
     * @param fieldPath field path
     * @param fieldType field type
     * @return added field
     */
    Field add(String fieldPath, EFieldType fieldType);

    /**
     * Method compares fields and creates missing children in source and target root field.
     * Depending on the order: Source root field calls method with target root field as parameter.
     * Result is the duplicity of two fields with field's types' differences.
     *
     * @param targetField target field
     * @return <code>true</code> if one of these field were changed else <code>false</code> fields weren't edited
     */
    boolean merge(Field targetField);

    /**
     * Method creates structured node with attributes.
     *
     * @param parent for root field
     * @param gh graph helper
     */
    void createNodes(Node parent, StreamSetsGraphHelper gh);

    /**
     * Usage from root field
     * @return list of leafs
     */
    List<Field> getOutputLeafs();

    /**
     * Usage from root field
     *
     * @param fieldPath of wanted field
     * @return found field <code>null</code>
     */
    Field findFieldByPath(String fieldPath);
}
