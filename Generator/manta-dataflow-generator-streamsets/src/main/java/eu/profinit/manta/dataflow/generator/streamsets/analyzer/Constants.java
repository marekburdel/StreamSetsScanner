package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

/**
 * Class contains constants used during data flow generation.
 * @author mburdel
 */
public final class Constants {

    // Attributes
    public static final String ATTRIBUTE_PIPELINE_TITLE = "PIPELINE_TITLE";
    public static final String ATTRIBUTE_SOQL_QUERY = "SOQL_QUERY";
    public static final String ATTRIBUTE_SQL_QUERY = "SQL_QUERY";
    public static final String ATTRIBUTE_KAFKA_CONSUMER_GROUP = "KAFKA_CONSUMER_GROUP";
    public static final String ATTRIBUTE_HASH_TYPE = "HASH_TYPE";
    public static final String ATTRIBUTE_MASK_TYPE = "MASK_TYPE";
    public static final String ATTRIBUTE_CUSTOM_MASK = "CUSTOM_MASK";
    public static final String ATTRIBUTE_REGULAR_EXPRESSION = "REGULAR_EXPRESSION";
    public static final String ATTRIBUTE_GROUPS_TO_SHOW = "GROUPS_TO_SHOW";
    public static final String ATTRIBUTE_NEW_VALUE = "NEW_VALUE";
    public static final String ATTRIBUTE_SET_TO_NULL = "SET_TO_NULL";
    public static final String ATTRIBUTE_FIELD_TO_SPLIT = "FIELD_TO_SPLIT";
    public static final String ATTRIBUTE_SEPARATOR = "SEPARATOR";
    public static final String ATTRIBUTE_CONVERT_TO_TYPE = "CONVERT_TO_TYPE";
    public static final String ATTRIBUTE_CONVERSION = "CONVERSION";
    public static final String ATTRIBUTE_SCHEMA_NAME = "SCHEMA_NAME";
    public static final String ATTRIBUTE_HEADER_ATTRIBUTE = "HEADER_ATTRIBUTE";
    public static final String ATTRIBUTE_CONDITION = "CONDITION";
    public static final String ATTRIBUTE_SCRIPT = "SCRIPT";
    public static final String ATTRIBUTE_JDBC_URL = "JDBC_URL";
    public static final String ATTRIBUTE_DATABASE_NAME = "DATABASE_NAME";
    public static final String ATTRIBUTE_TABLE_NAME = "TABLE_NAME";

    public static final String SLASH = "/";
    public static final String SINGLE_QUOTE = "'";

    public static final String QUERY_RESULT = ".QUERY_RESULT";

    public static final String DEFAULT_DATABASE_NAME = "DEFAULT_DATABASE_NAME";
    public static final String DEFAULT_TABLE_NAME = "DEFAULT_TABLE_NAME";

    public static final String INPUT = "INPUT";
    public static final String OUTPUT = "OUTPUT ";
    public static final String EXPRESSION = "EXPRESSION ";
    public static final String HTTP = "HTTP ";

    public static final String DEFAULT_URI = "Default File System URI";
    public static final String DEFAULT_DIR = "Unknown Directory";

    // Resources
    public static final String KAFKA_MULTITOPIC_RESOURCE_NAME = "Kafka";
    public static final String KAFKA_MULTITOPIC_RESOURCE_TYPE = "Kafka";
    public static final String KAFKA_MULTITOPIC_RESOURCE_DESC = "Kafka Multitopic - Consumer Group";

    public static final String HTTP_RESOURCE_NAME = "HTTP";
    public static final String HTTP_RESOURCE_TYPE = "HTTP";
    public static final String HTTP_RESOURCE_DESC = "HTTP Client";

    public static final String KAFKA_PRODUCER_RESOURCE_NAME = "Kafka";
    public static final String KAFKA_PRODUCER_RESOURCE_TYPE = "Kafka";
    public static final String KAFKA_PRODUCER_RESOURCE_DESC = "Kafka Producer";

    public static final String SALESFORCE_RESOURCE_NAME = "Salesforce";
    public static final String SALESFORCE_RESOURCE_TYPE = "Salesforce";
    public static final String SALESFORCE_RESOURCE_DESC = "Salesforce";
    public static final String SALESFORCE_APPLICATION = "Salesforce Application";

    private Constants() {
    }
}
