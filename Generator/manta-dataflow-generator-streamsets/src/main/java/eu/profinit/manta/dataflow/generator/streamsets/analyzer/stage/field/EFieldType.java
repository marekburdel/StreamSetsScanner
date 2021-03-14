package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

/**
 * @author mburdel
 */
public enum EFieldType {
    /**
     * input
     */
    I,

    /**
     * output
     */
    O,

    /**
     * input/output
     */
    IO,

    /** Similar to output, but wont create flow between source (any type)field and target begin field.
     If some field is overwritten these fields will not match together with flow.

     Opposite to EFieldType.I
     */
    BEGIN,

    /**
     * Exists only during analyzing. In final result shouldn't appear.
     */
    UNKNOWN
}
