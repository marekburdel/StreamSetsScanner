import eu.profinit.manta.connector.streamsets.extractor.RestExtractor;
import eu.profinit.manta.connector.streamsets.extractor.exception.StreamSetsExtractorException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author mburdel
 */
public class RestExtractorTest {

    private RestExtractor restExtractor;

    private File outputFile;

    @Before public void setUp() {
        outputFile = new File("target/test-classes/extracted_pipelines");

        restExtractor = new RestExtractor("guest", "guest", "192.168.0.24", 18630, "http");

        restExtractor.setOutputFile(outputFile);
        restExtractor.setEnabledControlHub(false);
        restExtractor.setDisableOutputInit(false);

        restExtractor.setIncludePipelines(StringUtils.EMPTY);
        restExtractor.setIncludeLabels(StringUtils.EMPTY);
        restExtractor.setExcludePipelines(StringUtils.EMPTY);
        restExtractor.setExcludeLabels(StringUtils.EMPTY);
    }

    @Test public void test_testConnection() throws StreamSetsExtractorException {
        restExtractor.testConnection();
    }

    @Test public void test_ExtractPipelineById() {
        restExtractor.setIncludePipelines("ExpressionEvaluatorExpressionOrder02d132cab7-b4e8-4431-903e-d2077738eccf");
        restExtractor.execute(null, null);

        Assert.assertEquals(1, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelinesByIds() {
        restExtractor.setIncludePipelines("ExpressionEvaluatorExpressionOrder02d132cab7-b4e8-4431-903e-d2077738eccf, "
                + "TestLocalFS0153b1ad3e-f1cd-4a29-84f4-5cc91303ed94, " + "unknown_pipeline_id, "
                + "IncorrectFieldPathTest8dcd97da-77ba-4dc5-8861-8a3a06be7eaf");
        restExtractor.execute(null, null);

        Assert.assertEquals(3, getListOfFilesLength());
    }

    @Test public void test_ExtractUnknownPipelineById() {
        restExtractor.setIncludePipelines("unknown_pipeline_id");
        restExtractor.execute(null, null);

        Assert.assertEquals(0, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelinesByUnknownLabel() {
        restExtractor.setIncludeLabels("unknown_label");
        restExtractor.execute(null, null);

        Assert.assertEquals(0, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelinesByLabel_00() {
        restExtractor.setIncludeLabels("Test");
        restExtractor.execute(null, null);

        Assert.assertEquals(4, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelinesByLabel_01() {
        restExtractor.setIncludeLabels("Tutorial");
        restExtractor.execute(null, null);

        Assert.assertEquals(2, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelinesByIdAndLabel_withCollision() {
        restExtractor.setIncludeLabels("Test, unknown_label, Tutorial");
        restExtractor.setIncludePipelines(
                "IncorrectFieldPathTest8dcd97da-77ba-4dc5-8861-8a3a06be7eaf, " + "unknown_pipeline_id, "
                        + "ExpressionEvaluatorExpressionOrder02d132cab7-b4e8-4431-903e-d2077738eccf");
        restExtractor.execute(null, null);

        Assert.assertEquals(7, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelines_withExclude_00() {
        restExtractor.setIncludePipelines("TestRuntimeValuese5a61971-b66f-4459-9a55-7a1429556c1b,"
                + " IncorrectFieldPathTest8dcd97da-77ba-4dc5-8861-8a3a06be7eaf");
        restExtractor.setExcludeLabels("Test");
        restExtractor.setExcludePipelines("IncorrectFieldPathTest8dcd97da-77ba-4dc5-8861-8a3a06be7eaf");
        restExtractor.execute(null, null);

        Assert.assertEquals(0, getListOfFilesLength());
    }

    @Test public void test_ExtractPipelines_withExclude_01() {
        restExtractor.setIncludeLabels("Test, unknown_label");
        restExtractor
                .setExcludePipelines("TestRuntimeValuese5a61971-b66f-4459-9a55-7a1429556c1b, " + "unknown_pipeline_id");
        restExtractor.execute(null, null);

        Assert.assertEquals(3, getListOfFilesLength());
    }

    @Test public void test_ExtractAllPipelines() {
        restExtractor.execute(null, null);

        Assert.assertTrue(getListOfFilesLength() > 50);
    }

    @Test public void test_ExtractAllPipelines_withExclude() {
        restExtractor.setExcludeLabels("Generator-Test, client_pipelines");
        restExtractor.execute(null, null);

        Assert.assertTrue(getListOfFilesLength() < 50);
    }

    private int getListOfFilesLength() {
        File[] listOfFiles = outputFile.listFiles();
        assert listOfFiles != null;
        return listOfFiles.length;
    }
}
