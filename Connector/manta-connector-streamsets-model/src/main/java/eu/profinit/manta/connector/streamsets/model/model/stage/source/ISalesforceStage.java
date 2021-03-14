package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface ISalesforceStage extends IStage {

    /**
     *
     * @return SOQL Query
     */
    String getSoqlQuery();

    /**
     *
     * @return username
     */
    String getUsername();

    /**
     *
     * @return SalesForce SOAP API Authentication Endpoint: login.salesforce.com
     */
    String getAuthEndpoint();
}
