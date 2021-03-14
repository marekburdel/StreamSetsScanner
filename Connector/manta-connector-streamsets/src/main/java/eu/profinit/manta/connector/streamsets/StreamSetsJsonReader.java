package eu.profinit.manta.connector.streamsets;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.model.parser.IParserService;
import eu.profinit.manta.platform.automation.AbstractFileInputReader;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


/**
 * Input connector loading StreamSets input as a single json file or directory into JSONObject(s) and transforming it
 * into StreamSets model.
 *
 * @author mburdel
 *
 */
public class StreamSetsJsonReader extends AbstractFileInputReader<IStreamSetsServer> {

    private static final String JSON_SUFFIX = "json";

    private String jsonEncoding;

    private JSONParser jsonParser;

    private IParserService parserService;

    public StreamSetsJsonReader() {
        setFilter(new SuffixFileFilter(JSON_SUFFIX, IOCase.INSENSITIVE));
    }

    @Override protected IStreamSetsServer readFile(File file) throws IOException {
        try (FileInputStream is = new FileInputStream(file.getAbsolutePath());
                InputStreamReader isr = new InputStreamReader(is, jsonEncoding)) {
            BufferedReader buffReader = new BufferedReader(isr);

            JSONObject jsonObject = (JSONObject) jsonParser.parse(buffReader);
            return parserService.processStreamSetsServer(jsonObject);
        } catch (IOException | ParseException e) {
            throw new IOException("Error encountered when parsing json export file: " + file.getName(), e);
        }
    }

    public void setJsonEncoding(String jsonEncoding) {
        this.jsonEncoding = jsonEncoding;
    }

    public void setJsonParser(JSONParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void setParserService(IParserService parserService) {
        this.parserService = parserService;
    }
}
