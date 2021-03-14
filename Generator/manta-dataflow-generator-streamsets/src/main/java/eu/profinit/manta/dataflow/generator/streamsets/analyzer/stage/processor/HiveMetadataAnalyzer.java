package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IHiveMetadataStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class HiveMetadataAnalyzer extends AbstractDatabaseStageAnalyzer<IHiveMetadataStage> {

    @Override public void createNodes(IHiveMetadataStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    private void addStageAttributes(IHiveMetadataStage stage, PipelineContext ctx) {
        ctx.getGraphHelper().getNode(stage).addAttribute(Constants.ATTRIBUTE_JDBC_URL, stage.getHiveJDBCUrl());
        ctx.getGraphHelper().getNode(stage).addAttribute(Constants.ATTRIBUTE_DATABASE_NAME, stage.getDbNameEL());
        ctx.getGraphHelper().getNode(stage).addAttribute(Constants.ATTRIBUTE_TABLE_NAME, stage.getTableNameEL());
    }

}
