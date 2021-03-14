package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStage;
import eu.profinit.manta.dataflow.generator.common.query.DataflowQueryService;
import eu.profinit.manta.dataflow.generator.modelutils.NodeCreator;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.*;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.generator.streamsets.helper.StreamSetsGraphHelper;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import eu.profinit.manta.dataflow.model.Edge;
import eu.profinit.manta.dataflow.model.Node;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Abstract analyzer class
 * @author mburdel
 */
public abstract class AbstractStageAnalyzer<T extends IStage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStageAnalyzer.class);

    private DataflowQueryService queryService;

    private NodeCreator nodeCreator;

    /**
     * Service for parsing fields
     */
    private FieldParserService parserService;

    /**
     * Method analyzes fields for Stage.
     *
     * @param stage stage for fields' analysis
     * @param ctx context with all needed information
     */
    public void analyzeFields(T stage, PipelineContext ctx) {
        // create root field
        Field rootField = new Field("/", EFieldType.IO, parserService);
        ctx.getSetting(stage).addInnerField(rootField);
    }

    /**
     * Method expands analyzed fields to all neighboring objects with expansion algorithm.
     *
     * @param stage stage for fields' expansion
     * @param ctx context with all needed information
     */
    public void expandFields(T stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        Map<IStage, Boolean> expansionMap = ctx.getExpansionMap();

        inputExpansion(expansionMap, setting);
        if (otherExpansion(setting) || outputExpansion(expansionMap, setting)) {
            // If new field is found, we need to expand it to all stages. Otherwise input stages won't know about its.
            outputExpansion(expansionMap, setting);
            otherExpansion(setting);
            inputExpansion(expansionMap, setting);
        }
        expansionMap.put(stage, true);
    }

    /**
     * Method creates nodes for all stage's analyzer's fields with additional attributes.
     *
     * @param stage stage for nodes' creation
     * @param ctx context with all needed information
     */
    public void createNodes(T stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();

        // creates nodes for stage with only one rootField, otherwise stages' analyzers should manage it itself
        if (setting.getInnerFields().size() == 1) {
            setting.getInnerFields().get(0).createNodes(gh.getNode(stage), gh);
        }
        createFieldAttributes(stage, ctx);
    }

    /**
     * Method creates flows for stage's output/event lanes and for inner bindings(additional flows)
     * that were created analysis of fields.
     *
     * @param stage stage for flows' creation
     * @param ctx context with all needed information
     */
    public void createFlows(T stage, PipelineContext ctx) {
        createOtherStageFlows(stage, ctx);
        createOutputStageFlows(stage, ctx);
    }

    /**
     * Method creates fields' attributes that added to stages' settings during fields' analysis (before nodes' creation)
     * @param stage stage
     * @param ctx context
     */
    protected void createFieldAttributes(IStage stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);
        StreamSetsGraphHelper gh = ctx.getGraphHelper();

        for (FieldAttribute fieldAttribute : setting.getFieldAttributes()) {
            if (fieldAttribute.getField().getFieldPath().equals("/")) {
                gh.getNode(stage).addAttribute(fieldAttribute.getKey(), fieldAttribute.getValue());
            } else {
                addAttributeToFieldChildren(fieldAttribute, fieldAttribute.getField(), gh);
            }
        }
    }

    /**
     * Method adds field's attribute to its children, if field has children. Else adds attribute to field.
     * @param fieldAttribute field attribute with field's path, attribute's key and value
     * @param field field
     * @param gh graph helper
     */
    private void addAttributeToFieldChildren(FieldAttribute fieldAttribute, Field field, StreamSetsGraphHelper gh) {
        if (field.hasChildren()) {
            for (Field child : field.getChildren().values()) {
                addAttributeToFieldChildren(fieldAttribute, child, gh);
            }
        } else {
            gh.getNode(field).addAttribute(fieldAttribute.getKey(), fieldAttribute.getValue());
        }
    }

    /**
     * Method creates other(inner) stage's flows that was analyzed before.
     * @param stage stage
     * @param ctx context
     */
    protected void createOtherStageFlows(T stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        for (FieldFlow fieldFlow : setting.getFieldFlows()) {
            createOtherFlows(fieldFlow.getSourceField(), fieldFlow.getTargetField(), fieldFlow.getFlowType(),
                    ctx.getGraphHelper());
        }
    }

    /**
     * Method creates flows between this stage(output root field) and output stages(input root fields).
     * @param stage stage
     * @param ctx context
     */
    protected void createOutputStageFlows(T stage, PipelineContext ctx) {
        StageAnalyzerSetting setting = ctx.getSetting(stage);

        List<Field> leafs = setting.getOutputRootField().getOutputLeafs();

        for (Binding binding : setting.getOutputBindings()) {
            Field targetRootField = binding.getTargetField();
            for (Field leaf : leafs) {
                Field targetLeaf = targetRootField.findFieldByPath(leaf.getFieldPath());
                if (targetLeaf != null) {
                    if (targetLeaf.getFieldType() != EFieldType.BEGIN) {
                        createDirectFlow(leaf, targetLeaf, ctx.getGraphHelper());
                    }
                } else {
                    LOGGER.debug("Target leaf doesn't exist. Source stage: {}, Target stage: {}, "
                                    + "Leaf path: {}, Leaf type: {}", stage.getStageName(),
                            binding.getTargetStage().getStageName(), leaf.getFieldPath(), leaf.getFieldType());
                }
            }
        }
    }

    /**
     * Method starts merging between this stage and inputs' stages.
     * @param expansionMap expansion map
     * @param setting stage's setting
     * @return <code>true</code> if some root field was changed (at least one child was added) else <code>false</code>
     */
    protected boolean inputExpansion(Map<IStage, Boolean> expansionMap, StageAnalyzerSetting setting) {
        boolean expanded = false;
        for (Binding binding : setting.getInputBindings()) {
            if (binding.merge()) {
                expansionMap.put(binding.getSourceStage(), false);
                expanded = true;
            }
        }
        return expanded;
    }

    /**
     * Method starts merging inner fields for current stage.
     * @param setting stage's setting
     * @return <code>true</code> if inner fields were changed by expansion else <code>false</code>
     */
    protected boolean otherExpansion(StageAnalyzerSetting setting) {
        boolean expanded = false;
        for (Binding binding : setting.getOtherBindings()) {
            if (binding.merge()) {
                expanded = true;
            }
        }
        List<Binding> otherBindings = setting.getOtherBindings();
        for (int i = otherBindings.size() - 1; i >= 0; --i) {
            Binding binding = otherBindings.get(i);
            if (binding.merge()) {
                expanded = true;
            }
        }
        return expanded;
    }

    /**
     * Method starts merging between this stage and outputs' stages.
     * @param expansionMap expansion map
     * @param setting stage's setting
     * @return <code>true</code> if some root field was changed (at least one child was added) else <code>false</code>
     */
    protected boolean outputExpansion(Map<IStage, Boolean> expansionMap, StageAnalyzerSetting setting) {
        boolean expanded = false;
        for (Binding binding : setting.getOutputBindings()) {
            if (binding.merge()) {
                expansionMap.put(binding.getTargetStage(), false);
                expanded = true;
            }
        }
        return expanded;
    }

    /**
     * Method creates direct flow between these two fields.
     * @param sourceField source
     * @param targetField target
     * @param gh graph helper
     */
    protected void createDirectFlow(Field sourceField, Field targetField, StreamSetsGraphHelper gh) {
        Node sourceNode = gh.getNode(sourceField);
        Node targetNode = gh.getNode(targetField);
        if (sourceNode != null && targetNode != null) {
            gh.addDirectFlow(sourceNode, targetNode);
        }
    }

    /**
     * Method creates filter flow between these two fields.
     * @param sourceField source
     * @param targetField target
     * @param gh graph helper
     */
    private void createFilterFlow(Field sourceField, Field targetField, StreamSetsGraphHelper gh) {
        Node sourceNode = gh.getNode(sourceField);
        Node targetNode = gh.getNode(targetField);
        if (sourceNode != null && targetNode != null) {
            gh.addFilterFlow(sourceNode, targetNode);
        }
    }

    /**
     * Method recursively creates inner flows between these two fields.
     * @param sourceField source
     * @param targetField target
     * @param flowType flow type
     * @param gh graph helper
     */
    private void createOtherFlows(Field sourceField, Field targetField, Edge.Type flowType, StreamSetsGraphHelper gh) {
        if (!sourceField.hasChildren() && !targetField.hasChildren()) {
            if (flowType == Edge.Type.DIRECT) {
                createDirectFlow(sourceField, targetField, gh);
            } else {
                createFilterFlow(sourceField, targetField, gh);
            }
        } else if (sourceField.hasChildren() && targetField.hasChildren()) {
            for (Map.Entry<String, Field> sourceChildEntry : sourceField.getChildren().entrySet()) {
                Field targetChild = targetField.getChild(sourceChildEntry.getKey());
                if (targetChild != null) {
                    createOtherFlows(sourceChildEntry.getValue(), targetChild, flowType, gh);
                }
            }
        } else if (sourceField.hasChildren()) {
            for (Field child : sourceField.getChildren().values()) {
                createOtherFlows(child, targetField, flowType, gh);
            }
        } else {
            LOGGER.debug("Inner flows: Stage is not fully merged. Stage name: {} ", getClass().getSimpleName());
        }
    }

    /**
     * Method replaces Runtime Value with its own value without removing other EL
     * @param value JSP 2.0 Expression Language as string
     * @param ctx context
     * @return replaced value
     */
    protected String replaceRuntimeValueWithoutRemovingEL(String value, PipelineContext ctx) {
        return replaceRuntimeValue(value, ctx, false);
    }

    /**
     * Method replaces Runtime Value with its own value and removes other EL
     * @param value JSP 2.0 Expression Language as string
     * @param ctx context
     * @return replaced value with removed EL
     */
    protected String replaceRuntimeValueAndRemoveEL(String value, PipelineContext ctx) {
        return replaceRuntimeValue(value, ctx, true);
    }

    private String replaceRuntimeValue(String value, PipelineContext ctx, boolean remove) {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        ExpressionLanguageHelper elHelper = getParserService().getExpressionLanguageHelper();
        String replacement = elHelper
                .replaceRuntimeValues(value, ctx.getPipeline().getPipelineConfig().getConfiguration().getParameters(),
                        remove);
        return replacement.isEmpty() ? value : replacement;
    }

    /**
     * Method defines column name corresponding to field's path. Field path can contains single or double quotes.
     * These quotes are used for StreamSets' purposes, so if this situation occurs, quotes need to be removed.
     * @param fieldPath field's path
     * @return column's name without single / double quotes
     */
    protected String defineColumnNameByFieldPath(String fieldPath) {
        String columnName = fieldPath.startsWith("/") ? fieldPath.substring(1) : fieldPath;
        // remove single or double quotes
        if ((columnName.startsWith("'") && columnName.endsWith("'")) || (columnName.startsWith("\"") && columnName
                .endsWith("\""))) {
            columnName = columnName.substring(1, columnName.length() - 1);
        }
        return columnName;
    }

    protected DataflowQueryService getQueryService() {
        return queryService;
    }

    public void setQueryService(DataflowQueryService queryService) {
        this.queryService = queryService;
    }

    protected NodeCreator getNodeCreator() {
        return nodeCreator;
    }

    public void setNodeCreator(NodeCreator nodeCreator) {
        this.nodeCreator = nodeCreator;
    }

    protected FieldParserService getParserService() {
        return parserService;
    }

    public void setParserService(FieldParserService parserService) {
        this.parserService = parserService;
    }
}
