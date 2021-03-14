package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * @author mburdel
 */
public interface IHTTPHeadersComponent extends IStageComponent {

    String getKey();

    String getValue();
}
