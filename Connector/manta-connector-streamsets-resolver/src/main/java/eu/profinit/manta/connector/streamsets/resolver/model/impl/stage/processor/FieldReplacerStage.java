package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IReplaceRuleComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldReplacerStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.ReplaceRuleComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldReplacerStage extends Stage implements IFieldReplacerStage {

    private List<ReplaceRuleComponent> replaceRules;

    public FieldReplacerStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, List<ReplaceRuleComponent> replaceRules) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.replaceRules = replaceRules;
    }

    @Override public List<? extends IReplaceRuleComponent> getReplaceRules() {
        return replaceRules;
    }
}
