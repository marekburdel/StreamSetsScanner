package eu.profinit.manta.dataflow.generator.streamsets.el;

import eu.profinit.manta.connector.streamsets.StreamSetsJsonReader;
import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.ParserServiceImpl;
import eu.profinit.manta.dataflow.generator.streamsets.TestBase;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author mburdel
 */
public class ELRuntimeValuesResolverTest extends TestBase {

    private static final String JSON_ENCODING = "UTF-8";

    private static final String STREAMSETS_TEST_FLOW_XML = "spring/streamsets-test-flow.xml";

    private static final String PATH_TO_JSON = "./src/test/resources/runtime_values_test/json/TestRuntimeValuesParameters.json";

    /**
     *  Pipeline's parameters loaded from Json:
     *      KEY                 VALUE
     *  --------------------------------------------------------------------------------
     *      MANTA               getmanta
     *      MANTA_URL           ${runtime:conf('MantaUrl')}
     *      MANTA_USERNAME      ${runtime:loadResource('test_runtime_resource_00.txt', false)}
     */
    private Map<String, String> pipelineParameters;

    private ExpressionLanguageHelper expressionLanguageHelper;

    @Before public void setUp() throws IOException {
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(STREAMSETS_TEST_FLOW_XML);
        expressionLanguageHelper = springContext.getBean(eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper.class);

        StreamSetsJsonReader streamSetsJsonReader = new StreamSetsJsonReader();
        streamSetsJsonReader.setJsonEncoding(JSON_ENCODING);
        streamSetsJsonReader.setJsonParser(new JSONParser());
        streamSetsJsonReader.setParserService(new ParserServiceImpl());
        File jsonFile = FileUtils.getFile(PATH_TO_JSON);
        streamSetsJsonReader.setInputFile(jsonFile);
        if (streamSetsJsonReader.canRead()) {
            IStreamSetsServer server = streamSetsJsonReader.read();
            pipelineParameters = server.getPipeline().getPipelineConfig().getConfiguration().getParameters();
        }
    }

    @Test public void test_RuntimeParameters_CorrectInput_00() {
        String parameterExpression = "${MANTA}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("getmanta", replacement);
    }

    @Test public void test_RuntimeParameters_CorrectInput_01() {
        String parameterExpression = "${MANTA}.com/${MANTA}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("getmanta.com/getmanta", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_00() {
        String parameterExpression = "${record:value(MANTA)}/${Unknown_parameter}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("getmanta/Unknown_parameter", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_01() {
        String parameterExpression = "${record:value(Unknown_parameter)}/${Unknown_parameter}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("Unknown_parameter/Unknown_parameter", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_02() {
        String parameterExpression = "${record:value('delete_me')}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("record:value(\"delete_me\")", replacement);
    }

    @Test public void test_RuntimeParameters_WithRuntimeProperties() {
        String parameterExpression = "${record:value(MANTA_URL)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("https://getmanta.com/", replacement);
    }

    @Test public void test_RuntimeParameters_WithRuntimeResources() {
        String parameterExpression = "${record:value(MANTA_USERNAME)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, false);
        Assert.assertEquals("manta_username", replacement);
    }

    @Test public void test_runtimeProperties_CorrectInput_00() {
        String runtimePropertiesExpression = "${runtime:conf('HDFSDirTemplate')}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, false);
        Assert.assertEquals("/HDFS/DirectoryTemplate", replacement);
    }

    @Test public void test_runtimeProperties_CorrectInput_01() {
        String runtimePropertiesExpression = "/tmp${runtime:conf('HDFSDirTemplate')}/logfiles";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, false);
        Assert.assertEquals("/tmp/HDFS/DirectoryTemplate/logfiles", replacement);
    }

    @Test public void test_runtimeProperties_InCorrectInput() {
        String runtimePropertiesExpression = "/tmp${runtime:conf('not_found')}/logfiles";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, false);
        Assert.assertEquals("/tmpruntime:conf(\"not_found\")/logfiles", replacement);
    }

    @Test public void test_runtimeResources_CorrectInput() {
        String runtimeResourcesExpression = "${runtime:loadResource(\"test_runtime_resource_01.txt\", true)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimeResourcesExpression, pipelineParameters, false);
        Assert.assertEquals("manta_password", replacement);
    }

    @Test public void test_runtimeResources_InCorrectInput_MissingFile() {
        String runtimeResourcesExpression = "${runtime:loadResource(\"test_not_found_resource_00.txt\", true)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimeResourcesExpression, pipelineParameters, false);
        Assert.assertEquals("runtime:loadResource(\"test_not_found_resource_00.txt\", true)", replacement);
    }

    @Test public void test_runtimeResources_InCorrectInput_MissingFile_ReplacingInFunction() {
        String expressionString = "${str:truncate(runtime:loadResource(\"unknown.txt\", false))}";
        String removedELString = expressionLanguageHelper
                .replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("str:truncate(runtime:loadResource(\"unknown.txt\", false))", removedELString);
    }

    @Test public void test_runtimeResources_CorrectInput_ReplacingValueInFunctionArg() {
        String expressionString = "${str:truncate(runtime:loadResource(\"test_runtime_resource_01.txt\", false))}";
        String removedELString = expressionLanguageHelper
                .replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("str:truncate(manta_password)", removedELString);
    }

    @Test public void test_RuntimeParameters_CorrectInput_00_Remove() {
        String parameterExpression = "${MANTA}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("getmanta", replacement);
    }

    @Test public void test_RuntimeParameters_CorrectInput_01_Remove() {
        String parameterExpression = "${MANTA}.com/${MANTA}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("getmanta.com/getmanta", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_00_Remove() {
        String parameterExpression = "${record:value(MANTA)}/${Unknown_parameter}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("getmanta/", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_01_Remove() {
        String parameterExpression = "${record:value(Unknown_parameter)}/${Unknown_parameter}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("/", replacement);
    }

    @Test public void test_RuntimeParameters_UnknownParameter_02_Remove() {
        String parameterExpression = "${record:value('delete_me')}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("", replacement);
    }

    @Test public void test_RuntimeParameters_WithRuntimeProperties_Remove() {
        String parameterExpression = "${record:value(MANTA_URL)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("https://getmanta.com/", replacement);
    }

    @Test public void test_RuntimeParameters_WithRuntimeResources_Remove() {
        String parameterExpression = "${record:value(MANTA_USERNAME)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(parameterExpression, pipelineParameters, true);
        Assert.assertEquals("manta_username", replacement);
    }

    @Test public void test_runtimeProperties_CorrectInput_00_Remove() {
        String runtimePropertiesExpression = "${runtime:conf('HDFSDirTemplate')}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, true);
        Assert.assertEquals("/HDFS/DirectoryTemplate", replacement);
    }

    @Test public void test_runtimeProperties_CorrectInput_01_Remove() {
        String runtimePropertiesExpression = "/tmp${runtime:conf('HDFSDirTemplate')}/logfiles";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, true);
        Assert.assertEquals("/tmp/HDFS/DirectoryTemplate/logfiles", replacement);
    }

    @Test public void test_runtimeProperties_InCorrectInput_Remove() {
        String runtimePropertiesExpression = "/tmp${runtime:conf('not_found')}/logfiles";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimePropertiesExpression, pipelineParameters, true);
        Assert.assertEquals("/tmp/logfiles", replacement);
    }

    @Test public void test_runtimeResources_CorrectInput_Remove() {
        String runtimeResourcesExpression = "${runtime:loadResource(\"test_runtime_resource_01.txt\", true)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimeResourcesExpression, pipelineParameters, true);
        Assert.assertEquals("manta_password", replacement);
    }

    @Test public void test_runtimeResources_InCorrectInput_MissingFile_Remove() {
        String runtimeResourcesExpression = "${runtime:loadResource(\"test_not_found_resource_00.txt\", true)}";
        String replacement = expressionLanguageHelper
                .replaceRuntimeValues(runtimeResourcesExpression, pipelineParameters, true);
        Assert.assertEquals("", replacement);
    }

    @Test public void test_runtimeResources_InCorrectInput_MissingFile_ReplacingInFunction_Remove() {
        String expressionString = "${str:truncate(runtime:loadResource(\"unknown.txt\", false))}";
        String removedELString = expressionLanguageHelper
                .replaceRuntimeValues(expressionString, pipelineParameters, true);
        Assert.assertEquals("", removedELString);
    }

    @Test public void test_runtimeResources_CorrectInput_ReplacingValueInFunctionArg_Remove() {
        String expressionString = "${str:truncate(runtime:loadResource(\"test_runtime_resource_01.txt\", false))}";
        String removedELString = expressionLanguageHelper
                .replaceRuntimeValues(expressionString, pipelineParameters, true);
        Assert.assertEquals("", removedELString);
    }

    @Test public void test_RemoveELFromStringTest_00() {
        String expressionString = "manta${'manta'}manta${record:value('/age') + 1}";
        String removedELString = expressionLanguageHelper.replaceRuntimeValues(expressionString, null, true);
        Assert.assertEquals("mantamanta", removedELString);
    }

    @Test public void test_RemoveELFromStringTest_01() {
        String expressionString = "manta";
        String removedELString = expressionLanguageHelper.replaceRuntimeValues(expressionString, null, true);
        Assert.assertEquals("manta", removedELString);
    }

    @Test public void test_strtrim_function_withString() {
        String expressionString = "${str:trim(\" Manta Manta   \")}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, null, false);
        Assert.assertEquals("Manta Manta", replacement);
    }

    @Test public void test_strtrim_function_runtimeParameters_00() {
        String expressionString = "${str:trim(MANTA)}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("getmanta", replacement);
    }

    @Test public void test_strtrim_function_runtimeParameters_01() {
        String expressionString = "${str:trim(record:value(MANTA_URL))}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("https://getmanta.com/", replacement);
    }

    @Test public void test_strtrim_function_runtimeParameters_02() {
        String expressionString = "${str:trim(MANTA_USERNAME)}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("manta_username", replacement);
    }

    @Test public void test_strtrim_function_runtimeProperties() {
        String expressionString = "FS${str:trim(runtime:conf('HDFSDirTemplate'))}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("FS/HDFS/DirectoryTemplate", replacement);
    }

    @Test public void test_strtrim_function_runtimeResources_00() {
        String expressionString = "${str:trim(runtime:loadResource(\"test_runtime_resource_02.txt\", false))}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("Manta File System", replacement);
    }

    @Test public void test_strtrim_function_runtimeResources_01() {
        String expressionString = "something_here/${str:trim(runtime:loadResource(\"test_runtime_resource_02.txt\", false))}";
        String replacement = expressionLanguageHelper.replaceRuntimeValues(expressionString, pipelineParameters, false);
        Assert.assertEquals("something_here/Manta File System", replacement);
    }

}
