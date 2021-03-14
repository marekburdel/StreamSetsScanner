package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.destination;

import eu.profinit.manta.connector.streamsets.model.model.stage.destination.IHadoopFSStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractFileStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class HadoopFSAnalyzer extends AbstractFileStageAnalyzer<IHadoopFSStage> {

    @Override public void createFlows(IHadoopFSStage stage, PipelineContext ctx) {
        String hdfsUri = replaceRuntimeValueWithoutRemovingEL(stage.getHdfsUri(), ctx);
        String dirPath = replaceRuntimeValueWithoutRemovingEL(stage.getDirectoryTemplate(), ctx);
        String prefix = replaceRuntimeValueWithoutRemovingEL(stage.getUniquePrefix(), ctx);

        hdfsUri = hdfsUri.isEmpty() ? Constants.DEFAULT_URI : hdfsUri;
        dirPath = dirPath.isEmpty() ? Constants.DEFAULT_DIR : dirPath;

        String path;
        if (stage.getFileNameSuffix() != null && !stage.getFileNameSuffix().isEmpty()) {
            path = hdfsUri + Constants.SLASH + dirPath + Constants.SLASH + prefix + stage.getFileNameSuffix();
        } else {
            path = hdfsUri + Constants.SLASH + dirPath + Constants.SLASH + prefix;
        }

        createFileNodeWithColumnsAndFlowsAsDestination(stage, ctx, path);
        super.createFlows(stage, ctx);
    }
}
