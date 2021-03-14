package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.ITargetFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;

import java.util.List;

/**
 * @author mburdel
 */
public class TargetFieldHasherComponent implements ITargetFieldHasherComponent {

    private List<String> sourceFieldsToHash;
    private EHashType hashType;
    private String targetField;
    private String headerAttribute;

    public TargetFieldHasherComponent(List<String> sourceFieldsToHash, EHashType hashType, String targetField,
            String headerAttribute) {
        this.sourceFieldsToHash = sourceFieldsToHash;
        this.hashType = hashType;
        this.targetField = targetField;
        this.headerAttribute = headerAttribute;
    }

    @Override public List<String> getSourceFieldsToHash() {
        return sourceFieldsToHash;
    }

    @Override public EHashType getHashType() {
        return hashType;
    }

    @Override public String getTargetField() {
        return targetField;
    }

    @Override public String getHeaderAttribute() {
        return headerAttribute;
    }
}
