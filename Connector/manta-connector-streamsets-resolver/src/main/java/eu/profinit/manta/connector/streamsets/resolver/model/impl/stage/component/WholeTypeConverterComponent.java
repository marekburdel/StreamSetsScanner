package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IWholeTypeConverterComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EFieldTypeConverterDataType;

/**
 * @author mburdel
 */
public class WholeTypeConverterComponent implements IWholeTypeConverterComponent {

    private EFieldTypeConverterDataType sourceType;
    private EFieldTypeConverterDataType targetType;

    public WholeTypeConverterComponent(EFieldTypeConverterDataType sourceType, EFieldTypeConverterDataType targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override public EFieldTypeConverterDataType getSourceType() {
        return sourceType;
    }

    @Override public EFieldTypeConverterDataType getTargetType() {
        return targetType;
    }
}
