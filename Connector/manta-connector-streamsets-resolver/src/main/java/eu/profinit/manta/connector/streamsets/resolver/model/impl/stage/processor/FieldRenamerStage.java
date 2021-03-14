package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IRenameMappingComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldRenamerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.RenameMappingComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldRenamerStage extends Stage implements IFieldRenamerStage {

    private List<RenameMappingComponent> renameMappings;

    public FieldRenamerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, List<RenameMappingComponent> renameMappings) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.renameMappings = renameMappings;
    }

    @Override public List<? extends IRenameMappingComponent> getRenameMapping() {
        return renameMappings;
    }
}
