package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IHadoopFSStandaloneStage extends IStage {

    /**
     *
     * @return File System URI for the underlying Hadoop file system
     */
    String getHdfsUri();

    /**
     *
     * @return files directory
     */
    String getDirectoryPath();

    /**
     *
     * @return file name pattern (glob or regex)
     */
    String getFilePattern();

}
