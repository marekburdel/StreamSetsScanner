package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.connector.streamsets.model.model.IPipeline;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageNodeTypeGenerator;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pipeline analyzer
 *
 * @author mburdel
 */
public class PipelineAnalyzer implements IAnalyzer<IPipeline>, ISequentialAnalyzer<IPipeline> {

    private PipelineConfigAnalyzer pipelineConfigAnalyzer;

    @Override public void analyze(IPipeline pipeline, StreamSetsGraphHelper gh) {
        PipelineContext ctx = new PipelineContext(pipeline, gh);
        init(pipeline, ctx);
        analyzeFields(pipeline, ctx);
        analyzeBindings(pipeline, ctx);
        expandFields(pipeline, ctx);
        createNodesAndFlows(pipeline, ctx);
    }

    @Override public void init(IPipeline pipeline, PipelineContext ctx) {
        pipelineConfigAnalyzer.init(pipeline.getPipelineConfig(), ctx);
    }

    @Override public void analyzeBindings(IPipeline pipeline, PipelineContext ctx) {
        pipelineConfigAnalyzer.analyzeBindings(pipeline.getPipelineConfig(), ctx);
    }

    @Override public void analyzeFields(IPipeline pipeline, PipelineContext ctx) {
        pipelineConfigAnalyzer.analyzeFields(pipeline.getPipelineConfig(), ctx);
    }

    @Override public void expandFields(IPipeline pipeline, PipelineContext ctx) {
        pipelineConfigAnalyzer.expandFields(pipeline.getPipelineConfig(), ctx);
    }

    @Override public void createNodesAndFlows(IPipeline pipeline, PipelineContext ctx) {
        // create pipeline's node
        Node pipelineNode = ctx.getGraphHelper()
                .buildNode(pipeline, pipeline.getPipelineConfig().getPipelineId(), NodeType.STREAMSETS_PIPELINE,
                        ctx.getGraphHelper().getNode(ctx.getGraphHelper().getServer()));
        pipelineNode.addAttribute(Constants.ATTRIBUTE_PIPELINE_TITLE, pipeline.getPipelineConfig().getTitle());

        /*
        Build nodes for all stages in pipeline.
        To provide the best possible stages' nodes' names visualization, firstly we try to detect duplicate.
        If duplicate is found, just append unique instance stage name between parentheses behind stage's name.
        Else use only defined stage's name by user.
         */
        if (detectDuplicateStageName(pipeline.getPipelineConfig().getStages())) {
            for (IStage stage : pipeline.getPipelineConfig().getStages()) {
                String nodeName = stage.getStageUIInfo().getLabel() + " (" + stage.getInstanceName() + ")";
                ctx.getGraphHelper()
                        .buildNode(stage, nodeName, StageNodeTypeGenerator.getNodeType(stage.getStageName()),
                                pipelineNode);
            }
        } else {
            for (IStage stage : pipeline.getPipelineConfig().getStages()) {
                String nodeName = stage.getStageUIInfo().getLabel();
                // check empty stage name
                nodeName = nodeName.isEmpty() ? stage.getInstanceName() : nodeName;
                ctx.getGraphHelper()
                        .buildNode(stage, nodeName, StageNodeTypeGenerator.getNodeType(stage.getStageName()),
                                pipelineNode);
            }
        }

        pipelineConfigAnalyzer.createNodesAndFlows(pipeline.getPipelineConfig(), ctx);
    }

    /**
     *
     * @param stages pipeline's stages
     * @return <code>true</code> if stages' names contains duplicity (at least one stage has same name as another stage)
     * else <code>false</code>
     */
    private boolean detectDuplicateStageName(List<IStage> stages) {
        Set<String> stageNames = new HashSet<>();
        for (IStage stage : stages) {
            if (!stageNames.add(stage.getStageUIInfo().getLabel())) {
                return true;
            }
        }
        return false;
    }

    public IStage getSourceStage() {
        throw new UnsupportedOperationException();
    }

    public List<IStage> getTargetStages() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param sourceStage destination stage from input pipeline
     * @param targetStage source stage from output pipeline
     * @return boolean <code>true</code> if pipelines were merged with some field(s) change else <code>false</code>.
     */
    public boolean mergeStages(IStage sourceStage, IStage targetStage) {
        // UNSUPPORTED
        return true;
    }

    public void setPipelineConfigAnalyzer(PipelineConfigAnalyzer pipelineConfigAnalyzer) {
        this.pipelineConfigAnalyzer = pipelineConfigAnalyzer;
    }
}
