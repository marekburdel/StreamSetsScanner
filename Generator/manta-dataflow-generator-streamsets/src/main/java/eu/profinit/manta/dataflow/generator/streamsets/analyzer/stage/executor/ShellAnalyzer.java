package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.executor;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IEnvironmentVariableComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.executor.IShellStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.Dependence;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class ShellAnalyzer extends AbstractStageAnalyzer<IShellStage> {

    @Override public void analyzeFields(IShellStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);
        analyzeEnvironmentVariables(stage, ctx);
    }

    /**
     * Method analyzes all environment variables expression to find valid field path(s)
     * @param stage stage
     * @param ctx pipeline's context
     */
    private void analyzeEnvironmentVariables(IShellStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        ExpressionLanguageHelper elHelper = getParserService().getExpressionLanguageHelper();

        for (IEnvironmentVariableComponent component : stage.getEnvironmentVariableComponents()) {
            List<Dependence> dependencies = elHelper.getDependenciesFromEL(component.getValue());
            analyzeDependencies(setting.getOutputRootField(), dependencies);
        }
    }

    private void analyzeDependencies(Field outputRootField, List<Dependence> dependencies) {
        for (Dependence dependence : dependencies) {
            outputRootField.add(dependence.getFieldPath(), EFieldType.IO);
        }
    }

    @Override public void createNodes(IShellStage stage, PipelineContext ctx) {
        super.createNodes(stage, ctx);
        addStageAttributes(stage, ctx);
    }

    private void addStageAttributes(IShellStage stage, PipelineContext ctx) {
        ctx.getGraphHelper().getNode(stage)
                .addAttribute(Constants.ATTRIBUTE_SCRIPT, replaceRuntimeValueWithoutRemovingEL(stage.getScript(), ctx));
    }
}
