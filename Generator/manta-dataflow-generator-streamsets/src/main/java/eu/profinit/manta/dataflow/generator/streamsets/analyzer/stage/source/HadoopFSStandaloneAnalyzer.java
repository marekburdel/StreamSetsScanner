package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.source.IHadoopFSStandaloneStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractFileStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class HadoopFSStandaloneAnalyzer extends AbstractFileStageAnalyzer<IHadoopFSStandaloneStage> {

    @Override public void createFlows(IHadoopFSStandaloneStage stage, PipelineContext ctx) {
        String hdfsUri = replaceRuntimeValueWithoutRemovingEL(stage.getHdfsUri(), ctx);
        String filesDirectory = replaceRuntimeValueWithoutRemovingEL(stage.getDirectoryPath(), ctx);
        String fileNamePattern = replaceRuntimeValueWithoutRemovingEL(stage.getFilePattern(), ctx);

        hdfsUri = hdfsUri.isEmpty() ? Constants.DEFAULT_URI : hdfsUri;
        String path = hdfsUri + Constants.SLASH + filesDirectory + Constants.SLASH + fileNamePattern;

        createFileNodeWithColumnsAndFlowsAsSource(stage, ctx, path);
        super.createFlows(stage, ctx);
    }
}
