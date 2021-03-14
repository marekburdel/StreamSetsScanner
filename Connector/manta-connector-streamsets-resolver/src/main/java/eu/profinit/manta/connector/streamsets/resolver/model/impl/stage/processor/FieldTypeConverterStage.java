package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.processor;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageUIInfo;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IWholeTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EConvertBy;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.IFieldTypeConverterStage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.Stage;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.FieldTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component.WholeTypeConverterComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldTypeConverterStage extends Stage implements IFieldTypeConverterStage {

    private EConvertBy convertBy;
    private List<FieldTypeConverterComponent> fieldTypeConverters;
    private List<WholeTypeConverterComponent> wholeTypeConverters;

    public FieldTypeConverterStage(String instanceName, String stageName, IStageUIInfo stageUIInfo,
            List<String> inputLanes, List<String> outputLanes, List<String> eventLanes, EConvertBy convertBy,
            List<FieldTypeConverterComponent> fieldTypeConverters,
            List<WholeTypeConverterComponent> wholeTypeConverters) {
        super(instanceName, stageName, stageUIInfo, inputLanes, outputLanes, eventLanes);
        this.convertBy = convertBy;
        this.fieldTypeConverters = fieldTypeConverters;
        this.wholeTypeConverters = wholeTypeConverters;
    }

    @Override public EConvertBy getConvertBy() {
        return convertBy;
    }

    @Override public List<? extends IFieldTypeConverterComponent> getFieldTypeConverters() {
        return fieldTypeConverters;
    }

    @Override public List<? extends IWholeTypeConverterComponent> getWholeTypeConverters() {
        return wholeTypeConverters;
    }
}
