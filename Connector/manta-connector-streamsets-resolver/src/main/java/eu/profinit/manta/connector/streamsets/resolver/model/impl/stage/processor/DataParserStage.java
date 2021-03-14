package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IDataParserStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class DataParserStage extends Stage implements IDataParserStage {

    private String fieldPathToParse;
    private String parsedFieldPath;

    public DataParserStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String fieldPathToParse, String parsedFieldPath) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.fieldPathToParse = fieldPathToParse;
        this.parsedFieldPath = parsedFieldPath;
    }

    @Override public String getFieldPathToParse() {
        return fieldPathToParse;
    }

    @Override public String getParsedFieldPath() {
        return parsedFieldPath;
    }

}
