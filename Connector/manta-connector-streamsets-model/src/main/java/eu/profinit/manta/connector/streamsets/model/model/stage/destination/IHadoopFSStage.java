package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IHadoopFSStage extends IStage {

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
     * @return File System URI for the underlying Hadoop file system
     */
    String getHdfsUri();

    /**
     *
     * @return files directory
     */
    String getDirectoryTemplate();
}
