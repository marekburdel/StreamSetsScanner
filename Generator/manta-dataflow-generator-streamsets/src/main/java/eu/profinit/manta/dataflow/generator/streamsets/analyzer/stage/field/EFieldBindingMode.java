package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

/**
 * @author mburdel
 */
public enum EFieldBindingMode {
    /**
     * Normal merge (documented in confluence dev Scanners - StreamSets)
     */
    MERGE,

    /**
     * FieldType.I to FieldType.O and FieldType.O to FieldType.I
     */
    MERGE_RENAMER_REVERSED,

    /**
     * During Fields' Expansion input field with FieldType.? will expand as a new field with FieldType.I
     */
    MERGE_REMOVER_KEEP,

    /**
     * Merge Mode for Expression Evaluator / EL
     */
    MERGE_EL,

}
