package eu.profinit.manta.connector.streamsets.model.model.stage.executor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IEnvironmentVariableComponent;

import java.util.List;

/**
 *
 * @author mburdel
 */
public interface IShellStage extends IStage {

    /**
     *
     * @return shell script
     */
    String getScript();

    /**
     *
     * @return list of environment variables
     */
    List<? extends IEnvironmentVariableComponent> getEnvironmentVariableComponents();

}
