package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IDataParserStage;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.AbstractStageAnalyzer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.StageAnalyzerSetting;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldFlow;
import eu.profinit.manta.dataflow.generator.streamsets.helper.PipelineContext;
import eu.profinit.manta.dataflow.model.Edge;

/**
 *
 * @author mburdel
 */
public class DataParserAnalyzer extends AbstractStageAnalyzer<IDataParserStage> {

    @Override public void analyzeFields(IDataParserStage stage, PipelineContext ctx) {
        super.analyzeFields(stage, ctx);

        StageAnalyzerSetting setting = ctx.getSetting(stage);

        Field rootField = setting.getInputRootField();
        Field fieldToParse = rootField.add(stage.getFieldPathToParse(), EFieldType.IO);
        Field parsedField = rootField.add(stage.getParsedFieldPath(), EFieldType.BEGIN);

        setting.addFieldFlow(new FieldFlow(fieldToParse, parsedField, Edge.Type.DIRECT));
    }

}
