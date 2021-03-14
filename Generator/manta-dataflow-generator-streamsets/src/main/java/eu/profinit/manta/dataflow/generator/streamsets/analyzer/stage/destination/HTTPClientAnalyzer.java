package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHTTPClientStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;

/**
 *
 * @author mburdel
 */
public class HTTPClientAnalyzer extends AbstractDatabaseStageAnalyzer<IHTTPClientStage> {

    @Override public void createFlows(IHTTPClientStage stage, PipelineContext ctx) {
        createService(stage, ctx);
        super.createFlows(stage, ctx);
    }

    /**
     * Method creates application with service.
     * @param stage stage
     * @param ctx context
     */
    private void createService(IHTTPClientStage stage, PipelineContext ctx) {
        // resolving runtime values
        String applicationName = replaceRuntimeValueWithoutRemovingEL(stage.getResourceUrl(), ctx);
        String serviceName = Constants.HTTP + replaceRuntimeValueWithoutRemovingEL(stage.getHttpMethod(), ctx);

        Node service = ctx.getGraphHelper()
                .createServiceNode(Constants.HTTP_RESOURCE_NAME, Constants.HTTP_RESOURCE_TYPE,
                        Constants.HTTP_RESOURCE_DESC, applicationName, serviceName);
        mergeAndConnectAllAnalyzedFieldsAsDestination(stage, ctx, service);
    }

}
