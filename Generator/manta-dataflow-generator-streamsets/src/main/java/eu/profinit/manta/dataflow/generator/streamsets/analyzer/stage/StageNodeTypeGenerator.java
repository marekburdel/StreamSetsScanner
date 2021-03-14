package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageType;
import eu.profinit.manta.dataflow.model.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to generate node's type for stage
 * @author mburdel
 */
public class StageNodeTypeGenerator implements IStageType {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageNodeTypeGenerator.class);

    private StageNodeTypeGenerator() {
    }

    public static NodeType getNodeType(String stageNameString) {
        IStageType.StageName stageName;
        try {
            stageName = IStageType.StageName.getStageName(stageNameString);
        } catch (IllegalArgumentException e) {
            return NodeType.STREAMSETS_STAGE;
        }

        switch (stageName) {
        // Source Processors
        case JDBC_QUERY_CONSUMER:
            return NodeType.STREAMSETS_JDBC_QUERY_CONSUMER;
        case DIRECTORY:
            return NodeType.STREAMSETS_DIRECTORY;
        case SALESFORCE:
            return NodeType.STREAMSETS_SALESFORCE;
        case HADOOP_FS_STANDALONE:
            return NodeType.STREAMSETS_HADOOP_FS_STANDALONE;
        case KAFKA_MULTITOPIC_CONSUMER:
            return NodeType.STREAMSETS_KAFKA_MULTITOPIC_CONSUMER;
        case ORACLE_CDC_CLIENT:
            return NodeType.STREAMSETS_ORACLE_CDC_CLIENT;
        case POSTGRESQL_CDC_CLIENT:
            return NodeType.STREAMSETS_POSTGRESQL_CDC_CLIENT;

        // Processor Processors
        case EXPRESSION_EVALUATOR:
            return NodeType.STREAMSETS_EXPRESSION_EVALUATOR;
        case FIELD_SPLITTER:
            return NodeType.STREAMSETS_FIELD_SPLITTER;
        case FIELD_REPLACER:
            return NodeType.STREAMSETS_FIELD_REPLACER;
        case FIELD_RENAMER:
            return NodeType.STREAMSETS_FIELD_RENAMER;
        case SCHEMA_GENERATOR:
            return NodeType.STREAMSETS_SCHEMA_GENERATOR;
        case FIELD_REMOVER:
            return NodeType.STREAMSETS_FIELD_REMOVER;
        case STREAM_SELECTOR:
            return NodeType.STREAMSETS_STREAM_SELECTOR;
        case FIELD_MASKER:
            return NodeType.STREAMSETS_FIELD_MASKER;
        case HIVE_METADATA:
            return NodeType.STREAMSETS_HIVE_METADATA;
        case FIELD_ORDER:
            return NodeType.STREAMSETS_FIELD_ORDER;
        case FIELD_HASHER:
            return NodeType.STREAMSETS_FIELD_HASHER;
        case FIELD_TYPE_CONVERTER:
            return NodeType.STREAMSETS_FIELD_TYPE_CONVERTER;
        case FIELD_PIVOTER:
            return NodeType.STREAMSETS_FIELD_PIVOTER;
        case DATA_PARSER:
            return NodeType.STREAMSETS_DATA_PARSER;

        // Destination Processors
        case TRASH:
            return NodeType.STREAMSETS_TRASH;
        case LOCAL_FS:
            return NodeType.STREAMSETS_LOCAL_FS;
        case KAFKA_PRODUCER:
            return NodeType.STREAMSETS_KAFKA_PRODUCER;
        case HTTP_CLIENT:
            return NodeType.STREAMSETS_HTTP_CLIENT;
        case HIVE_METASTORE:
            return NodeType.STREAMSETS_HIVE_METASTORE;
        case HADOOP_FS:
            return NodeType.STREAMSETS_HADOOP_FS;
        case JDBC_PRODUCER:
            return NodeType.STREAMSETS_JDBC_PRODUCER;

        // Executor Processors
        case SHELL:
            return NodeType.STREAMSETS_SHELL;
        // Other Downloadable Processors

        default:
            return NodeType.STREAMSETS_STAGE;
        }
    }
}

