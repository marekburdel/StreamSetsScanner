package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.common.connections.query.Resultset;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IJDBCQueryConsumerStage;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryResult;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class JDBCQueryConsumerAnalyzer extends AbstractDatabaseStageAnalyzer<IJDBCQueryConsumerStage> {

    @Override public void analyzeFields(IJDBCQueryConsumerStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        // try more connection types to get query result with some output
        DataflowQueryResult queryResult = getQueryResult(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);

        setting.setQueryResult(queryResult);

        analyzeFieldsFromQueryResult(stage, ctx, queryResult);
    }

    private DataflowQueryResult getQueryResult(IJDBCQueryConsumerStage stage, PipelineContext ctx) {
        DataflowQueryService queryService = getQueryService();

        String sqlStatement = replaceRuntimeValueAndRemoveEL(stage.getSqlQuery(), ctx);
        String scriptNodeName = stage.getStageUIInfo().getLabel() + Constants.QUERY_RESULT;

        Connection connection;
        DataflowQueryResult queryResult;

        connection = createConnection(ConnectionType.JDBC, stage.getJdbcConnectionString(),
                stage.getJdbcConnectionString(), null, null, stage.getUsername(), stage, ctx);
        DataflowQueryResult unknownQueryResult = queryService
                .getDataFlow(null, scriptNodeName, sqlStatement, connection);
        if (!checkOutputMetadataIsEmpty(unknownQueryResult.getOutputMetadata())) {
            return unknownQueryResult;
        }

        // try all types of connections
        ConnectionType[] connectionTypes = ConnectionType.values();

        for (ConnectionType connectionType : connectionTypes) {
            connection = createConnection(connectionType, stage.getJdbcConnectionString(),
                    stage.getJdbcConnectionString(), null, null, stage.getUsername(), stage, ctx);
            queryResult = queryService.getDataFlow(null, scriptNodeName, sqlStatement, connection);
            if (!checkOutputMetadataIsEmpty(queryResult.getOutputMetadata())) {
                return queryResult;
            }
        }

        // save jdbc connection
        createConnection(ConnectionType.JDBC, stage.getJdbcConnectionString(), stage.getJdbcConnectionString(), null,
                null, stage.getUsername(), stage, ctx);

        return unknownQueryResult;
    }

    private boolean checkOutputMetadataIsEmpty(List<Resultset> outputMetadata) {
        if (outputMetadata.isEmpty()) {
            return true;
        }
        // check if resultsets are empty
        int count = 0;
        for (Resultset resultset : outputMetadata) {
            if (!resultset.getColumns().isEmpty()) {
                ++count;
            }
        }

        return count == 0;
    }

    @Override public void createNodes(IJDBCQueryConsumerStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    @Override public void createFlows(IJDBCQueryConsumerStage stage, PipelineContext ctx) {
        createFlowsFromQueryResultAsSource(stage, ctx);
        super.createFlows(stage, ctx);
    }

    private void addStageAttributes(IJDBCQueryConsumerStage stage, PipelineContext ctx) {
        ctx.getGraphHelper().getNode(stage).addAttribute(Constants.ATTRIBUTE_SQL_QUERY, stage.getSqlQuery());
    }

}
