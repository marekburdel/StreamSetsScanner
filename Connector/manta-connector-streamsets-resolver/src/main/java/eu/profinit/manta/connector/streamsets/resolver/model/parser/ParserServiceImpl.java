package eu.profinit.manta.connector.streamsets.resolver.model.parser;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.model.model.*;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.parser.IParserService;
import eu.profinit.manta.connector.streamsets.model.parser.IStageProcessor;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.*;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.StageType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Implementation of StreamSets ParserService.
 *
 * @author mburdel
 */
public class ParserServiceImpl implements IParserService {

    @Override public IStreamSetsServer processStreamSetsServer(JSONObject jsonFile) {
        return new StreamSetsServer(processPipeline(jsonFile));
    }

    private ITopology processTopology() {
        //TBA
        throw new UnsupportedOperationException("Topology from Control Hub is not supported.");
    }

    private IPipeline processPipeline(JSONObject jsonFile) {
        return new Pipeline(processPipelineConfig((JSONObject) jsonFile.get("pipelineConfig")));
    }

    private IPipelineConfig processPipelineConfig(JSONObject jsonPipelineConfig) {
        PipelineConfig pipelineConfig = new PipelineConfig((String) jsonPipelineConfig.get("pipelineId"),
                (String) jsonPipelineConfig.get("title"), (String) jsonPipelineConfig.get("description"),
                processConfiguration((JSONArray) jsonPipelineConfig.get("configuration")),
                processStages((JSONArray) jsonPipelineConfig.get("stages")));
        pipelineConfig.setLanes(processPipelineLanes(pipelineConfig.getStages()));
        return pipelineConfig;
    }

    private Map<String, ILane> processPipelineLanes(List<IStage> stages) {
        Map<String, ILane> lanes = new HashMap<>();
        Set<String> eventLanes = new HashSet<>();

        for (IStage stage : stages) {
            for (String laneId : stage.getInputLanes()) {
                Lane lane = (Lane) lanes.get(laneId);
                if (lane == null) {
                    lane = new Lane(laneId);
                    lanes.put(laneId, lane);
                }
                lane.addTargetStage(stage);
            }
            for (String laneId : stage.getOutputLanes()) {
                Lane lane = (Lane) lanes.get(laneId);
                if (lane == null) {
                    lane = new Lane(laneId);
                    lanes.put(laneId, lane);
                }
                lane.setSourceStage(stage);
            }
            for (String laneId : stage.getEventLanes()) {
                Lane lane = (Lane) lanes.get(laneId);
                if (lane == null) {
                    lane = new Lane(laneId);
                    lanes.put(laneId, lane);
                }
                lane.setSourceStage(stage);
                eventLanes.add(laneId);
            }
        }
        removeEventLanes(lanes, eventLanes);
        return lanes;
    }

    private void removeEventLanes(Map<String, ILane> lanes, Set<String> eventLanes) {
        for (String laneId : eventLanes) {
            lanes.remove(laneId);
        }
    }

    private IConfiguration processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            if (configurationName.equals("constants")) {
                return new Configuration(processParameters((JSONArray) getValue(jsonObject)));
            }
        }
        return new Configuration(new HashMap<>());
    }

    private Map<String, String> processParameters(JSONArray jsonArray) {
        Map<String, String> parameters = new HashMap<>();
        for (Object jsonObject : jsonArray) {
            String key = (String) ((JSONObject) jsonObject).get("key");
            String value = (String) ((JSONObject) jsonObject).get("value");
            parameters.put(key, value);
        }
        return parameters;
    }

    private List<IStage> processStages(JSONArray jsonStages) {
        List<IStage> stages = new ArrayList<>();
        Set<String> eventLanes = new HashSet<>();
        for (Object jsonStage : jsonStages) {
            IStage stage = processStage((JSONObject) jsonStage);
            eventLanes.addAll(stage.getEventLanes());
            stages.add(stage);
        }
        removeEventStages(stages, eventLanes);
        return stages;
    }

    private void removeEventStages(List<IStage> stages, Set<String> eventLanes) {
        Iterator<IStage> iterator = stages.iterator();
        while (iterator.hasNext()) {
            IStage stage = iterator.next();
            for (String laneId : stage.getInputLanes()) {
                if (eventLanes.contains(laneId)) {
                    iterator.remove();
                }
            }
        }
    }

    private IStage processStage(JSONObject jsonStage) {
        String stageName = (String) jsonStage.get("stageName");
        IStageProcessor stageProcessor = StageType.getStageProcessor(stageName);

        return stageProcessor.process(jsonStage);
    }

    /**
     *
     * @param jsonObject configuration name
     * @return String value
     */
    private String getStringName(Object jsonObject) {
        return (String) ((JSONObject) jsonObject).get("name");
    }

    /**
     *
     * @param jsonObject configuration value
     * @return JSONObject value
     */
    private Object getValue(Object jsonObject) {
        return ((JSONObject) jsonObject).get("value");
    }

}
