package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.ISchemaGeneratorStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;

import java.util.List;

/**
 * @author mburdel
 */
public class SchemaGeneratorStage extends Stage implements ISchemaGeneratorStage {

    private String schemaName;
    private String attributeName;

    public SchemaGeneratorStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, String schemaName,
            String attributeName) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.schemaName = schemaName;
        this.attributeName = attributeName;
    }

    @Override public String getSchemaName() {
        return schemaName;
    }

    @Override public String getAttributeName() {
        return attributeName;
    }

}
