package eu.profinit.manta.connector.streamsets.model.parser;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import org.json.simple.JSONObject;

/**
 * Stage Processor processes JSONObject into Stage model.
 *
 * @author mburdel
 */
public interface IStageProcessor {

    /**
     * Process specific configuration that depends on stageName and transforming it into correct Stage model.
     *
     * @param jsonStage json stage
     * @return processed Stage
     */
    IStage process(JSONObject jsonStage);
}
