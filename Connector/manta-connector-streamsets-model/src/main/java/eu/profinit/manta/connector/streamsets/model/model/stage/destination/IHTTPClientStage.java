package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IHTTPHeadersComponent;

import java.util.List;

/**
 * @author mburdel
 */
public interface IHTTPClientStage extends IStage {

    /**
     *
     * @return HTTP resource URL
     */
    String getResourceUrl();

    /**
     *
     * @return username
     */
    String getUsername();

    /**
     *
     * @return headers
     */
    List<? extends IHTTPHeadersComponent> getHeaders();

    /**
     *
     * @return HTTP method
     */
    String getHttpMethod();

}
