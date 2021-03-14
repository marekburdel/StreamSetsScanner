package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldBindingMode.*;

/**
 * This class stores and manages fields (columns) for stage.
 * @author mburdel
 */
public class Field implements IField {

    private static final String FIELD_PREFIX = "/";

    /**
     * Field's path. Identifier for field (in field group / stage). Comparision is case-insensitive.
     */
    private String fieldPath;

    /**
     * Last name from parsed field's path.
     */
    private String name;

    /**
     * Field type defines (with binding mode) how field will be merged.
     */
    private EFieldType fieldType;

    /**
     * Field's children. Key is field's name (not field's path)
     */
    private Map<String, Field> children;

    /**
     * Parser for field. Generally slash is delimiter (exceptions with single / double quotes) between fields' names.
     */
    private FieldParserService parserService;

    public Field(String fieldPath, EFieldType fieldType, FieldParserService parserService) {
        this.fieldPath = fieldPath;
        this.name = parserService.constructFieldName(fieldPath);
        this.fieldType = fieldType;
        this.parserService = parserService;
        this.children = new HashMap<>();
    }

    @Override public String getFieldPath() {
        return fieldPath;
    }

    @Override public String getName() {
        return name;
    }

    @Override public EFieldType getFieldType() {
        return fieldType;
    }

    @Override public List<Field> getOutputLeafs() {
        List<Field> leafs = new ArrayList<>();
        if (!this.hasChildren()) {
            if (getFieldType() == EFieldType.IO || getFieldType() == EFieldType.O
                    || getFieldType() == EFieldType.BEGIN) {
                leafs.add(this);
            }
        } else {
            for (Field field : children.values()) {
                leafs.addAll(field.getOutputLeafs());
            }
        }
        return leafs;
    }

    @Override public Field findFieldByPath(String fieldPath) {
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);
        if (parsedFieldPath == null) {
            return null;
        }

        Field child = this;
        for (int index = 0; index < parsedFieldPath.size(); ++index) {
            if (index == 0) {
                if (fieldPath.equals("/")) {
                    return this;
                }
            } else {
                if (child == null || !child.hasChildren()) {
                    return null;
                } else {
                    child = child.getChild(parsedFieldPath.get(index));
                }
            }
        }

        if (child != null && child.getName().equalsIgnoreCase(parsedFieldPath.get(parsedFieldPath.size() - 1))) {
            return child;
        } else {
            return null;
        }
    }

    @Override public Field add(String fieldPath, EFieldType fieldType) {
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);
        if (parsedFieldPath == null) {
            return null;
        }

        Field parent = this;
        Field child = null;

        StringBuilder prefixBuf = new StringBuilder();

        for (int index = 0; index < parsedFieldPath.size(); ++index) {
            if (index == 0) {
                if (fieldPath.equals("/")) {
                    this.fieldType = fieldType;
                    return this;
                }
            } else {
                if (child != null) {
                    parent = child;
                }
                prefixBuf.append(FIELD_PREFIX).append(parsedFieldPath.get(index));
                child = parent.getChild(parsedFieldPath.get(index));
                if (child == null) {
                    child = new Field(prefixBuf.toString(), fieldType, parserService);
                    parent.addChild(parsedFieldPath.get(index), child);
                }
            }
        }
        return child;
    }

    @Override public void createNodes(Node parent, StreamSetsGraphHelper gh) {
        if (!getFieldPath().equals("/") && !this.hasChildren()) {
            gh.buildNode(this, getFieldPath(), NodeType.FIELD, parent);
        }
        for (Field child : getChildren().values()) {
            child.createNodes(parent, gh);
        }
    }

    @Override public boolean merge(Field targetField) {
        boolean edited = false;
        for (Field sourceChild : this.children.values()) {
            if (targetField.fieldType == EFieldType.BEGIN) {
                continue;
            }
            Field targetChild = targetField.getChild(sourceChild.getName());

            if (targetChild == null) {
                if (sourceChild.getFieldType() == EFieldType.I) {
                    continue;
                }
                targetChild = createTargetChildField(targetField, sourceChild, MERGE);
                edited = true;
            }
            if (sourceChild.merge(targetChild)) {
                edited = true;
            }
        }
        for (Field targetChild : targetField.children.values()) {
            Field sourceChild = this.getChild(targetChild.getName());
            if (sourceChild == null) {
                if (targetChild.getFieldType() == EFieldType.O || targetChild.getFieldType() == EFieldType.BEGIN) {
                    continue;
                }
                sourceChild = createSourceChildField(targetChild, MERGE);
                edited = true;
            }
            if (sourceChild.merge(targetChild)) {
                edited = true;
            }
        }
        return edited;
    }

    /**
     * Method compares fields and creates missing children in source and target root field. Binding mode is reversed.
     * @param targetField target field
     * @return <code>true</code> if one of these field were changed else <code>false</code> fields weren't edited
     */
    public boolean mergeWithReversedFieldType(Field targetField) {
        boolean edited = false;
        for (Field sourceChild : this.children.values()) {
            Field targetChild = targetField.getChild(sourceChild.getName());
            if (targetChild == null) {
                targetChild = createTargetChildField(targetField, sourceChild, MERGE_RENAMER_REVERSED);
                edited = true;
            }
            if (sourceChild.mergeWithReversedFieldType(targetChild)) {
                edited = true;
            }
        }
        for (Field targetChild : targetField.children.values()) {
            Field sourceChild = this.getChild(targetChild.getName());
            if (sourceChild == null) {
                sourceChild = createSourceChildField(targetChild, MERGE_RENAMER_REVERSED);
                edited = true;
            }
            if (sourceChild.mergeWithReversedFieldType(targetChild)) {
                edited = true;
            }
        }
        return edited;
    }

    /**
     * Method compares fields and creates missing children in source and target root field. Binding mode is set to
     * do not create unwanted fields from source field. These fields shouldn't pass through (should be removed).
     * List of fields that can pass through stage is already set.
     * @param targetField target field
     * @return <code>true</code> if one of these field were changed else <code>false</code> fields weren't edited
     */
    public boolean mergeKeep(Field targetField) {
        boolean edited = false;
        for (Field sourceChild : this.children.values()) {
            Field targetChild = targetField.getChild(sourceChild.getName());
            if (targetChild == null) {
                if (sourceChild.getFieldType() == EFieldType.I) {
                    continue;
                }
                targetChild = createTargetChildField(targetField, sourceChild, MERGE_REMOVER_KEEP);
                edited = true;
            }
            if (sourceChild.mergeKeep(targetChild)) {
                edited = true;
            }
        }
        for (Field targetChild : targetField.children.values()) {
            Field sourceChild = this.getChild(targetChild.getName());
            if (sourceChild == null) {
                if (targetChild.getFieldType() == EFieldType.O) {
                    continue;
                }
                sourceChild = createSourceChildField(targetChild, MERGE_REMOVER_KEEP);
                edited = true;
            }
            if (sourceChild.mergeKeep(targetChild)) {
                edited = true;
            }
        }
        return edited;
    }

    /**
     * Method compares fields and creates missing children in source and target root field. Binding mode is set to
     * EL's behaviour from Expression Evaluator Stage. Specially it merges new created output field
     * with found field in dependency from EL.
     * @param targetField target field
     * @return <code>true</code> if one of these field were changed else <code>false</code> fields weren't edited
     */
    public boolean mergeEL(Field targetField) {
        boolean edited = false;
        for (Field sourceChild : this.children.values()) {
            Field targetChild = targetField.getChild(sourceChild.getName());
            if (targetChild == null) {
                if (sourceChild.getFieldType() == EFieldType.I) {
                    continue;
                }
                targetChild = createTargetChildField(targetField, sourceChild, MERGE_EL);
                edited = true;
            }
            if (sourceChild.mergeEL(targetChild)) {
                edited = true;
            }
        }
        for (Field targetChild : targetField.children.values()) {
            Field sourceChild = this.getChild(targetChild.getName());
            if (sourceChild == null) {
                if (targetChild.getFieldType() != EFieldType.BEGIN) {
                    targetChild.fieldType = EFieldType.BEGIN;
                }
                sourceChild = createSourceChildField(targetChild, MERGE_EL);
                edited = true;
            }
            if (sourceChild.mergeEL(targetChild)) {
                edited = true;
            }
        }
        return edited;
    }

    /**
     * Alternative method for add field (child) for Field Renamer
     * @param fieldPath field path
     * @param fieldType field type
     * @return added Field child
     */
    public Field addRenamerField(String fieldPath, EFieldType fieldType) {
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);
        if (parsedFieldPath == null) {
            return null;
        }

        Field parent = this;
        Field child = null;

        StringBuilder prefixBuf = new StringBuilder();

        for (int index = 0; index < parsedFieldPath.size(); ++index) {
            if (index == 0) {
                if (fieldPath.equals("/")) {
                    this.fieldType = fieldType;
                    return this;
                }
            } else {
                if (child != null) {
                    // Field Renamer behavior
                    child.fieldType = EFieldType.IO;
                    parent = child;
                }
                prefixBuf.append(FIELD_PREFIX).append(parsedFieldPath.get(index));
                child = parent.getChild(parsedFieldPath.get(index));
                if (child == null) {
                    child = new Field(prefixBuf.toString(), fieldType, parserService);
                    parent.addChild(parsedFieldPath.get(index), child);
                }
            }
        }
        return child;
    }

    private Field createSourceChildField(Field targetChild, EFieldBindingMode bindingMode) {
        Field child;
        String childPath = this.fieldPath.equals("/") ?
                           FIELD_PREFIX + targetChild.getName() :
                           this.fieldPath + FIELD_PREFIX + targetChild.getName();
        switch (bindingMode) {
        case MERGE:
        case MERGE_REMOVER_KEEP:
        case MERGE_EL:
            child = new Field(childPath, EFieldType.UNKNOWN, parserService);
            // set Field Type
            if (this.fieldType == EFieldType.I) {
                child.fieldType = EFieldType.I;
            } else if (this.fieldType == EFieldType.O) {
                child.fieldType = EFieldType.O;
            } else if (this.fieldType == EFieldType.BEGIN) {
                child.fieldType = EFieldType.BEGIN;
            } else {
                child.fieldType = EFieldType.IO;
            }
            break;
        case MERGE_RENAMER_REVERSED:
            child = new Field(childPath, EFieldType.I, parserService);
            break;
        default:
            throw new IllegalStateException();
        }
        this.addChild(child.getName(), child);
        return child;
    }

    private Field createTargetChildField(Field targetField, Field sourceChild, EFieldBindingMode bindingMode) {
        Field child;
        String childPath = targetField.fieldPath.equals("/") ?
                           FIELD_PREFIX + sourceChild.getName() :
                           targetField.fieldPath + FIELD_PREFIX + sourceChild.getName();
        switch (bindingMode) {
        case MERGE:
            child = new Field(childPath, EFieldType.UNKNOWN, parserService);
            // set Field Type
            if (sourceChild.getFieldType() == EFieldType.O || sourceChild.getFieldType() == EFieldType.BEGIN) {
                child.fieldType = EFieldType.O;
            } else {
                child.fieldType = EFieldType.IO;
            }
            if (targetField.fieldType == EFieldType.I) {
                child.fieldType = EFieldType.I;
            } else if (targetField.fieldType == EFieldType.O) {
                child.fieldType = EFieldType.O;
            }
            break;
        case MERGE_REMOVER_KEEP:
            child = new Field(childPath, EFieldType.UNKNOWN, parserService);
            if (targetField.getFieldType() == EFieldType.IO && !targetField.getFieldPath().equals("/")) {
                child.fieldType = EFieldType.IO;
            } else {
                child.fieldType = EFieldType.I;
            }
            break;
        case MERGE_EL:
        case MERGE_RENAMER_REVERSED:
            child = new Field(childPath, EFieldType.BEGIN, parserService);
            break;
        default:
            throw new IllegalStateException();
        }
        targetField.addChild(child.getName(), child);
        return child;
    }

    public Map<String, Field> getChildren() {
        return children;
    }

    /**
     *
     * @return <code>true</code> if field doesn't have children else <code>false</code>
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    /**
     * Method compares field case-insensitive.
     * @param fieldName fields' name
     * @return case-insensitive field or <code>null</code> if field doesn't exist
     */
    public Field getChild(String fieldName) {
        // replace uppercase
        Field field = children.get(fieldName.toLowerCase());
        if (field == null) {
            return null;
        }
        String currentFieldName = field.getName();
        if (currentFieldName.equals(currentFieldName.toUpperCase())) {
            field.name = fieldName;
        }
        return field;
    }

    private void addChild(String fieldName, Field child) {
        children.put(fieldName.toLowerCase(), child);
    }

}
