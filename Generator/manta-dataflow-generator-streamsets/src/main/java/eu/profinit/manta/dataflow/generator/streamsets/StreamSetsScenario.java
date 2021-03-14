package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.dataflow.generator.modelutils.GraphScenario;

/**
 * @author mburdel
 */
public class StreamSetsScenario extends GraphScenario<IStreamSetsServer> {

    /**
     * Kód licence pro Manta Flow.
     */
    private static final String MANTA_FLOW_SS_CODE = "mf_streamsets";

    /**
     * Popis produktu Manta Flow StreamSets zobrazený při nedostatečné licenci.
     */
    private static final String MANTA_FLOW_SS_DESCRIPTION = "Manta Flow StreamSets";

    @Override protected boolean isInputCorrect(IStreamSetsServer input) {
        return input != null;
    }

    @Override protected void checkInput(IStreamSetsServer input) {
        if (!checkTechnology(MANTA_FLOW_SS_CODE)) {
            raiseInsufficentLicenseException(MANTA_FLOW_SS_DESCRIPTION);
        }
    }
}
