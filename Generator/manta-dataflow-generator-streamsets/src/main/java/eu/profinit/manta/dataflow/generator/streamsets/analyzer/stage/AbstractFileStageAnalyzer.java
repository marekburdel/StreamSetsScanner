package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;

/**
 * Abstract class for file's operations
 * @author mburdel
 */
public abstract class AbstractFileStageAnalyzer<T extends IStage> extends AbstractStageAnalyzer<T> {

    /**
     * Method creates node for file as source, adds to this created node analyzed fields as columns
     * and then creates flows between its.
     * @param stage stage
     * @param ctx context
     * @param path file path
     */
    protected void createFileNodeWithColumnsAndFlowsAsSource(T stage, PipelineContext ctx, String path) {
        path = replaceRuntimeValueWithoutRemovingEL(path, ctx);
        path = path.replaceAll("/+", "/");
        Node fileNode = getNodeCreator().createFileNode(ctx.getGraphHelper().graph(), path);

        connectStageAndFileNodeAsSource(stage, ctx, fileNode);
    }

    /**
     * Method creates node for file as destination, adds to this created node analyzed fields as columns
     * and then creates flows between its.
     * @param stage stage
     * @param ctx context
     * @param path file path
     */
    protected void createFileNodeWithColumnsAndFlowsAsDestination(T stage, PipelineContext ctx, String path) {
        path = replaceRuntimeValueWithoutRemovingEL(path, ctx);
        Node fileNode = getNodeCreator().createFileNode(ctx.getGraphHelper().graph(), path);

        connectStageAndFileNodeAsDestination(stage, ctx, fileNode);
    }

    /**
     * Method adds all known columns into file node and creates flows between its and analyzed fields.
     * @param stage stage
     * @param ctx context
     * @param fileNode file node
     */
    protected void connectStageAndFileNodeAsSource(T stage, PipelineContext ctx, Node fileNode) {
        for (Node fieldNode : ctx.getGraphHelper().getNode(stage).getChildren()) {
            String columnName = defineColumnNameByFieldPath(fieldNode.getName());
            Node columnNode = ctx.getGraphHelper()
                    .addNode(columnName, NodeType.COLUMN, fileNode, fileNode.getResource());
            ctx.getGraphHelper().addDirectFlow(columnNode, fieldNode);
        }
    }

    /**
     * Method adds all known columns into file node and creates flows between its and analyzed fields.
     * @param stage stage
     * @param ctx context
     * @param fileNode file node
     */
    protected void connectStageAndFileNodeAsDestination(T stage, PipelineContext ctx, Node fileNode) {
        for (Node fieldNode : ctx.getGraphHelper().getNode(stage).getChildren()) {
            String columnName = defineColumnNameByFieldPath(fieldNode.getName());
            Node columnNode = ctx.getGraphHelper()
                    .addNode(columnName, NodeType.COLUMN, fileNode, fileNode.getResource());
            ctx.getGraphHelper().addDirectFlow(fieldNode, columnNode);
        }
    }

}
