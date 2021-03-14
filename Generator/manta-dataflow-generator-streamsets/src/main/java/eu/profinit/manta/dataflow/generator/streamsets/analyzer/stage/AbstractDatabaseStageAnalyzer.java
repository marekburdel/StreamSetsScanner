package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.common.connections.impl.ConnectionImpl;
import eu.profinit.manta.connector.common.connections.query.Column;
import eu.profinit.manta.connector.common.connections.query.Resultset;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.ISchemaTableComponent;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryResult;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.AttributeNames;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;
import eu.profinit.manta.dataflow.model.Resource;
import eu.profinit.manta.dataflow.model.impl.ResourceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for database's operations
 * @author mburdel
 */
public abstract class AbstractDatabaseStageAnalyzer<T extends IStage> extends AbstractStageAnalyzer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatabaseStageAnalyzer.class);

    /**
     * Method adds children to node(source) according to analyzed fields during Fields' analysis.
     * Then method adds direct flow between field's node and created node.
     *
     * @param stage that is directly connected with source
     * @param ctx context
     * @param tableNode table/service node
     */
    protected void mergeAndConnectAllAnalyzedFieldsAsSource(T stage, PipelineContext ctx, Node tableNode) {
        for (Node fieldNode : ctx.getGraphHelper().getNode(stage).getChildren()) {
            String columnName = defineColumnNameByFieldPath(fieldNode.getName());
            Node columnNode = ctx.getGraphHelper()
                    .addNode(columnName, NodeType.COLUMN, tableNode, tableNode.getResource());
            ctx.getGraphHelper().addDirectFlow(columnNode, fieldNode);
        }
    }

    /**
     * Method adds children to node(destination) according to analyzed fields during Fields' analysis.
     * Then method adds direct flow between field's node and created node.
     *
     * @param stage that is directly connected with source
     * @param ctx context
     * @param tableNode table/service node
     */
    protected void mergeAndConnectAllAnalyzedFieldsAsDestination(T stage, PipelineContext ctx, Node tableNode) {
        for (Node fieldNode : ctx.getGraphHelper().getNode(stage).getChildren()) {
            String columnName = defineColumnNameByFieldPath(fieldNode.getName());
            Node columnNode = ctx.getGraphHelper()
                    .addNode(columnName, NodeType.COLUMN, tableNode, tableNode.getResource());
            ctx.getGraphHelper().addDirectFlow(fieldNode, columnNode);
        }
    }

    /**
     * Method provides manual analysis of database connector.
     * In this case there is not sql statement in analyzing database connector
     * or analyzing of sql statement was not successful, then this method is called.
     *
     * @param stage     stage
     * @param ctx       context
     * @param tableName table name
     * @return node created table node
     */
    protected Node createTableNodeManually(T stage, PipelineContext ctx, String tableName) {

        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();
        Connection connection = setting.getConnection();
        DataflowQueryService queryService = getQueryService();

        // try get defaults from query service
        Resource resource = queryService.getDatabaseResource(connection);
        String serverName = queryService.getDefaultServer(connection);
        String databaseName = queryService.getDefaultDatabase(connection);
        String schemaName = queryService.getDefaultSchema(connection);

        // default resource
        if (resource == null) {
            resource = new ResourceImpl(connection.getType(), connection.getType(), StringUtils.EMPTY);
        }

        // default server
        if (StringUtils.isBlank(serverName)) {
            serverName = connection.getServerName();
        }

        // default database
        if (StringUtils.isBlank(databaseName)) {
            databaseName = StringUtils.isNotBlank(connection.getDatabaseName()) ?
                           connection.getDatabaseName() :
                           connection.getConnectionString();
            databaseName = StringUtils.isBlank(databaseName) ? Constants.DEFAULT_DATABASE_NAME : databaseName;
        }

        // default scheme
        if (StringUtils.isBlank(schemaName) && (StringUtils.isNotBlank(connection.getSchemaName()) || StringUtils
                .isNotBlank(connection.getUserName()))) {
            schemaName = StringUtils.isNotBlank(connection.getSchemaName()) ?
                         connection.getSchemaName() :
                         connection.getUserName();
        }

        tableName = tableName == null ? Constants.DEFAULT_TABLE_NAME : tableName;
        LOGGER.warn(
                "Cannot determine name of the table. Default name will be used. Table name: {}, " + "Database name: {}",
                tableName, databaseName);

        Node tableNode;
        tableNode = getNodeCreator()
                .createTableNode(gh.graph(), serverName, databaseName, schemaName, tableName, resource);
        return tableNode;
    }

    /**
     * Method creates fields according to DataflowQueryResult's columns.
     * @param stage stage
     * @param ctx context
     * @param queryResult DataflowQueryResult from connection, that was provided by stage's configuration.
     */
    protected void analyzeFieldsFromQueryResult(T stage, PipelineContext ctx, DataflowQueryResult queryResult) {
        List<Resultset> resultsets = queryResult.getOutputMetadata();
        for (Resultset resultset : resultsets) {
            for (Column column : resultset.getColumns()) {
                addFieldAsColumn(ctx.getSetting(stage).getInputRootField(), column.getName());
            }
        }
    }

    /**
     * Method creates field corresponding to column name. Firstly method tries to create field with same name.
     * If column name contains special characters, new field will be created with single quotes.
     * @param rootField root Field
     * @param columnName column name
     * @return created field
     */
    protected Field addFieldAsColumn(Field rootField, String columnName) {
        String analyzedFieldPath;
        Field addedField;

        analyzedFieldPath = Constants.SLASH + columnName;
        addedField = rootField.add(analyzedFieldPath, EFieldType.IO);
        // if column name contains special chars or white space
        if (addedField == null) {

            analyzedFieldPath = Constants.SLASH + Constants.SINGLE_QUOTE + columnName.replaceAll("'", "\\\\'")
                    + Constants.SINGLE_QUOTE;
            addedField = rootField.add(analyzedFieldPath, EFieldType.IO);
            if (addedField == null) {
                LOGGER.warn("Cannot add field with name: {}", columnName);
            }
        }
        return addedField;
    }

    /**
     * Method sorts stage's fields in DataflowQueryResult's order.
     * @param stage stage
     * @param ctx context
     * @param queryResult DataflowQueryResult from connection, that was provided by stage's configuration.
     * @return list of sorted fields' nodes in same order as columns in DataflowQueryResult
     */
    private List<Node> sortFieldsNodesByQueryResult(T stage, PipelineContext ctx, DataflowQueryResult queryResult) {
        List<Node> nodes = new ArrayList<>();

        Field rootField = ctx.getSetting(stage).getInputRootField();

        for (Resultset resultset : queryResult.getOutputMetadata()) {
            for (Column column : resultset.getColumns()) {
                Field field = findFieldAsColumn(rootField, column);
                Node fieldNode = ctx.getGraphHelper().getNode(field);
                nodes.add(fieldNode);
            }
        }

        return nodes;
    }

    /**
     * Method ignores single quotes while is trying to find field with corresponding column's name.
     * @param rootField root field
     * @param column column
     * @return Field that has name as column
     */
    private Field findFieldAsColumn(Field rootField, Column column) {
        String analyzedFieldPath;
        Field foundField;

        analyzedFieldPath = Constants.SLASH + column.getName();
        foundField = rootField.findFieldByPath(analyzedFieldPath);

        if (foundField == null) {
            analyzedFieldPath = Constants.SLASH + Constants.SINGLE_QUOTE + column.getName() + Constants.SINGLE_QUOTE;
            foundField = rootField.findFieldByPath(analyzedFieldPath);

            if (foundField == null) {
                analyzedFieldPath = Constants.SLASH + Constants.SINGLE_QUOTE + column.getName().replaceAll("'", "\\\\'")
                        + Constants.SINGLE_QUOTE;
                foundField = rootField.findFieldByPath(analyzedFieldPath);
            }
        }
        return foundField;
    }

    /**
     * If Query Result contains some data, method creates flows between fields and columns in Table Node
     * that is created by Query Service.
     * Else method creates Table node by itself, adds all known columns to Table Node
     * and then creates flows between columns and fiels.
     * @param stage stage
     * @param ctx context
     */
    protected void createFlowsFromQueryResultAsSource(T stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        DataflowQueryResult queryResult = setting.getQueryResult();

        if (!queryResult.getOutputMetadata().isEmpty()) {
            List<Node> sortedFieldsNodes = sortFieldsNodesByQueryResult(stage, ctx, queryResult);

            queryResult.connectAllResultsetsTo(sortedFieldsNodes, DataflowQueryResult.MatchStrategy.MATCH_BY_POSITION);
            Node mergedNode = null;
            try {
                mergedNode = queryResult.mergeTo(ctx.getGraphHelper().graph());
            } catch (Exception e) {
                LOGGER.error("Cannot merge database with graph.", e);
            }
            if (mergedNode != null && sortedFieldsNodes.isEmpty()) {
                mergeAndConnectAllAnalyzedFieldsAsSource(stage, ctx, mergedNode);
            }
        } else {
            LOGGER.error("Unsuccessful attempt to analyze SQL query in stage: {}", stage.getStageUIInfo().getLabel());
            createFlowsFromQueryResultAsSourceManually(stage, ctx);
        }
    }

    private void createFlowsFromQueryResultAsSourceManually(T stage, PipelineContext ctx) {
        Node tableNode = createTableNodeManually(stage, ctx, null);
        tableNode
                .addAttribute(AttributeNames.NODE_OBJECT_SOURCE_TYPE, AttributeNames.NODE_OBJECT_SOURCE_TYPE_DEDUCTION);
        mergeAndConnectAllAnalyzedFieldsAsSource(stage, ctx, tableNode);
    }

    /**
     * During Fields's Analysis phase this method uses Query Service to get all known columns
     * from jdbc url, username, schema(s) and table(s).
     *
     * @param stage stage
     * @param ctx context
     * @param jdbcUrl jdbc url
     * @param username username
     * @param components list of components that contains schemas and tables for jdbcUrl and username
     * @param connectionType connection type
     */
    protected void analyzeFieldsFromSchemaTableComponents(T stage, PipelineContext ctx, String jdbcUrl, String username,
            List<? extends ISchemaTableComponent> components, ConnectionType connectionType) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        Connection connection;
        String schemaName;
        String tableName;

        for (ISchemaTableComponent component : components) {
            schemaName = replaceRuntimeValueWithoutRemovingEL(component.getSchemaName(), ctx);
            tableName = replaceRuntimeValueWithoutRemovingEL(component.getTableName(), ctx);

            connection = createConnection(connectionType, jdbcUrl, jdbcUrl, null, schemaName, username, stage, ctx);
            DataflowQueryService queryService = getQueryService();
            Node tableNode = queryService
                    .addObjectNode(null, schemaName, tableName, connection, ctx.getGraphHelper().graph());

            List<Node> columnNodes = queryService.addColumnNodes(tableNode, connection, ctx.getGraphHelper().graph());

            if (columnNodes != null) {
                for (Node columnNode : columnNodes) {
                    Field analyzedField = addFieldAsColumn(setting.getInputRootField(), columnNode.getName());
                    setting.addNodeAndFieldPair(columnNode, analyzedField);
                }
            } else {
                LOGGER.error("Cannot found schema {} with table {}.", schemaName, tableName);
            }
        }
    }

    /**
     * Method creates flows between analyzed columns and fields.
     * @param stage stage
     * @param ctx context
     * @param jdbcUrl jdbc url
     * @param username username
     * @param components list of components that contains schemas and tables for jdbcUrl and username
     */
    protected void createFlowsAsSourceFromSchemaTableComponents(T stage, PipelineContext ctx, String jdbcUrl,
            String username, List<? extends ISchemaTableComponent> components) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        if (setting.getNodeAndFieldPairs().isEmpty()) {
            String schemaName = null;
            String tableName = null;
            if (components.size() == 1) {
                ISchemaTableComponent component = components.get(0);
                schemaName = replaceRuntimeValueWithoutRemovingEL(component.getSchemaName(), ctx);
                tableName = replaceRuntimeValueWithoutRemovingEL(component.getTableName(), ctx);
            }
            Connection connection = createConnection(ConnectionType.JDBC, jdbcUrl, jdbcUrl, null, schemaName, username,
                    stage, ctx);
            setting.setConnection(connection);
            Node tableNode = createTableNodeManually(stage, ctx, tableName);
            tableNode.addAttribute(AttributeNames.NODE_OBJECT_SOURCE_TYPE,
                    AttributeNames.NODE_OBJECT_SOURCE_TYPE_DEDUCTION);
            mergeAndConnectAllAnalyzedFieldsAsSource(stage, ctx, tableNode);
        }
        for (Pair<Node, Field> pair : setting.getNodeAndFieldPairs()) {
            Node columnNode = pair.getKey();
            Node fieldNode = ctx.getGraphHelper().getNode(pair.getValue());

            ctx.getGraphHelper().addDirectFlow(columnNode, fieldNode);
        }
    }

    /**
     * Method creates connection with resolving runtime values
     * @param connectionType connection type
     * @param jdbcConnectionString JDBC connection strng
     * @param serverName server name
     * @param databaseName database name
     * @param schemaName schema name
     * @param username username
     * @param stage stage
     * @param ctx pipeline's context
     * @return created Connection
     */
    protected Connection createConnection(ConnectionType connectionType, String jdbcConnectionString, String serverName,
            String databaseName, String schemaName, String username, T stage, PipelineContext ctx) {
        // create connection with resolving runtime values
        jdbcConnectionString =
                jdbcConnectionString == null ? null : replaceRuntimeValueWithoutRemovingEL(jdbcConnectionString, ctx);
        serverName = serverName == null ? null : replaceRuntimeValueWithoutRemovingEL(serverName, ctx);
        username = username == null ? null : replaceRuntimeValueWithoutRemovingEL(username, ctx);
        databaseName = databaseName == null ? null : replaceRuntimeValueWithoutRemovingEL(databaseName, ctx);
        schemaName = schemaName == null ? null : replaceRuntimeValueWithoutRemovingEL(schemaName, ctx);

        Connection connection = new ConnectionImpl(connectionType.getId(), jdbcConnectionString, serverName,
                databaseName, schemaName, username);
        // set connection to stage settings
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        setting.setConnection(connection);

        return connection;
    }

}
