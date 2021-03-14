package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHiveMetastoreStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IHiveMetadataStage;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class HiveMetastoreAnalyzer extends AbstractDatabaseStageAnalyzer<IHiveMetastoreStage> {

    @Override public void analyzeFields(IHiveMetastoreStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        // create connection with resolving runtime values
        String jdbcUrl = replaceRuntimeValueWithoutRemovingEL(stage.getHiveJDBCUrl(), ctx);
        String username = null;
        if (stage.getUseCredentials()) {
            username = replaceRuntimeValueWithoutRemovingEL(stage.getUsername(), ctx);
        }

        String databaseName = null;
        String tableName = null;

        // get information about database name and table name from HiveMetadata Stage
        IHiveMetadataStage stageHiveMetadata = findHiveMetadataStage(stage, ctx);
        if (stageHiveMetadata != null) {
            databaseName = replaceRuntimeValueWithoutRemovingEL(stageHiveMetadata.getDbNameEL(), ctx);
            tableName = replaceRuntimeValueWithoutRemovingEL(stageHiveMetadata.getTableNameEL(), ctx);
        }

        Connection connection = createConnection(ConnectionType.HIVE, jdbcUrl, jdbcUrl, databaseName, null, username,
                stage, ctx);

        setting.setConnection(connection);

        DataflowQueryService queryService = getQueryService();
        Node tableNode = queryService
                .addObjectNode(connection.getDatabaseName(), connection.getSchemaName(), tableName, connection,
                        ctx.getGraphHelper().graph());
        List<Node> columnNodes = queryService.addColumnNodes(tableNode, connection, ctx.getGraphHelper().graph());

        if (columnNodes != null) {
            for (Node columnNode : columnNodes) {
                String analyzedFieldPath = Constants.SLASH + columnNode.getName();
                Field analyzedField = setting.getInputRootField().add(analyzedFieldPath, EFieldType.IO);
                setting.addNodeAndFieldPair(columnNode, analyzedField);
            }
        }
    }

    private IHiveMetadataStage findHiveMetadataStage(IHiveMetastoreStage stage, PipelineContext ctx) {
        for (String laneId : stage.getInputLanes()) {
            IStage sourceStage = ctx.getLanes().get(laneId).getSourceStage();
            if (sourceStage instanceof IHiveMetadataStage) {
                return (IHiveMetadataStage) sourceStage;
            }
        }

        return null;
    }

    @Override public void createFlows(IHiveMetastoreStage stage, PipelineContext ctx) {
        createFlowsAsSource(stage, ctx);
        super.createFlows(stage, ctx);
    }

    private void createFlowsAsSource(IHiveMetastoreStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        if (setting.getNodeAndFieldPairs().isEmpty()) {
            String tableName = null;
            IHiveMetadataStage stageHiveMetadata = findHiveMetadataStage(stage, ctx);
            if (stageHiveMetadata != null) {
                tableName = replaceRuntimeValueWithoutRemovingEL(stageHiveMetadata.getTableNameEL(), ctx);
            }
            Node tableNode = createTableNodeManually(stage, ctx, tableName);
            mergeAndConnectAllAnalyzedFieldsAsDestination(stage, ctx, tableNode);
        }
        for (Pair<Node, Field> pair : setting.getNodeAndFieldPairs()) {
            Node columnNode = pair.getKey();
            Node fieldNode = ctx.getGraphHelper().getNode(pair.getValue());
            ctx.getGraphHelper().addDirectFlow(fieldNode, columnNode);
        }
    }

}
