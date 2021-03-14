package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.ISchemaGeneratorStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;

/**
 *
 * @author mburdel
 */
public class SchemaGeneratorAnalyzer extends AbstractStageAnalyzer<ISchemaGeneratorStage> {

    @Override public void createNodes(ISchemaGeneratorStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    private void addStageAttributes(ISchemaGeneratorStage stage, PipelineContext ctx) {
        StreamSetsGraphHelper gh = ctx.getGraphHelper();
        gh.getNode(stage).addAttribute(Constants.ATTRIBUTE_SCHEMA_NAME,
                replaceRuntimeValueWithoutRemovingEL(stage.getSchemaName(), ctx));
        gh.getNode(stage).addAttribute(Constants.ATTRIBUTE_HEADER_ATTRIBUTE,
                replaceRuntimeValueWithoutRemovingEL(stage.getAttributeName(), ctx));
    }
}
