package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.connector.common.connections.ConnectionType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 *
 * @author mburdel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/streamsets-test-flow.xml")
public class StageAnalyzerTest extends DatabaseTestBase {

    @BeforeClass public static void init() {
        LOGGER = LoggerFactory.getLogger(StageAnalyzerTest.class);
        loadContext();

        createDictionary(ConnectionType.ORACLE, "ora1", "ora1");
        createDictionary(ConnectionType.POSTGRESQL, "datasource", "datasource");
        createDictionary(ConnectionType.HIVE, "datasource", "datasource");
    }

    // Query Statements in JDBC Query Consumer Stage
    @Test public void test_JDBCQueryValue_00() throws IOException {
        provideTest("./src/test/resources/json/apostropheTest.json",
                "./src/test/resources/graph_test/apostropheTest_output.txt",
                "./src/test/resources/graph_test/expected_apostropheTest_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_apostropheTest_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    // Complex tests with lot of stages

    @Test public void test_ComplexTest_00() throws IOException {
        provideTest("./src/test/resources/json/ComplexTest_00.json",
                "./src/test/resources/graph_test/ComplexTest_00_output.txt",
                "./src/test/resources/graph_test/expected_ComplexTest_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_ComplexTest_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    // SOURCES

    @Test public void test_JDBCQueryConsumer_00() throws IOException {
        provideTest("./src/test/resources/json/TestJDBCQueryConsumer_00.json",
                "./src/test/resources/graph_test/sources/TestJDBCQueryConsumer_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestJDBCQueryConsumer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestJDBCQueryConsumer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_JDBCQueryConsumer_01() throws IOException {
        provideTest("./src/test/resources/json/TestJDBCQueryConsumer_01.json",
                "./src/test/resources/graph_test/sources/TestJDBCQueryConsumer_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestJDBCQueryConsumer_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestJDBCQueryConsumer_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_JDBCQueryConsumer_02() throws IOException {
        provideTest("./src/test/resources/json/TestJDBCQueryConsumer_02.json",
                "./src/test/resources/graph_test/sources/TestJDBCQueryConsumer_02_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestJDBCQueryConsumer_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestJDBCQueryConsumer_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_OracleCDCClient_00() throws IOException {
        provideTest("./src/test/resources/json/TestOracleCDCClient_00.json",
                "./src/test/resources/graph_test/sources/TestOracleCDCClient_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestOracleCDCClient_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestOracleCDCClient_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_OracleCDCClient_01() throws IOException {
        provideTest("./src/test/resources/json/TestOracleCDCClient_01.json",
                "./src/test/resources/graph_test/sources/TestOracleCDCClient_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestOracleCDCClient_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestOracleCDCClient_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_OracleCDCClient_02() throws IOException {
        provideTest("./src/test/resources/json/TestOracleCDCClient_02.json",
                "./src/test/resources/graph_test/sources/TestOracleCDCClient_02_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestOracleCDCClient_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestOracleCDCClient_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_PostgreSQLCDCClient_00() throws IOException {
        provideTest("./src/test/resources/json/TestPostgreSQLCDCClient_00.json",
                "./src/test/resources/graph_test/sources/TestPostgreSQLCDCClient_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestPostgreSQLCDCClient_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestPostgreSQLCDCClient_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_PostgreSQLCDCClient_01() throws IOException {
        provideTest("./src/test/resources/json/TestPostgreSQLCDCClient_01.json",
                "./src/test/resources/graph_test/sources/TestPostgreSQLCDCClient_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestPostgreSQLCDCClient_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestPostgreSQLCDCClient_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_KafkaMultitopicConsumer_00() throws IOException {
        provideTest("./src/test/resources/json/TestKafkaMultitopicConsumer_00.json",
                "./src/test/resources/graph_test/sources/TestKafkaMultitopicConsumer_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestKafkaMultitopicConsumer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestKafkaMultitopicConsumer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_Salesforce_00() throws IOException {
        provideTest("./src/test/resources/json/TestSalesforce_00.json",
                "./src/test/resources/graph_test/sources/TestSalesforce_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestSalesforce_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestSalesforce_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_Salesforce_01() throws IOException {
        provideTest("./src/test/resources/json/TestSalesforce_01.json",
                "./src/test/resources/graph_test/sources/TestSalesforce_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestSalesforce_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestSalesforce_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_Directory_00() throws IOException {
        provideTest("./src/test/resources/json/TestDirectory_00.json",
                "./src/test/resources/graph_test/sources/TestDirectory_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestDirectory_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestDirectory_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_Directory_01() throws IOException {
        provideTest("./src/test/resources/json/TestDirectory_01.json",
                "./src/test/resources/graph_test/sources/TestDirectory_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestDirectory_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestDirectory_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HadoopFSStandalone_00() throws IOException {
        provideTest("./src/test/resources/json/TestHadoopFSStandalone_00.json",
                "./src/test/resources/graph_test/sources/TestHadoopFSStandalone_00_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestHadoopFSStandalone_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHadoopFSStandalone_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HadoopFSStandalone_01() throws IOException {
        provideTest("./src/test/resources/json/TestHadoopFSStandalone_01.json",
                "./src/test/resources/graph_test/sources/TestHadoopFSStandalone_01_output.txt",
                "./src/test/resources/graph_test/sources/expected_TestHadoopFSStandalone_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHadoopFSStandalone_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }




    // PROCESSORS

    @Test public void test_HiveMetadata_00() throws IOException {
        provideTest("./src/test/resources/json/TestHiveMetadata_00.json",
                "./src/test/resources/graph_test/processors/TestHiveMetadata_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestHiveMetadata_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHiveMetadata_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HiveMetadata_01() throws IOException {
        provideTest("./src/test/resources/json/TestHiveMetadata_01.json",
                "./src/test/resources/graph_test/processors/TestHiveMetadata_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestHiveMetadata_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHiveMetadata_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_SchemaGenerator_00() throws IOException {
        provideTest("./src/test/resources/json/TestSchemaGenerator_00.json",
                "./src/test/resources/graph_test/processors/TestSchemaGenerator_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestSchemaGenerator_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestSchemaGenerator_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldOrder_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldOrder_00.json",
                "./src/test/resources/graph_test/processors/TestFieldOrder_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldOrder_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldOrder_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldOrder_01() throws IOException {
        provideTest("./src/test/resources/json/TestFieldOrder_01.json",
                "./src/test/resources/graph_test/processors/TestFieldOrder_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldOrder_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldOrder_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldOrder_02() throws IOException {
        provideTest("./src/test/resources/json/TestFieldOrder_02.json",
                "./src/test/resources/graph_test/processors/TestFieldOrder_02_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldOrder_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldOrder_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldReplacer_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldReplacer_00.json",
                "./src/test/resources/graph_test/processors/TestFieldReplacer_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldReplacer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldReplacer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldSplitter_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldSplitter_00.json",
                "./src/test/resources/graph_test/processors/TestFieldSplitter_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldSplitter_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldSplitter_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldSplitter_01() throws IOException {
        provideTest("./src/test/resources/json/TestFieldSplitter_01.json",
                "./src/test/resources/graph_test/processors/TestFieldSplitter_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldSplitter_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldSplitter_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldSplitter_02() throws IOException {
        provideTest("./src/test/resources/json/TestFieldSplitter_02.json",
                "./src/test/resources/graph_test/processors/TestFieldSplitter_02_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldSplitter_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldSplitter_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldRemover_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldRemover_00.json",
                "./src/test/resources/graph_test/processors/TestFieldRemover_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldRemover_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldRemover_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldRemover_01() throws IOException {
        provideTest("./src/test/resources/json/TestFieldRemover_01.json",
                "./src/test/resources/graph_test/processors/TestFieldRemover_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldRemover_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldRemover_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldRemover_02() throws IOException {
        provideTest("./src/test/resources/json/TestFieldRemover_02.json",
                "./src/test/resources/graph_test/processors/TestFieldRemover_02_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldRemover_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldRemover_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldMasker_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldMasker_00.json",
                "./src/test/resources/graph_test/processors/TestFieldMasker_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldMasker_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldMasker_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldTypeConverter_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldTypeConverter_00.json",
                "./src/test/resources/graph_test/processors/TestFieldTypeConverter_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldTypeConverter_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldTypeConverter_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldHasher_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldHasher_00.json",
                "./src/test/resources/graph_test/processors/TestFieldHasher_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldHasher_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldHasher_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldHasher_01() throws IOException {
        provideTest("./src/test/resources/json/TestFieldHasher_01.json",
                "./src/test/resources/graph_test/processors/TestFieldHasher_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldHasher_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldHasher_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_StreamSelector_00() throws IOException {
        provideTest("./src/test/resources/json/TestStreamSelector_00.json",
                "./src/test/resources/graph_test/processors/TTestStreamSelector_00_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestStreamSelector_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestStreamSelector_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_StreamSelector_01() throws IOException {
        provideTest("./src/test/resources/json/TestStreamSelector_01.json",
                "./src/test/resources/graph_test/processors/TestStreamSelector_01_output.txt",
                "./src/test/resources/graph_test/processors/expected_TestStreamSelector_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestStreamSelector_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldRenamer_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldRenamer_00.json",
                "./src/test/resources/graph_test/processors/TestFieldRenamer_00.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldRenamer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldRenamer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_DataParser_00() throws IOException {
        provideTest("./src/test/resources/json/TestDataParser_00.json",
                "./src/test/resources/graph_test/processors/TestDataParser_00.txt",
                "./src/test/resources/graph_test/processors/expected_TestDataParser_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestDataParser_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldPivoter_00() throws IOException {
        provideTest("./src/test/resources/json/TestFieldPivoter_00.json",
                "./src/test/resources/graph_test/processors/TestFieldPivoter_00.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldPivoter_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldPivoter_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldPivoter_01() throws IOException {
        provideTest("./src/test/resources/json/TestFieldPivoter_01.json",
                "./src/test/resources/graph_test/processors/TestFieldPivoter_01.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldPivoter_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldPivoter_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldPivoter_02() throws IOException {
        provideTest("./src/test/resources/json/TestFieldPivoter_02.json",
                "./src/test/resources/graph_test/processors/TestFieldPivoter_02.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldPivoter_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldPivoter_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_FieldPivoter_03() throws IOException {
        provideTest("./src/test/resources/json/TestFieldPivoter_03.json",
                "./src/test/resources/graph_test/processors/TestFieldPivoter_03.txt",
                "./src/test/resources/graph_test/processors/expected_TestFieldPivoter_03_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestFieldPivoter_03_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_ExpressionEvaluator_00() throws IOException {
        provideTest("./src/test/resources/json/TestExpressionEvaluator_00.json",
                "./src/test/resources/graph_test/processors/TestExpressionEvaluator_00.txt",
                "./src/test/resources/graph_test/processors/expected_TestExpressionEvaluator_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestExpressionEvaluator_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_ExpressionEvaluator_01() throws IOException {
        provideTest("./src/test/resources/json/TestExpressionEvaluator_01.json",
                "./src/test/resources/graph_test/processors/TestExpressionEvaluator_01.txt",
                "./src/test/resources/graph_test/processors/expected_TestExpressionEvaluator_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestExpressionEvaluator_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_ExpressionEvaluator_02() throws IOException {
        provideTest("./src/test/resources/json/TestExpressionEvaluator_02.json",
                "./src/test/resources/graph_test/processors/TestExpressionEvaluator_02.txt",
                "./src/test/resources/graph_test/processors/expected_TestExpressionEvaluator_02_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestExpressionEvaluator_02_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_ExpressionEvaluator_03() throws IOException {
        provideTest("./src/test/resources/json/TestExpressionEvaluator_03.json",
                "./src/test/resources/graph_test/processors/TestExpressionEvaluator_03.txt",
                "./src/test/resources/graph_test/processors/expected_TestExpressionEvaluator_03_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestExpressionEvaluator_03_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    // DESTINATIONS

    @Test public void test_HiveMetastore_00() throws IOException {
        provideTest("./src/test/resources/json/TestHiveMetastore_00.json",
                "./src/test/resources/graph_test/destinations/TestHiveMetastore_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestHiveMetastore_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHiveMetastore_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_JDBCProducer_00() throws IOException {
        provideTest("./src/test/resources/json/TestJDBCProducer_00.json",
                "./src/test/resources/graph_test/destinations/TestJDBCProducer_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestJDBCProducer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestJDBCProducer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_JDBCProducer_01() throws IOException {
        provideTest("./src/test/resources/json/TestJDBCProducer_01.json",
                "./src/test/resources/graph_test/destinations/TestJDBCProducer_01_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestJDBCProducer_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestJDBCProducer_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_KafkaProducer_00() throws IOException {
        provideTest("./src/test/resources/json/TestKafkaProducer_00.json",
                "./src/test/resources/graph_test/destinations/TestKafkaProducer_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestKafkaProducer_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestKafkaProducer_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HTTPClient_00() throws IOException {
        provideTest("./src/test/resources/json/TestHTTPClient_00.json",
                "./src/test/resources/graph_test/destinations/TestHTTPClient_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestHTTPClient_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHTTPClient_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HTTPClient_01() throws IOException {
        provideTest("./src/test/resources/json/TestHTTPClient_01.json",
                "./src/test/resources/graph_test/destinations/TestHTTPClient_01_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestHTTPClient_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHTTPClient_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_LocalFS_00() throws IOException {
        provideTest("./src/test/resources/json/TestLocalFS_00.json",
                "./src/test/resources/graph_test/destinations/TestLocalFS_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestLocalFS_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestLocalFS_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_LocalFS_01() throws IOException {
        provideTest("./src/test/resources/json/TestLocalFS_01.json",
                "./src/test/resources/graph_test/destinations/TestLocalFS_01_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestLocalFS_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestLocalFS_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HadoopFS_00() throws IOException {
        provideTest("./src/test/resources/json/TestHadoopFS_00.json",
                "./src/test/resources/graph_test/destinations/TestHadoopFS_00_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestHadoopFS_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHadoopFS_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_HadoopFS_01() throws IOException {
        provideTest("./src/test/resources/json/TestHadoopFS_01.json",
                "./src/test/resources/graph_test/destinations/TestHadoopFS_01_output.txt",
                "./src/test/resources/graph_test/destinations/expected_TestHadoopFS_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestHadoopFS_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    // Executors

    @Test public void test_Shell_00() throws IOException {
        provideTest("./src/test/resources/json/TestShell_00.json",
                "./src/test/resources/graph_test/executors/TestShell_00_output.txt",
                "./src/test/resources/graph_test/executors/expected_TestShell_00_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestShell_00_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    @Test public void test_Shell_01() throws IOException {
        provideTest("./src/test/resources/json/TestShell_01.json",
                "./src/test/resources/graph_test/executors/TestShell_01_output.txt",
                "./src/test/resources/graph_test/executors/expected_TestShell_01_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_TestShell_01_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }

    //

    @Test public void test_IncorrectFieldPath() throws IOException {
        provideTest("./src/test/resources/json/IncorrectFieldPathTest.json",
                "./src/test/resources/graph_test/IncorrectFieldPathTest_output.txt",
                "./src/test/resources/graph_test/expected_IncorrectFieldPathTest_output.txt",
                "./src/test/resources/graph_test/graph_png/graph_IncorrectFieldPathTest_output.png",
                TestMode.COMPARE_EXPECTED_AND_OUTPUT);
    }
    // Query service example

    //    @Ignore
    //    @Test public void dataFlowQuery() {
    //        ConnectionImpl connectionOracle = new ConnectionImpl(ConnectionType.ORACLE.getId(),
    //                "jdbc:oracle:thin:@localhost:1521/ora1", null, null, "Username", "ora1");
    //        Graph graph = new GraphImpl(Resource.NO_RESOURCE);
    //        DataflowQueryService queryService = task.getQueryService();
    //        Node n1 = queryService.addObjectNode(connectionOracle.getDatabaseName(), connectionOracle.getSchemaName(), "Person", connectionOracle, graph);
    ////        DataflowQueryResult queryResult = queryService.getDataFlow(n1, "script_name", "SELECT SURNAME FROM Person", connectionOracle);
    //
    //        ConnectionImpl connectionPostgre = new ConnectionImpl(ConnectionType.POSTGRESQL.getId(),
    //                "jdbc:postgresql://localhost:5432/database", null, null, "Username", "datasource");
    //        Node n2 = queryService.addObjectNode(connectionPostgre.getDatabaseName(), connectionPostgre.getSchemaName(), "Dog", connectionPostgre, graph);
    //
    //
    //        ConnectionImpl connectionHive = new ConnectionImpl(ConnectionType.HIVE.getId(),
    //                "jdbc:hive://datasource:10000", null, "Username", null, null);
    //        Node n3 = queryService.addObjectNode(connectionHive.getDatabaseName(), connectionHive.getSchemaName(), "Person", connectionHive, graph);
    //        List<Node> nodes = queryService.addColumnNodes(n3, connectionHive, graph);queryService.addColumnNodes(n3, connectionHive, graph);
//    }

}
