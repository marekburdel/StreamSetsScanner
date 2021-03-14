package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.common.connections.Connection;
import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.common.connections.query.Column;
import eu.profinit.manta.connector.common.connections.query.Resultset;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.ISalesforceStage;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryResult;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Graph;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.Resource;
import eu.profinit.manta.dataflow.model.impl.GraphImpl;
import eu.profinit.manta.dataflow.model.impl.ResourceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mburdel
 */
public class SalesforceAnalyzer extends AbstractDatabaseStageAnalyzer<ISalesforceStage> {

    @Override public void analyzeFields(ISalesforceStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        // create connection only to get all used columns
        Connection connection = createConnection(ConnectionType.ORACLE, stage.getAuthEndpoint(),
                stage.getAuthEndpoint(), stage.getUsername(), stage.getUsername(), stage.getUsername(), stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);
        String soqlStatement = replaceRuntimeValueAndRemoveEL(stage.getSoqlQuery(), ctx);

        DataflowQueryService queryService = getQueryService();

        String scriptNodeName = stage.getStageUIInfo().getLabel() + Constants.QUERY_RESULT;
        DataflowQueryResult queryResult = queryService.getDataFlow(null, scriptNodeName, soqlStatement, connection);

        setting.setQueryResult(queryResult);

        analyzeFieldsFromQueryResult(stage, ctx, queryResult);
    }

    /**
     * Firstly method creates temporary graph. To this graph adds stage's node with corresponding columns' nodes.
     * Then connects and merges it with QueryResult. Merged graph is used to get table's name.
     * @param stage stage
     * @param ctx context
     * @return Table Name that is resolved by Query Result
     */
    private String analyzeTableNameFromQueryResult(ISalesforceStage stage, PipelineContext ctx) {
        Resource tmpResource = new ResourceImpl("tmp", "tmp", "tmp");
        Graph tmpGraph = new GraphImpl(tmpResource);

        Node tmpStageNode = tmpGraph.addNode("Stage", "tmp", null, tmpResource);
        DataflowQueryResult queryResult = ctx.getSetting(stage).getQueryResult();
        List<Resultset> resultsets = queryResult.getOutputMetadata();
        for (Resultset resultset : resultsets) {
            for (Column column : resultset.getColumns()) {
                tmpGraph.addNode(column.getName(), "tmp", tmpStageNode, tmpResource);
            }
        }

        queryResult.connectAllResultsetsTo(new ArrayList<>(tmpGraph.getChildren(tmpStageNode)),
                DataflowQueryResult.MatchStrategy.MATCH_BY_NAME_CI);
        queryResult.mergeTo(tmpGraph);

        // get table name from merged graph
        if (!tmpGraph.getChildren(tmpStageNode).isEmpty()) {
            Node field = tmpGraph.getChildren(tmpStageNode).iterator().next();
            if (!field.getIncomingEdges().isEmpty()) {
                Node column = field.getIncomingEdges().iterator().next().getSource();
                String tableName = column.getParent().getName();
                return tableName;
            }
        }
        return null;
    }

    @Override public void createNodes(ISalesforceStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    @Override public void createFlows(ISalesforceStage stage, PipelineContext ctx) {
        createService(stage, ctx);
        super.createFlows(stage, ctx);
    }

    /**
     * Method creates Application/Service nodes' structure for Stage
     * @param stage stage
     * @param ctx context
     */
    private void createService(ISalesforceStage stage, PipelineContext ctx) {
        // resolving runtime values
        String applicationName = replaceRuntimeValueWithoutRemovingEL(stage.getUsername(), ctx);
        String serviceName = analyzeTableNameFromQueryResult(stage, ctx);

        applicationName = applicationName == null || applicationName.isEmpty() ?
                          Constants.SALESFORCE_APPLICATION :
                          applicationName;
        serviceName = serviceName == null || serviceName.isEmpty() ? Constants.DEFAULT_TABLE_NAME : serviceName;

        Node service = ctx.getGraphHelper()
                .createServiceNode(Constants.SALESFORCE_RESOURCE_NAME, Constants.SALESFORCE_RESOURCE_TYPE,
                        Constants.SALESFORCE_RESOURCE_DESC, applicationName, serviceName);
        mergeAndConnectAllAnalyzedFieldsAsSource(stage, ctx, service);
    }

    private void addStageAttributes(ISalesforceStage stage, PipelineContext ctx) {
        ctx.getGraphHelper().getNode(stage).addAttribute(Constants.ATTRIBUTE_SOQL_QUERY, stage.getSoqlQuery());
    }

}
