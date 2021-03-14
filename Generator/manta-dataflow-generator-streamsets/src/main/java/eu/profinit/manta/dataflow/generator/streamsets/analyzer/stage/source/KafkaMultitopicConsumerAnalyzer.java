package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.source.IKafkaMultitopicConsumerStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractDatabaseStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;

/**
 *
 * @author mburdel
 */
public class KafkaMultitopicConsumerAnalyzer extends AbstractDatabaseStageAnalyzer<IKafkaMultitopicConsumerStage> {

    @Override public void createNodes(IKafkaMultitopicConsumerStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        createService(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    private void addStageAttributes(IKafkaMultitopicConsumerStage stage, PipelineContext ctx) {
        Node stageNode = ctx.getGraphHelper().getNode(stage);
        if (stage.getTopics().size() == 1) {
            stageNode.addAttribute(Constants.ATTRIBUTE_KAFKA_CONSUMER_GROUP,
                    replaceRuntimeValueWithoutRemovingEL(stage.getConsumerGroup(), ctx));
        }
    }

    private void createService(IKafkaMultitopicConsumerStage stage, PipelineContext ctx) {
        // create application + service with resolving runtime values
        String applicationName = replaceRuntimeValueWithoutRemovingEL(stage.getBrokerUri(), ctx);
        String serviceName;
        if (stage.getTopics().size() == 1) {
            String topic = stage.getTopics().get(0);
            serviceName = replaceRuntimeValueWithoutRemovingEL(topic, ctx);
        } else {
            serviceName = replaceRuntimeValueWithoutRemovingEL(stage.getConsumerGroup(), ctx);
        }

        Node service = ctx.getGraphHelper()
                .createServiceNode(Constants.KAFKA_MULTITOPIC_RESOURCE_NAME, Constants.KAFKA_MULTITOPIC_RESOURCE_TYPE,
                        Constants.KAFKA_MULTITOPIC_RESOURCE_DESC, applicationName, serviceName);
        mergeAndConnectAllAnalyzedFieldsAsSource(stage, ctx, service);
    }
}
