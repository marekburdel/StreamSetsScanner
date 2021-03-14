package eu.profinit.manta.connector.streamsets.extractor;

import eu.profinit.manta.connector.streamsets.extractor.exception.StreamSetsExtractorException;
import eu.profinit.manta.platform.automation.AbstractTask;
import eu.profinit.manta.platform.automation.Null;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Common base for StreamSets extractors.
 *
 * @author mburdel
 *
 */
public abstract class AbstractExtractor extends AbstractTask<Null, Null> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExtractor.class);

    private String includePipelines;

    private String includeLabels;

    private String excludePipelines;

    private String excludeLabels;

    /** Output File for extraction. */
    private File outputFile;

    /** Whether the output should be initialized before extraction. */
    private boolean disableOutputInit = false;

    private boolean outputCleared = false;

    @Override protected void doExecute(Null input, Null output) {
        extract();
    }

    /**
     * Runs extraction queries and stores result into files.
     */
    protected abstract void extract();

    /**
     * Method called at the end of the extraction to close all connections.
     */
    protected abstract void close();

    /**
     * Delete and recreate output directory.
     */
    void prepareOutput() {
        if (!disableOutputInit && !outputCleared) {
            LOGGER.info("Deleting old files...");
            FileUtils.deleteQuietly(getOutputFile());
            outputCleared = true;
        }
        prepareDirectory(getOutputFile());
    }

    /**
     *
     * @param directory directory to prepare
     */
    void prepareDirectory(File directory) {
        if (!directory.isDirectory() && !directory.mkdirs()) {
            LOGGER.warn("Cannot create the output directory {} for StreamSets extraction.", directory);
        }

        boolean rightsResult = directory.setReadable(true, false);
        rightsResult &= directory.setWritable(true, false);
        if (!rightsResult) {
            LOGGER.debug("Cannot set read or write rights on the output directory {}.", directory);
        }
    }

    /**
     * Extract list of pipelines
     *
     * @return List of Pipeline DAO
     * @throws StreamSetsExtractorException is thrown when problem while connecting to StreamSets Server occurs.
     */
    protected abstract List<PipelineDAO> extractListOfPipelines() throws StreamSetsExtractorException;

    /**
     * Extract pipeline by Pipeline Id by GET method to outputFile
     *
     * @param pipelineId pipeline's id
     */
    protected abstract void extractPipelineById(String pipelineId);

    /**
     *
     * @return the output directory where extraction of a single server will be stored
     */
    File getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the output directory where extraction of a single server will be stored
     *
     * @param outputFile the output directory
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     *
     * @param disableOutputInit <code>true</code> if output should not be initialized before extraction,
     *            <code>false</code> (default) if output data should be deleted before extraction
     */
    public void setDisableOutputInit(boolean disableOutputInit) {
        this.disableOutputInit = disableOutputInit;
    }

    protected String getIncludePipelines() {
        return includePipelines;
    }

    public void setIncludePipelines(String includePipelines) {
        this.includePipelines = includePipelines;
    }

    protected String getIncludeLabels() {
        return includeLabels;
    }

    public void setIncludeLabels(String includeLabels) {
        this.includeLabels = includeLabels;
    }

    protected String getExcludePipelines() {
        return excludePipelines;
    }

    public void setExcludePipelines(String excludePipelines) {
        this.excludePipelines = excludePipelines;
    }

    protected String getExcludeLabels() {
        return excludeLabels;
    }

    public void setExcludeLabels(String excludeLabels) {
        this.excludeLabels = excludeLabels;
    }
}
