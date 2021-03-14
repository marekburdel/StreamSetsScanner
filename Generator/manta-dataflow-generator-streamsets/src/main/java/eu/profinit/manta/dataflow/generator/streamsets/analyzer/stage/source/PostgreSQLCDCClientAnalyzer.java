package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IPostgreSQLCDCClientStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class PostgreSQLCDCClientAnalyzer extends AbstractDatabaseStageAnalyzer<IPostgreSQLCDCClientStage> {

    @Override public void analyzeFields(IPostgreSQLCDCClientStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeFieldsFromSchemaTableComponents(stage, ctx, stage.getJdbcConnectionString(), stage.getUsername(),
                stage.getSchemaTableComponents(), ConnectionType.POSTGRESQL);
    }

    @Override public void createFlows(IPostgreSQLCDCClientStage stage, PipelineContext ctx) {
        createFlowsAsSourceFromSchemaTableComponents(stage, ctx, stage.getJdbcConnectionString(), stage.getUsername(),
                stage.getSchemaTableComponents());
        super.createFlows(stage, ctx);
    }

}
