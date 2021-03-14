package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IKafkaProducerStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;

/**
 *
 * @author mburdel
 */
public class KafkaProducerAnalyzer extends AbstractDatabaseStageAnalyzer<IKafkaProducerStage> {

    @Override public void createFlows(IKafkaProducerStage stage, PipelineContext ctx) {
        createService(stage, ctx);
        super.createFlows(stage, ctx);
    }

    /**
     * Method creates application + service with resolving runtime values
     * @param stage stage
     * @param ctx context
     */
    private void createService(IKafkaProducerStage stage, PipelineContext ctx) {
        String applicationName = replaceRuntimeValueWithoutRemovingEL(stage.getBrokerUri(), ctx);
        String serviceName = replaceRuntimeValueWithoutRemovingEL(stage.getTopic(), ctx);

        Node service = ctx.getGraphHelper()
                .createServiceNode(Constants.KAFKA_PRODUCER_RESOURCE_NAME, Constants.KAFKA_PRODUCER_RESOURCE_TYPE,
                        Constants.KAFKA_PRODUCER_RESOURCE_DESC, applicationName, serviceName);
        mergeAndConnectAllAnalyzedFieldsAsDestination(stage, ctx, service);
    }

}
