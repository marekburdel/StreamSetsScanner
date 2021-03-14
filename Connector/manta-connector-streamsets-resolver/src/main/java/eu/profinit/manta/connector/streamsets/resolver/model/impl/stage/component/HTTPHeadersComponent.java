package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHTTPHeadersComponent;

/**
 * @author mburdel
 */
public class HTTPHeadersComponent implements IHTTPHeadersComponent {

    private String key;
    private String value;

    public HTTPHeadersComponent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override public String getKey() {
        return key;
    }

    @Override public String getValue() {
        return value;
    }
}
