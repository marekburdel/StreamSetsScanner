package eu.profinit.manta.connector.streamsets.model.model.stage;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mburdel
 */
public interface IStageType {

    /**
     * Specifies the stage name type. Each enum value contains stageName string that represents
     * identification for specific StreamSets Stage.
     */
    enum StageName {

        DEFAULT_STAGE,

        // Sources
        JDBC_QUERY_CONSUMER("com_streamsets_pipeline_stage_origin_jdbc_JdbcDSource"),
        DIRECTORY("com_streamsets_pipeline_stage_origin_spooldir_SpoolDirDSource"),
        SALESFORCE("com_streamsets_pipeline_stage_origin_salesforce_ForceDSource"),
        HADOOP_FS_STANDALONE("com_streamsets_pipeline_stage_origin_hdfs_HdfsDSource"),
        KAFKA_MULTITOPIC_CONSUMER("com_streamsets_pipeline_stage_origin_multikafka_MultiKafkaDSource"),
        ORACLE_CDC_CLIENT("com_streamsets_pipeline_stage_origin_jdbc_cdc_oracle_OracleCDCDSource"),
        POSTGRESQL_CDC_CLIENT("com_streamsets_pipeline_stage_origin_jdbc_cdc_postgres_PostgresCDCDSource"),

        // Development Sources,
        DEV_DATA_GENERATOR("com_streamsets_pipeline_stage_devtest_RandomDataGeneratorSource"),

        // Processors,
        EXPRESSION_EVALUATOR("com_streamsets_pipeline_stage_processor_expression_ExpressionDProcessor"),
        FIELD_SPLITTER("com_streamsets_pipeline_stage_processor_splitter_SplitterDProcessor"),
        FIELD_REPLACER("com_streamsets_pipeline_stage_processor_fieldreplacer_FieldReplacerDProcessor"),
        FIELD_RENAMER("com_streamsets_pipeline_stage_processor_fieldrenamer_FieldRenamerDProcessor"),
        SCHEMA_GENERATOR("com_streamsets_pipeline_stage_processor_schemagen_SchemaGeneratorDProcessor"),
        FIELD_REMOVER("com_streamsets_pipeline_stage_processor_fieldfilter_FieldFilterDProcessor"),
        STREAM_SELECTOR("com_streamsets_pipeline_stage_processor_selector_SelectorDProcessor"),
        FIELD_MASKER("com_streamsets_pipeline_stage_processor_fieldmask_FieldMaskDProcessor"),
        HIVE_METADATA("com_streamsets_pipeline_stage_processor_hive_HiveMetadataDProcessor"),
        FIELD_ORDER("com_streamsets_pipeline_stage_processor_fieldorder_FieldOrderDProcessor"),
        FIELD_HASHER("com_streamsets_pipeline_stage_processor_fieldhasher_FieldHasherDProcessor"),
        FIELD_TYPE_CONVERTER("com_streamsets_pipeline_stage_processor_fieldtypeconverter_FieldTypeConverterDProcessor"), DATA_PARSER(
                "com_streamsets_pipeline_stage_processor_parser_DataParserDProcessor"), FIELD_PIVOTER(
                "com_streamsets_pipeline_stage_processor_listpivot_ListPivotDProcessor"),

        // Destinations,
        TRASH("com_streamsets_pipeline_stage_destination_devnull_NullDTarget"), LOCAL_FS(
                "com_streamsets_pipeline_stage_destination_localfilesystem_LocalFileSystemDTarget"), KAFKA_PRODUCER(
                "com_streamsets_pipeline_stage_destination_kafka_KafkaDTarget"), HTTP_CLIENT(
                "com_streamsets_pipeline_stage_destination_http_HttpClientDTarget"), HIVE_METASTORE(
                "com_streamsets_pipeline_stage_destination_hive_HiveMetastoreDTarget"), HADOOP_FS(
                "com_streamsets_pipeline_stage_destination_hdfs_HdfsDTarget"), JDBC_PRODUCER(
                "com_streamsets_pipeline_stage_destination_jdbc_JdbcDTarget"),

        // Executors
        SHELL("com_streamsets_pipeline_stage_executor_shell_ShellDExecutor")

        // Downloadable

        ;

        private final String[] values;

        StageName(String... values) {
            this.values = values;
        }

        private static Map<String, StageName> mapping;

        public static StageName getStageName(String stageTypeName) throws IllegalArgumentException {
            if (mapping == null) {
                init();
            }
            StageName type = mapping.get(stageTypeName);
            if (type == null) {
                type = DEFAULT_STAGE;
            }
            return type;
        }

        private static void init() {
            mapping = new HashMap<>();
            for (StageName type : StageName.values()) {
                for (String value : type.values) {
                    Validate.isTrue(!mapping.containsKey(value));
                    mapping.put(value, type);
                }
            }
        }
    }
}
