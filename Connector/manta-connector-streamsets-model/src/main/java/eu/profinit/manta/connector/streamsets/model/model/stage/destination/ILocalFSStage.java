package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface ILocalFSStage extends IStage {

    /**
     *
     * @return unique file's prefix
     */
    String getUniquePrefix();

    /**
     *
     * @return file's suffix
     */
    String getFileNameSuffix();

    /**
     *
     * @return directory path
     */
    String getDirPathTemplate();

}
