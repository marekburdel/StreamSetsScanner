package eu.profinit.manta.connector.streamsets.model.model.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;

import java.util.List;

/**
 * @author mburdel
 */
public interface IKafkaMultitopicConsumerStage extends IStage {

    /**
     *
     * @return Broker URI (HOST:PORT)
     */
    String getBrokerUri();

    /**
     *
     * @return consumer group
     */
    String getConsumerGroup();

    /**
     *
     * @return List of topics to consume
     */
    List<String> getTopics();
}
