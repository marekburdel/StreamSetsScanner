package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.destination.KafkaProducerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class KafkaProducerProcessor extends AbstractStageProcessor {

    private String brokerUri;
    private String topic;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "conf.metadataBrokerList":
                brokerUri = (String) getValue(jsonObject);
                break;
            case "conf.topic":
                topic = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new KafkaProducerStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                brokerUri, topic);
    }
}
