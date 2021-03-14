package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IKafkaProducerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class KafkaProducerStage extends Stage implements IKafkaProducerStage {

    private String brokerUri;
    private String topic;

    public KafkaProducerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String brokerUri, String topicExpression) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.brokerUri = brokerUri;
        this.topic = topicExpression;
    }

    @Override public String getBrokerUri() {
        return brokerUri;
    }

    @Override public String getTopic() {
        return topic;
    }

}