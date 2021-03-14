package eu.profinit.manta.connector.streamsets.model.model.stage.processor;

/**
 * Enum for Stage - Field Remover
 *
 * @author mburdel
 */
public enum EFilterOperation {

    /** keep listed fields */
    KEEP,

    /** remove listed fields */
    REMOVE,

    /** remove listed fields if null */
    REMOVE_NULL,

    /** remove listed fields if empty string */
    REMOVE_EMPTY,

    /** remove listed fields if null or empty string */
    REMOVE_NULL_EMPTY,

    /** remove listed fields if fields' values are the given constant */
    REMOVE_CONSTANT

}
