package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFilterOperation;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor.FieldRemoverStage;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.AbstractStageProcessor;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldRemoverProcessor extends AbstractStageProcessor {
    private EFilterOperation filterOperation;
    private List<String> removeFields;
    private String constant;

    @Override protected void processConfiguration(JSONArray jsonConfiguration) {
        for (Object jsonObject : jsonConfiguration) {
            String configurationName = getStringName(jsonObject);
            switch (configurationName) {
            case "filterOperation":
                filterOperation = EFilterOperation.valueOf((String) getValue(jsonObject));
                break;
            case "fields":
                removeFields = processFields((JSONArray) getValue(jsonObject));
                break;
            case "constant":
                constant = (String) getValue(jsonObject);
                break;
            default: // do nothing
            }
        }
    }

    @Override public IStage processStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, JSONArray jsonConfiguration) {
        processConfiguration(jsonConfiguration);
        return new FieldRemoverStage(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes,
                filterOperation, removeFields, constant);
    }
}
