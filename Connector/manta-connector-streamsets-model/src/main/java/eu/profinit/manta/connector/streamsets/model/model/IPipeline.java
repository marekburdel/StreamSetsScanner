package eu.profinit.manta.connector.streamsets.model.model;

/**
 * StreamSets Pipeline defines pipeline configuration.
 *
 * @author mburdel
 */

public interface IPipeline {

    /**
     *
     * @return Pipeline Configuration (contains pipeline's id, other configuration information and stages)
     */
    IPipelineConfig getPipelineConfig();

//    IPipelineRules getPipelineRules();

//    ILibraryDefinitions getLibraryDefinitions();

}
