package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.common.connections.ConnectionType;
import eu.profinit.manta.connector.streamsets.model.model.stage.source.IOracleCDCClientStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class OracleCDCClientAnalyzer extends AbstractDatabaseStageAnalyzer<IOracleCDCClientStage> {

    @Override public void analyzeFields(IOracleCDCClientStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeFieldsFromSchemaTableComponents(stage, ctx, stage.getJdbcConnectionString(), stage.getUsername(),
                stage.getSchemaTableComponents(), ConnectionType.ORACLE);
    }

    @Override public void createFlows(IOracleCDCClientStage stage, PipelineContext ctx) {
        createFlowsAsSourceFromSchemaTableComponents(stage, ctx, stage.getJdbcConnectionString(), stage.getUsername(),
                stage.getSchemaTableComponents());
        super.createFlows(stage, ctx);
    }

}
