package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

/**
 * Argument type in EL function
 *
 * @author mburdel
 */
public enum EFunctionArgType {

    /**
     * Argument in function can be field path
     */
    FIELD_PATH,

    /**
     * Other
     */
    EXPRESSION
}
