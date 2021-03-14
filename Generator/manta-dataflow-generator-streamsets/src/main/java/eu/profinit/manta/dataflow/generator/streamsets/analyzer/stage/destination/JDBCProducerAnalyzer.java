package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IColumnNameComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IJDBCProducerStage;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class JDBCProducerAnalyzer extends AbstractDatabaseStageAnalyzer<IJDBCProducerStage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCProducerAnalyzer.class);

    @Override public void analyzeFields(IJDBCProducerStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);

        Connection connection = createConnection(ConnectionType.JDBC, stage.getJdbcUrl(), null, null, stage.getSchema(),
                stage.getUsername(), stage, ctx);

        String schemaName = replaceRuntimeValueWithoutRemovingEL(stage.getSchema(), ctx);
        String tableName = replaceRuntimeValueWithoutRemovingEL(stage.getTable(), ctx);

        DataflowQueryService queryService = getQueryService();
        Node tableNode = queryService
                .addObjectNode(connection.getDatabaseName(), connection.getSchemaName(), tableName, connection,
                        ctx.getGraphHelper().graph());
        List<Node> columnNodes = queryService.addColumnNodes(tableNode, connection, ctx.getGraphHelper().graph());

        if (columnNodes != null) {
            for (Node columnNode : columnNodes) {
                // check mapping column-field
                String analyzedFieldPath = analyzeFieldName(stage, columnNode.getName());
                Field analyzedField = setting.getInputRootField().add(analyzedFieldPath, EFieldType.IO);
                setting.addNodeAndFieldPair(columnNode, analyzedField);
            }
        } else {
            LOGGER.error("Cannot found schema {} with table {}", schemaName, tableName);
            for (IColumnNameComponent component : stage.getColumnNamesComponents()) {
                setting.getInputRootField().add(component.getFieldName(), EFieldType.IO);
            }
        }
    }

    @Override public void createFlows(IJDBCProducerStage stage, PipelineContext ctx) {
        createFlowsAsSource(stage, ctx);
        super.createFlows(stage, ctx);
    }

    private void createFlowsAsSource(IJDBCProducerStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        if (setting.getNodeAndFieldPairs().isEmpty()) {
            String tableName = replaceRuntimeValueWithoutRemovingEL(stage.getTable(), ctx);
            Node tableNode = createTableNodeManually(stage, ctx, tableName);
            mergeAndConnectAllAnalyzedFieldsAsDestination(stage, ctx, tableNode);
        }
        for (Pair<Node, Field> pair : setting.getNodeAndFieldPairs()) {
            Node columnNode = pair.getKey();
            Node fieldNode = ctx.getGraphHelper().getNode(pair.getValue());
            ctx.getGraphHelper().addDirectFlow(fieldNode, columnNode);
        }
    }

    @Override protected void mergeAndConnectAllAnalyzedFieldsAsDestination(IJDBCProducerStage stage,
            PipelineContext ctx, Node tableNode) {
        for (Node fieldNode : ctx.getGraphHelper().getNode(stage).getChildren()) {
            String columnName = analyzeColumnName(stage, fieldNode.getName());
            Node columnNode = ctx.getGraphHelper()
                    .addNode(columnName, NodeType.COLUMN, tableNode, tableNode.getResource());
            ctx.getGraphHelper().addDirectFlow(fieldNode, columnNode);
        }
    }

    private String analyzeFieldName(IJDBCProducerStage stage, String columnName) {
        String fieldName = Constants.SLASH + columnName;
        for (IColumnNameComponent component : stage.getColumnNamesComponents()) {
            if (StringUtils.equalsIgnoreCase(columnName, component.getColumnName())) {
                fieldName = component.getFieldName();
                break;
            }
        }
        return fieldName;
    }

    private String analyzeColumnName(IJDBCProducerStage stage, String fieldName) {
        String columnName = fieldName.startsWith("/") ? fieldName.substring(1) : fieldName;
        for (IColumnNameComponent component : stage.getColumnNamesComponents()) {
            if (StringUtils.equalsIgnoreCase(fieldName, component.getFieldName())) {
                columnName = component.getColumnName();
            }
        }
        return columnName;
    }

}
