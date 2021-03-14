package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IDirectoryStage extends IStage {

    /**
     *
     * @return directory path
     */
    String getDirectoryPath();

    /**
     *
     * @return file name pattern
     */
    String getFilePattern();

}
