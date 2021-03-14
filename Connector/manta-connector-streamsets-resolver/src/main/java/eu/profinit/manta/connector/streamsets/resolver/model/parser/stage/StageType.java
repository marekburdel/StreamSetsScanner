package eu.profinit.manta.connector.streamsets.resolver.model.parser.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageType;
import eu.profinit.manta.connector.streamsets.model.parser.IStageProcessor;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.destination.*;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.executor.ShellProcessor;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.processor.*;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.source.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mburdel
 */
public final class StageType implements IStageType {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageType.class);

    private StageType() {
    }

    public static IStageProcessor getStageProcessor(String stageNameString) {

        StageName stageName = StageName.getStageName(stageNameString);

        switch (stageName) {
        // Source Processors
        case JDBC_QUERY_CONSUMER:
            return new JDBCQueryConsumerProcessor();
        case DIRECTORY:
            return new DirectoryProcessor();
        case SALESFORCE:
            return new SalesforceProcessor();
        case HADOOP_FS_STANDALONE:
            return new HadoopFSStandaloneProcessor();
        case KAFKA_MULTITOPIC_CONSUMER:
            return new KafkaMultitopicConsumerProcessor();
        case ORACLE_CDC_CLIENT:
            return new OracleCDCClientProcessor();
        case POSTGRESQL_CDC_CLIENT:
            return new PostgreSQLCDCClientProcessor();

        // Processor Processors
        case EXPRESSION_EVALUATOR:
            return new ExpressionEvaluatorProcessor();
        case FIELD_SPLITTER:
            return new FieldSplitterProcessor();
        case FIELD_REPLACER:
            return new FieldReplacerProcessor();
        case FIELD_RENAMER:
            return new FieldRenamerProcessor();
        case SCHEMA_GENERATOR:
            return new SchemaGeneratorProcessor();
        case FIELD_REMOVER:
            return new FieldRemoverProcessor();
        case STREAM_SELECTOR:
            return new StreamSelectorProcessor();
        case FIELD_MASKER:
            return new FieldMaskerProcessor();
        case HIVE_METADATA:
            return new HiveMetadataProcessor();
        case FIELD_ORDER:
            return new FieldOrderProcessor();
        case FIELD_HASHER:
            return new FieldHasherProcessor();
        case FIELD_TYPE_CONVERTER:
            return new FieldTypeConverterProcessor();
        case DATA_PARSER:
            return new DataParserProcessor();
        case FIELD_PIVOTER:
            return new FieldPivoterProcessor();

        // Destination Processors
        case TRASH:
            return new TrashProcessor();
        case LOCAL_FS:
            return new LocalFSProcessor();
        case KAFKA_PRODUCER:
            return new KafkaProducerProcessor();
        case HTTP_CLIENT:
            return new HTTPClientProcessor();
        case HIVE_METASTORE:
            return new HiveMetastoreProcessor();
        case HADOOP_FS:
            return new HadoopFSProcessor();
        case JDBC_PRODUCER:
            return new JDBCProducerProcessor();

        // Executor Processors
        case SHELL:
            return new ShellProcessor();
        // Other Downloadable Processors

        default:
            LOGGER.warn("Unsupported stage: " + stageName + ". Default stage loaded.");
            return new UnknownStageProcessor();
        }
    }
}
