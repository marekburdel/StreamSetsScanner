package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.destination.ILocalFSStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractFileStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class LocalFSAnalyzer extends AbstractFileStageAnalyzer<ILocalFSStage> {

    @Override public void createFlows(ILocalFSStage stage, PipelineContext ctx) {
        String dirPath = stage.getDirPathTemplate();
        String prefix = stage.getUniquePrefix();

        String path;
        if (stage.getFileNameSuffix() != null && !stage.getFileNameSuffix().isEmpty()) {
            path = dirPath + Constants.SLASH + prefix + stage.getFileNameSuffix();
        } else {
            path = dirPath + Constants.SLASH + prefix;
        }

        createFileNodeWithColumnsAndFlowsAsDestination(stage, ctx, path);
        super.createFlows(stage, ctx);
    }
}
