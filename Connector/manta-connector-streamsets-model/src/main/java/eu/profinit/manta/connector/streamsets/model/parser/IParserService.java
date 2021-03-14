package eu.profinit.manta.connector.streamsets.model.parser;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import org.json.simple.JSONObject;

/**
 * Interface for Parser Service for processing StreamSets json exports.
 *
 * @author mburdel
 */
public interface IParserService {

    /**
     * Process Json and returns StreamSets Server Model.
     *
     * @param jsonFile Json File from export.
     * @return StreamSets Server.
     */
    IStreamSetsServer processStreamSetsServer(JSONObject jsonFile);
}
