package eu.profinit.manta.connector.streamsets.extractor;

import eu.profinit.manta.platform.automation.AbstractScenario;
import eu.profinit.manta.platform.automation.Null;

/**
 * @author mburdel
 */
public class ExtractorScenario extends AbstractScenario<Null, Null> {

    @Override
    protected boolean canExecute() {
        return getTasks().length > 0;
    }

    @Override
    protected void doExecute() {
        executeTasks(Null.INSTANCE, Null.INSTANCE);
    }
}
