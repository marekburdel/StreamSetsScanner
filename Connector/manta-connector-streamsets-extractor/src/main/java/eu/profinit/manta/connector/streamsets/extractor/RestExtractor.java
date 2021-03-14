package eu.profinit.manta.connector.streamsets.extractor;

import eu.profinit.manta.connector.streamsets.extractor.exception.ProblemCause;
import eu.profinit.manta.connector.streamsets.extractor.exception.StreamSetsExtractorException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Rest Extractor for StreamSets
 *
 * @author mburdel
 */
public class RestExtractor extends AbstractExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExtractor.class);
    private static final String JSON_SUFFIX = ".json";
    private static final String ALL_PIPELINE_CONFIGURATION_INFO_PATH = "/rest/v1/pipelines/";
    private static final String EXPORT_PIPELINE_ID_PREFIX = "/rest/v1/pipeline/";
    private static final String EXPORT_PIPELINE_ID_SUFFIX = "/export/";

    /** Messages for the user describing a status code received from StreamSets Server. */
    static final Map<Integer, String> STATUS_CODE_MESSAGES = new HashMap<>();

    static {
        STATUS_CODE_MESSAGES.put(HttpStatus.SC_BAD_REQUEST, "There was an error in the request sent.");
        STATUS_CODE_MESSAGES.put(HttpStatus.SC_UNAUTHORIZED, "Wrong login credentials were provided.");
        STATUS_CODE_MESSAGES.put(HttpStatus.SC_FORBIDDEN, "Access was forbidden.");
        STATUS_CODE_MESSAGES.put(HttpStatus.SC_NOT_FOUND, "The requested content wasn't found.");
    }

    /** The user name for connection to the StreamSets server. */
    private String username;

    /** The password for connection to the StreamSets server. */
    private String password;

    /** The authorization token for connection to the Control Hub server. */
    private String authToken;

    /** Boolean if Control Hub is enabled. */
    private boolean enabledControlHub;

    /** The address of the StreamSets server. */
    private String address;

    /** The port of the StreamSets server. */
    private int port;

    /** The scheme to connect to the StreamSets server. */
    private String scheme;

    /* Fields initialized during open method  */
    private HttpHost target;
    private CloseableHttpClient httpClient;
    private String authHeader;

    public RestExtractor() {
    }

    public RestExtractor(String username, String password, String address, int port, String scheme) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.port = port;
        this.scheme = scheme;
    }

    @Override protected void extract() {
        prepareOutput();
        try {
            LOGGER.info("Extracting StreamSets objects to {}", getOutputFile().getAbsolutePath());
            if (enabledControlHub) {
                LOGGER.error("Control Hub is not supported.");
            } else {
                open();
                checkCanExecute();
                Set<String> itemsToExtract = analyzeItemsToExtract();
                extractItems(itemsToExtract);
                close();
            }
        } catch (StreamSetsExtractorException e) {
            LOGGER.error("Problem occurred when extracting from StreamSets Server.", e);
        }
    }

    private void extractItems(Set<String> itemsToExtract) {
        LOGGER.info("Pipelines to extract: {}", itemsToExtract.size());
        for (String itemId : itemsToExtract) {
            LOGGER.info("Extracting pipeline: {}", itemId);
            extractPipelineById(itemId);
        }
    }

    private Set<String> analyzeItemsToExtract() throws StreamSetsExtractorException {
        List<PipelineDAO> allPipelines = extractListOfPipelines();
        Set<String> itemsToExtract = new HashSet<>();

        if (getIncludePipelines().isEmpty() && getIncludeLabels().isEmpty()) {
            itemsToExtract.addAll(analyzeAllItemsToExtract(allPipelines));
        } else {
            itemsToExtract.addAll(analyzeItemsToExtractByPipelineId());
            itemsToExtract.addAll(analyzeItemsToExtractByLabel(allPipelines));
        }
        Set<String> itemsToExclude = new HashSet<>();

        itemsToExclude.addAll(analyzeItemsToExcludeByPipelineId());
        itemsToExclude.addAll(analyzeItemsToExcludeByLabel(allPipelines));

        for (String pipelineToExclude : itemsToExclude) {
            itemsToExtract.remove(pipelineToExclude);
        }
        return itemsToExtract;
    }

    private List<String> analyzeItemsToExcludeByLabel(List<PipelineDAO> allPipelines) {
        if (getExcludeLabels().isEmpty()) {
            return Collections.emptyList();
        }
        List<String> labelsToExclude = Arrays.asList(getExcludeLabels().split("\\s*,\\s*"));
        Set<String> labels = new HashSet<>(labelsToExclude);
        List<String> pipelinesToExclude = new ArrayList<>();

        for (PipelineDAO pipeline : allPipelines) {
            for (String pipelineLabel : pipeline.getLabels()) {
                if (labels.contains(pipelineLabel)) {
                    pipelinesToExclude.add(pipeline.getPipelineId());
                }
            }
        }
        return pipelinesToExclude;
    }

    private List<String> analyzeItemsToExcludeByPipelineId() {
        if (getExcludePipelines().isEmpty()) {
            return Collections.emptyList();
        }
        String[] pipelinesIdsToExclude = getExcludePipelines().split("\\s*,\\s*");
        return Arrays.asList(pipelinesIdsToExclude);

    }

    private List<String> analyzeAllItemsToExtract(List<PipelineDAO> allPipelines) {
        List<String> pipelines = new ArrayList<>();
        for (PipelineDAO pipelineDAO : allPipelines) {
            pipelines.add(pipelineDAO.getPipelineId());
        }
        return pipelines;
    }

    private List<String> analyzeItemsToExtractByPipelineId() {
        if (getIncludePipelines().isEmpty()) {
            return Collections.emptyList();
        }
        String[] pipelinesIdsToExtract = getIncludePipelines().split("\\s*,\\s*");
        return Arrays.asList(pipelinesIdsToExtract);
    }

    private List<String> analyzeItemsToExtractByLabel(List<PipelineDAO> allPipelines) {
        if (getIncludeLabels().isEmpty()) {
            return Collections.emptyList();
        }
        List<String> labelsToExtract = Arrays.asList(getIncludeLabels().split("\\s*,\\s*"));
        Set<String> labels = new HashSet<>(labelsToExtract);
        List<String> pipelinesToExtract = new ArrayList<>();

        for (PipelineDAO pipeline : allPipelines) {
            for (String pipelineLabel : pipeline.getLabels()) {
                if (labels.contains(pipelineLabel)) {
                    pipelinesToExtract.add(pipeline.getPipelineId());
                }
            }
        }
        return pipelinesToExtract;
    }

    /**
     * Method called at the beginning of the extraction to open all connections and set up authorization header.
     */
    private void open() {
        httpClient = HttpClientBuilder.create().build();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        authHeader = "Basic " + new String(encodedAuth, StandardCharsets.US_ASCII);
        target = new HttpHost(address, port, scheme);
    }

    @Override protected void close() {
        if (httpClient == null) {
            return;
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            LOGGER.warn("Problem when closing HTTP connection to StreamSets Server", e);
        } finally {
            httpClient = null;
        }
    }

    @Override protected List<PipelineDAO> extractListOfPipelines() throws StreamSetsExtractorException {
        List<PipelineDAO> result = new ArrayList<>();

        HttpGet httpGet = new HttpGet(ALL_PIPELINE_CONFIGURATION_INFO_PATH);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        try (CloseableHttpResponse response = httpClient.execute(target, httpGet)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw createStatusCodeException(response.getStatusLine().getStatusCode());
            } else {
                HttpEntity entity = response.getEntity();
                Validate.isTrue(
                        entity.getContentType().getValue().startsWith(ContentType.APPLICATION_JSON.getMimeType()),
                        "Expecting JSON response but get %s", entity.getContentType());
                String jsonString = EntityUtils.toString(entity, StandardCharsets.UTF_8.toString());
                if (StringUtils.isBlank(jsonString)) {
                    LOGGER.error("The server response is blank for server {}:\n{}", address, response.toString());
                } else {
                    JSONParser parser = new JSONParser();
                    JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
                    for (Object jsonObject : jsonArray) {
                        PipelineDAO pipelineDAO = new PipelineDAO((JSONObject) jsonObject);
                        result.add(pipelineDAO);
                    }
                }
            }
        } catch (IOException e) {
            throw new StreamSetsExtractorException(ProblemCause.FAILED_HTTP_REQUEST, e);
        } catch (org.json.simple.parser.ParseException | IllegalArgumentException e) {
            throw new StreamSetsExtractorException(ProblemCause.RESPONSE_NOT_IN_JSON_FORMAT, e);
        }
        return result;
    }

    @Override protected void extractPipelineById(String pipelineId) {
        HttpGet httpGet = new HttpGet(EXPORT_PIPELINE_ID_PREFIX + pipelineId + EXPORT_PIPELINE_ID_SUFFIX);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        try (CloseableHttpResponse response = httpClient.execute(target, httpGet)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new IOException(response.toString());
            } else {
                HttpEntity entity = response.getEntity();
                Validate.isTrue(
                        entity.getContentType().getValue().startsWith(ContentType.APPLICATION_JSON.getMimeType()),
                        "Expecting JSON response but get %s", entity.getContentType());
                String fileName = constructPath(pipelineId);
                File outputFile = FileUtils.getFile(getOutputFile(), fileName);
                prepareDirectory(outputFile.getParentFile());
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    entity.writeTo(fos);
                } catch (IOException ex) {
                    LOGGER.error("Cannot write item into file: " + outputFile.getName(), ex);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Cannot extract item from server: http://{}:{}{}. Pipeline '{}' does not exist.", address,
                    port, httpGet.getURI(), pipelineId);
        }
    }

    /**
     * Tests ability of the extractor to connect to a server using current configuration of parameters.
     * @throws StreamSetsExtractorException connection to the StreamSets Server wasn't successful. The exception contains
     * additional info about the cause(s) of the failure.
     */
    public void testConnection() throws StreamSetsExtractorException {
        open();
        checkCanExecute();
        extractListOfPipelines();
        close();
    }

    /**
     * Does the same as {@link RestExtractor#canExecute()} but it throws an exception in case of fail.
     * @throws StreamSetsExtractorException any of the mandatory fields wasn't specified
     */
    private void checkCanExecute() throws StreamSetsExtractorException {
        Set<ProblemCause> problemCauses = new HashSet<>();
        if (StringUtils.isEmpty(username)) {
            problemCauses.add(ProblemCause.EMPTY_USERNAME);
        }
        if (StringUtils.isEmpty(password)) {
            problemCauses.add(ProblemCause.EMPTY_PASSWORD);
        }
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

        if (target.getHostName() == null) {
            problemCauses.add(ProblemCause.EMPTY_URI);
        } else if (!urlValidator.isValid(target.toURI())) {
            problemCauses.add(ProblemCause.BAD_URI);
        }
        if (!problemCauses.isEmpty()) {
            throw new StreamSetsExtractorException(problemCauses);
        }
    }

    /**
     * Creates an exception caused by invalid status code.
     * @param statusCode the status code received
     * @return new exception with message informing user about the error
     */
    private StreamSetsExtractorException createStatusCodeException(int statusCode) {
        String message = RestExtractor.STATUS_CODE_MESSAGES.get(statusCode);
        if (message == null) {
            message = "Expected status 200 but got " + statusCode + ".";
        }
        return new StreamSetsExtractorException(ProblemCause.UNEXPECTED_RETURN_STATUS, message);
    }

    /**
     * Constructing fileName path.
     *
     * @param name - pipeline's id
     * @return fileName.json
     */
    private String constructPath(String name) {
        return name + JSON_SUFFIX;
    }

    private void loginToControlHub() {
        throw new UnsupportedOperationException("Login to Control Hub is not supported.");
    }

    private void generateAuthToken() {
        throw new UnsupportedOperationException("Login to Control Hub is not supported.");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabledControlHub(boolean enabledControlHub) {
        this.enabledControlHub = enabledControlHub;
    }
}
