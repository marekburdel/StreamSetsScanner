package eu.profinit.manta.dataflow.generator.streamsets.analyzer;

import eu.profinit.manta.connector.streamsets.model.model.IPipelineConfig;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Binding;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Pipeline Config analyzer
 *
 * @author mburdel
 */
@SuppressWarnings({ "rawtypes", "unchecked" }) public class PipelineConfigAnalyzer
        implements ISequentialAnalyzer<IPipelineConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineConfigAnalyzer.class);

    /**
     * Map for stages' analyzers loaded from configuration file
     */
    private Map<IStageType.StageName, AbstractStageAnalyzer> stageAnalyzers;

    @Override public void analyzeFields(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        for (IStage stage : pipelineConfig.getStages()) {
            getStageAnalyzer(stage.getStageName()).analyzeFields(stage, ctx);
        }
    }

    @Override public void init(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        initAnalyzersSettings(pipelineConfig, ctx);
        initExpansionMap(pipelineConfig, ctx);
        ctx.setLanes(pipelineConfig.getLanes());
    }

    @Override public void analyzeBindings(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        for (IStage stage : pipelineConfig.getStages()) {
            analyzeBindings(pipelineConfig, stage, ctx);
        }
    }

    /**
     * Method adds all found connections between stages as biding to stage's settings.
     * @param pipelineConfig pipeline's config
     * @param targetStage target stage
     * @param ctx pipeline's context
     */
    private void analyzeBindings(IPipelineConfig pipelineConfig, IStage targetStage, PipelineContext ctx) {
        for (String laneId : targetStage.getInputLanes()) {
            // ignore event lanes
            if (pipelineConfig.getLanes().get(laneId) == null) {
                continue;
            }
            IStage sourceStage = pipelineConfig.getLanes().get(laneId).getSourceStage();
            Field sourceField = ctx.getSetting(sourceStage).getOutputRootField();
            Field targetField = ctx.getSetting(targetStage).getInputRootField();

            Binding binding = new Binding(sourceField, targetField, sourceStage, targetStage);
            ctx.getSetting(sourceStage).addOutputBinding(binding);
            ctx.getSetting(targetStage).addInputBinding(binding);
        }
    }

    @Override public void expandFields(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        boolean isExpanded = false;
        while (!isExpanded) {
            isExpanded = true;
            for (Map.Entry<IStage, Boolean> expansion : ctx.getExpansionMap().entrySet()) {
                if (!expansion.getValue()) {
                    isExpanded = false;
                    IStage stage = expansion.getKey();
                    getStageAnalyzer(stage.getStageName()).expandFields(stage, ctx);
                }
            }
        }
        // unsupported - topology expansion - get source/destinations and reset their value to false in expansionMap
    }

    @Override public void createNodesAndFlows(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        // check for empty stage(unknown field)
        if (unknownFieldExists(pipelineConfig, ctx)) {
            addUnknownFieldToAllRootFields(pipelineConfig, ctx);
        }
        for (IStage stage : pipelineConfig.getStages()) {
            getStageAnalyzer(stage.getStageName()).createNodes(stage, ctx);
        }
        for (IStage stage : pipelineConfig.getStages()) {
            getStageAnalyzer(stage.getStageName()).createFlows(stage, ctx);
        }
    }

    /**
     * Method creates stages' settings for all stages
     * @param pipelineConfig pipeline's config
     * @param ctx pipeline's context
     */
    private void initAnalyzersSettings(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        Map<IStage, StageAnalyzerSetting> settings = new HashMap<>();
        for (IStage stage : pipelineConfig.getStages()) {
            settings.put(stage, new StageAnalyzerSetting());
        }
        ctx.setSettings(settings);
    }

    /**
     * Method creates expansion map for fields' expansion's phase
     * @param pipelineConfig pipeline's config
     * @param ctx pipeline's context
     */
    private void initExpansionMap(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        Map<IStage, Boolean> expansionMap = new HashMap<>();
        for (IStage stage : pipelineConfig.getStages()) {
            expansionMap.put(stage, false);
        }
        ctx.setExpansionMap(expansionMap);
    }

    private AbstractStageAnalyzer getStageAnalyzer(String stageName) {
        AbstractStageAnalyzer analyzer = stageAnalyzers.get(IStageType.StageName.getStageName(stageName));
        if (analyzer == null) {
            return stageAnalyzers.get(IStageType.StageName.DEFAULT_STAGE);
        }
        return analyzer;
    }

    public void setStageAnalyzers(Map<IStageType.StageName, AbstractStageAnalyzer> stageAnalyzers) {
        this.stageAnalyzers = stageAnalyzers;
    }

    /**
     *
     * @param pipelineConfig pipeline config
     * @param ctx context
     * @return If one of any stages' root fields don't have at least one child return <code>true</code>
     * else <code>false</code>
     */
    private boolean unknownFieldExists(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        for (IStage stage : pipelineConfig.getStages()) {
            if (!ctx.getSetting(stage).getInputRootField().hasChildren()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method adds unknown field to all stages
     * @param pipelineConfig pipeline's config
     * @param ctx pipeline's context
     */
    private void addUnknownFieldToAllRootFields(IPipelineConfig pipelineConfig, PipelineContext ctx) {
        for (IStage stage : pipelineConfig.getStages()) {
            StageAnalyzerSetting setting = ctx.getSetting(stage);

            for (Field rootField : setting.getInnerFields()) {
                rootField.add("/UNKNOWN", EFieldType.IO);
            }
        }
    }

}
