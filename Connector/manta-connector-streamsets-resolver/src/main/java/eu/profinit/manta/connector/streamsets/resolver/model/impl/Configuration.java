package eu.profinit.manta.connector.streamsets.resolver.model.impl;

import eu.profinit.manta.connector.streamsets.model.model.IConfiguration;

import java.util.Map;

/**
 *
 * @author mburdel
 */
public class Configuration implements IConfiguration {

    private Map<String, String> parameters;

    public Configuration(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override public Map<String, String> getParameters() {
        return parameters;
    }

}
