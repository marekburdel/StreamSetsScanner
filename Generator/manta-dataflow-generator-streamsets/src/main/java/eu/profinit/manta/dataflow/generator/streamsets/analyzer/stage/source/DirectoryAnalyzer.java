package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.source;

import eu.profinit.manta.connector.streamsets.model.model.stage.source.IDirectoryStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.Constants;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractFileStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;

/**
 *
 * @author mburdel
 */
public class DirectoryAnalyzer extends AbstractFileStageAnalyzer<IDirectoryStage> {

    @Override public void createFlows(IDirectoryStage stage, PipelineContext ctx) {
        String filesDirectory = stage.getDirectoryPath();
        String fileNamePattern = stage.getFilePattern();

        String path = filesDirectory + Constants.SLASH + fileNamePattern;
        createFileNodeWithColumnsAndFlowsAsSource(stage, ctx, path);
        super.createFlows(stage, ctx);
    }

}
