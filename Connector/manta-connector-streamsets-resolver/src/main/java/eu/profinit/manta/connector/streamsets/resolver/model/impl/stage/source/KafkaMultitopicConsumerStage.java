package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IKafkaMultitopicConsumerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class KafkaMultitopicConsumerStage extends Stage implements IKafkaMultitopicConsumerStage {

    private String brokerUri;

    private String consumerGroup;

    private List<String> topics;

    public KafkaMultitopicConsumerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, String brokerUri,
            String consumerGroup, List<String> topics) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.brokerUri = brokerUri;
        this.consumerGroup = consumerGroup;
        this.topics = topics;
    }

    @Override public String getBrokerUri() {
        return brokerUri;
    }

    @Override public String getConsumerGroup() {
        return consumerGroup;
    }

    @Override public List<String> getTopics() {
        return topics;
    }

}
