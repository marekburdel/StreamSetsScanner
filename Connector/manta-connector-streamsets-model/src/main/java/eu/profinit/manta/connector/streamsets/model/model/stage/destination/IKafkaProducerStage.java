package eu.profinit.manta.connector.streamsets.model.model.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

/**
 * @author mburdel
 */
public interface IKafkaProducerStage extends IStage {

    /**
     *
     * @return Broker URI (HOST:PORT)
     */
    String getBrokerUri();

    /**
     *
     * @return Topic name
     */
    String getTopic();
}
