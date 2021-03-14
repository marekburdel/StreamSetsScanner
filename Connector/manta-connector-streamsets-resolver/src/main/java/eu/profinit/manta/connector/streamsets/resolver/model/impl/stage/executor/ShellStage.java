package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.executor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IEnvironmentVariableComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.executor.IShellStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.EnvironmentVariableComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class ShellStage extends Stage implements IShellStage {

    private String script;
    private List<EnvironmentVariableComponent> environmentVariableComponents;

    public ShellStage(String instanceName, String stageName, IStageUIInfo stageUIInfo, List<String> inputLanes,
            List<String> outputLanes, List<String> eventLanes, String script,
            List<EnvironmentVariableComponent> environmentVariableComponents) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.script = script;
        this.environmentVariableComponents = environmentVariableComponents;
    }

    @Override public String getScript() {
        return script;
    }

    @Override public List<? extends IEnvironmentVariableComponent> getEnvironmentVariableComponents() {
        return environmentVariableComponents;
    }
}
