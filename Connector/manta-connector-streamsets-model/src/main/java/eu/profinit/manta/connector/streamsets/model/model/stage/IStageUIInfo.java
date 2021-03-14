package eu.profinit.manta.connector.streamsets.model.model.stage;

/**
 * UI Stage Information.
 *
 * @author mburdel
 */
public interface IStageUIInfo {

    enum EStageType {
        SOURCE,
        PROCESSOR,
        TARGET, // DESTINATION
        EXECUTOR
    }

    /**
     *
     * @return Stage's label(name) defined by user or by default.
     */
    String getLabel();

    /**
     *
     * @return type of stage(SOURCE, PROCESSOR, TARGET, EXECUTOR)
     */
    EStageType getStageType();
}