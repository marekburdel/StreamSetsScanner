package eu.profinit.manta.connector.streamsets.resolver;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.ParserServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author mburdel
 */
public class ParserServiceTest {

    private ParserServiceImpl parserService = new ParserServiceImpl();

    @Test public void parseJson() throws IOException, ParseException {
        File jsonExportFile = new File("src/test/resources/FieldHasher-HashField.json");

        JSONParser parser = new JSONParser();

        FileInputStream is = new FileInputStream("src/test/resources/FieldHasher-HashField.json");
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8.name());
        BufferedReader buffReader = new BufferedReader(isr);

        JSONObject jsonObject = (JSONObject) parser.parse(buffReader);

        IStreamSetsServer streamSetsServer = parserService.processStreamSetsServer(jsonObject);
        streamSetsServer.getPipeline();
    }
}
