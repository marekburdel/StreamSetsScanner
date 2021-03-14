package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.commons.testutils.graphvis.GraphUtils;
import eu.profinit.manta.commons.testutils.graphvis.GraphVisUtils;
import eu.profinit.manta.connector.streamsets.StreamSetsJsonReader;
import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.ParserServiceImpl;
import eu.profinit.manta.dataflow.model.Graph;
import eu.profinit.manta.dataflow.model.impl.GraphImpl;
import eu.profinit.manta.dataflow.model.impl.ResourceImpl;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.JSONParser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author mburdel
 */
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class }) public abstract class TestBase {

    private static final String JSON_ENCODING = "UTF-8";

    private static final String TEST_SERVER_NAME = "Default StreamSets' server";

    private static final File TEST_DATA_FLOW_OUTPUT_GRAPH_DIR = FileUtils
            .getFile("./src/test/resources/graph_test/graph_png");

    protected static Logger LOGGER;

    @Autowired public StreamSetsDataflowTask task;

    @Rule public TestName testName = new TestName();

    private StreamSetsJsonReader streamSetsJsonReader;

    @Before
    public void setup() {
        streamSetsJsonReader = new StreamSetsJsonReader();
        streamSetsJsonReader.setJsonEncoding(JSON_ENCODING);
        streamSetsJsonReader.setJsonParser(new JSONParser());
        streamSetsJsonReader.setParserService(new ParserServiceImpl());
    }

    protected enum TestMode {
        COMPARE_EXPECTED_AND_OUTPUT, PRINT_GRAPH, MAKE_NEW_EXPECTED_FILE_AND_PRINT_NEW_GRAPH, CREATE_DIRECTORY_WITH_NEW_AND_OLD_FILES_AND_GRAPHS_FOR_COMPARING, DELETE_DIRECTORY_WITH_NEW_AND_OLD_FILES_AND_GRAPHS_FOR_COMPARING
    }

    protected void provideTest(String pathToJson, String pathToOutputGraphFile, String pathToExpectedGraphFile,
            String pathToGraphVisualisationOutput, TestMode testMode) throws IOException {
        File jsonFile = FileUtils.getFile(pathToJson);
        streamSetsJsonReader.setInputFile(jsonFile);
        if (streamSetsJsonReader.canRead()) {
            IStreamSetsServer server = streamSetsJsonReader.read();

            LOGGER.info("Analyzing server {} with pipeline {}", TEST_SERVER_NAME,
                    server.getPipeline().getPipelineConfig().getPipelineId());

            Graph graph = new GraphImpl(new ResourceImpl("resource", "type", "description"));

            File outputGraphFile = new File(pathToOutputGraphFile);
            File expectedGraphFile = new File(pathToExpectedGraphFile);

            // generate new expected files and print graph
            //             testMode = TestMode.MAKE_NEW_EXPECTED_FILE_AND_PRINT_NEW_GRAPH;

            task.doExecute(server, graph);
            switch (testMode) {
            case COMPARE_EXPECTED_AND_OUTPUT:
                GraphUtils.printGraphToFile(graph, outputGraphFile);
                String actual = FileUtils.readFileToString(outputGraphFile, "UTF-8");
                String expected = FileUtils.readFileToString(expectedGraphFile, "UTF-8");
                assertEquals(expected, actual);
                break;
            case PRINT_GRAPH:
                GraphVisUtils.printGraphImageToFile(graph, new File(pathToGraphVisualisationOutput));
                break;
            case MAKE_NEW_EXPECTED_FILE_AND_PRINT_NEW_GRAPH:
                GraphUtils.printGraphToFile(graph, expectedGraphFile);
                GraphVisUtils.printGraphImageToFile(graph, new File(pathToGraphVisualisationOutput));
                break;
            case CREATE_DIRECTORY_WITH_NEW_AND_OLD_FILES_AND_GRAPHS_FOR_COMPARING: {
                File location = outputGraphFile.getParentFile();
                Path path = Paths.get(location.getPath(), testName.getMethodName());
                String directoryPath = Files.createDirectory(path).toString();

                try {
                    Files.copy(Paths.get(expectedGraphFile.toURI()), Paths.get(directoryPath, "expectedFile.txt"));
                } catch (IOException ex) {
                    LOGGER.error("Some problem during copy operation", ex);
                }

                try {
                    Files.copy(Paths.get(pathToGraphVisualisationOutput), Paths.get(directoryPath, "expectedGraph.png"));
                } catch (IOException ex) {
                    LOGGER.error("Some problem during copy operation", ex);
                }

                GraphUtils.printGraphToFile(graph, new File(directoryPath + "/actualFile.txt"));
                GraphVisUtils.printGraphImageToFile(graph, new File(directoryPath + "/actualGraph.png"));
                break;
            }
            case DELETE_DIRECTORY_WITH_NEW_AND_OLD_FILES_AND_GRAPHS_FOR_COMPARING: {
                File location = outputGraphFile.getParentFile();
                Path path = Paths.get(location.getPath(), testName.getMethodName());
                FileUtils.deleteQuietly(path.toFile());
                break;
            }
            default:
            }
        } else {
            LOGGER.error("File {} was not found.", pathToJson);
        }
    }

    @AfterClass public static void cleanUp() {
        Collection<File> files = FileUtils.listFiles(TEST_DATA_FLOW_OUTPUT_GRAPH_DIR, new String[] { "dot" }, true);

        for (File file : files) {
            if (!file.delete()) {
                LOGGER.warn("Cannot clean up file: " + file.getName());
            }
        }
    }

}
