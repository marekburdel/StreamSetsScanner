package eu.profinit.manta.connector.streamsets.model.model;

import java.util.Map;

/**
 * StreamSets DataCollector Configuration - json object.
 *
 * @author mburdel
 */

public interface IConfiguration {

    /**
     *
     * @return pipeline's parameters map
     */
    Map<String, String> getParameters();
}
