package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.connector.common.StringFileReader;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.common.dictionary.AbstractDataDictionary;
import eu.profinit.manta.connector.common.dictionary.dao.jdbc.H2DictionaryDaoFactory;
import eu.profinit.manta.connector.hive.HiveReader;
import eu.profinit.manta.connector.hive.dictionary.HiveDataDictionary;
import eu.profinit.manta.connector.hive.dictionary.HiveResolverEntitiesFactory;
import eu.profinit.manta.connector.hive.dictionary.dialect.HiveDialect;
import eu.profinit.manta.connector.oracle.OracleReader;
import eu.profinit.manta.connector.oracle.dictionary.OracleDataDictionary;
import eu.profinit.manta.connector.oracle.dictionary.dialect.OracleDialect;
import eu.profinit.manta.connector.oracle.model.dictionary.OracleResolverEntitiesFactory;
import eu.profinit.manta.connector.postgresql.PostgresqlReader;
import eu.profinit.manta.connector.postgresql.dictionary.PostgresqlDataDictionary;
import eu.profinit.manta.connector.postgresql.dictionary.dialect.PostgresqlDialect;
import eu.profinit.manta.connector.postgresql.model.dictionary.PostgresqlResolverEntitiesFactory;
import eu.profinit.manta.platform.automation.InputReader;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author mburdel
 */
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration("classpath:spring/streamsets-test-flow.xml")
public class DatabaseTestBase extends TestBase {

    /**
     * Input directory with DDL scripts.
     */
    private static final File SCRIPT_FOLDER = new File("src/test/resources/ddl");

    /**
     * Output directory to store persisted dictionaries.
     */
    private static final File DICTIONARY_FOLDER = new File("target/test-classes/dictionary");

    private static eu.profinit.manta.plsql.parser.service.ParserServiceImpl oracleParserService;
    private static eu.profinit.manta.connector.postgresql.resolver.service.ParserServiceImpl postgresqlParserService;
    private static eu.profinit.manta.connector.hive.resolver.ParserServiceImpl hiveParserService;

    private static ClassPathXmlApplicationContext springContext;

    @Autowired public StreamSetsDataflowTask task;

    static void loadContext() {
        springContext = new ClassPathXmlApplicationContext("spring/streamsets-test-flow.xml");

        oracleParserService = springContext.getBean(eu.profinit.manta.plsql.parser.service.ParserServiceImpl.class);
        postgresqlParserService = springContext
                .getBean(eu.profinit.manta.connector.postgresql.resolver.service.ParserServiceImpl.class);
        hiveParserService = springContext.getBean(eu.profinit.manta.connector.hive.resolver.ParserServiceImpl.class);
    }

    @AfterClass
    public static void tearDownClass() {
        if (springContext != null) {
            springContext.close();
            springContext = null;
        }
    }

    /**
     * Creates and persists a dictionary in the given dialect with the given dictionary ID based on the parsed input.
     *
     * @param dialect      dialect
     * @param dictionaryId dictionary ID of the dictionary to be created
     * @param scriptFolder subfolder containing create scripts
     */
    static void createDictionary(ConnectionType dialect, String dictionaryId, String scriptFolder) {

        AbstractDataDictionary<?> dictionary = null;
        try (StringFileReader stringReader = new StringFileReader()) {
            stringReader.setInputFile(FileUtils.getFile(SCRIPT_FOLDER, dialect.getId(), scriptFolder));
            stringReader.setEncoding("utf8");

            File contextDirectory = new File(DICTIONARY_FOLDER, dialect.getId());

            InputReader<?> reader = null;
            switch (dialect) {
            case ORACLE:
                OracleReader oraReader = new OracleReader();
                oraReader.setResetBeforeRead(false);
                oraReader.setIsDefaultSchemaFromFolder(true);
                oraReader.setParserService(oracleParserService);
                H2DictionaryDaoFactory<OracleResolverEntitiesFactory> oraDaoFactory = new H2DictionaryDaoFactory<>();
                oraDaoFactory.setCreateMissing(true);
                OracleDataDictionary oraDict = new OracleDataDictionary(
                        oraDaoFactory.getDataDictionaryDao(contextDirectory, dictionaryId), new OracleDialect(), true,
                        dictionaryId);
                oraReader.setEntitiesFactory(oraDict);
                oraReader.setInputReader(stringReader);
                reader = oraReader;
                dictionary = oraDict;
                break;
            case POSTGRESQL:
                PostgresqlReader postgresqlReader = new PostgresqlReader();
                postgresqlReader.setResetBeforeRead(false);
                postgresqlReader.setIsDefaultSchemaFromFolder(true);
                postgresqlReader.setParserService(postgresqlParserService);
                H2DictionaryDaoFactory<PostgresqlResolverEntitiesFactory> postgresqlDaoFactory = new H2DictionaryDaoFactory<>();
                postgresqlDaoFactory.setCreateMissing(true);
                PostgresqlDataDictionary postgresqlDict = new PostgresqlDataDictionary(
                        postgresqlDaoFactory.getDataDictionaryDao(contextDirectory, dictionaryId), new PostgresqlDialect(), true,
                        "PostgresqlServer:PostgresqlDatabase");
                postgresqlReader.setEntitiesFactory(postgresqlDict);
                postgresqlReader.setInputReader(stringReader);
                reader = postgresqlReader;
                dictionary = postgresqlDict;
                break;
            case HIVE:
                HiveReader hiveReader = new HiveReader();
                hiveReader.setResetBeforeRead(false);
                hiveReader.setIsDefaultSchemaFromFolder(true);
                hiveReader.setParserService(hiveParserService);
                H2DictionaryDaoFactory<HiveResolverEntitiesFactory> hiveDaoFactory = new H2DictionaryDaoFactory<>();
                hiveDaoFactory.setCreateMissing(true);
                HiveDataDictionary hiveDict = new HiveDataDictionary(
                        hiveDaoFactory.getDataDictionaryDao(contextDirectory, dictionaryId), new HiveDialect(), true,
                        "HiveServer:HiveDatabase");
                hiveReader.setEntitiesFactory(hiveDict);
                hiveReader.setInputReader(stringReader);
                reader = hiveReader;
                dictionary = hiveDict;
                break;
            default:
                throw new IllegalArgumentException("reader provider not found for dialect " + dialect);
            }

            if (reader != null) {
                try {
                    while (reader.canRead()) {
                        try {
                            reader.read();
                            dictionary.persist();
                        } catch (IOException e) {
                            throw new RuntimeException("Error reading script " + reader.getInputName(), e);
                        }
                    }
                } finally {
                    dictionary.close();
                }
            }
        }

    }
}
