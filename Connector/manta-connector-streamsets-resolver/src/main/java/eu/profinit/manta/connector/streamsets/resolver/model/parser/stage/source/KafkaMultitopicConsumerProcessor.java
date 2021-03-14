package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.source.KafkaMultitopicConsumerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mburdel
 */
public class KafkaMultitopicConsumerProcessor extends AbstractStageProcessor {
    private String brokerUri;

    private String consumerGroup;

    private List<String> topics = new ArrayList<>();

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "conf.brokerURI":
                brokerUri = (String) getValue(jsonObject);
                break;
            case "conf.consumerGroup":
                consumerGroup = (String) getValue(jsonObject);
                break;
            case "conf.topicList":
                topics = processFields((JSONArray) getValue(jsonObject));
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new KafkaMultitopicConsumerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes,
                eventLanes, brokerUri, consumerGroup, topics);
    }
}
