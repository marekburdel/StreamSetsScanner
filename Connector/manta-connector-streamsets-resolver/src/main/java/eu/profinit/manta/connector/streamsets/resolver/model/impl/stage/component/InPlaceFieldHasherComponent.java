package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IInPlaceFieldHasherComponent;
import eu.profinit.manta.connector.streamsets.model.model.stage.processor.EHashType;

import java.util.List;

/**
 * @author mburdel
 */
public class InPlaceFieldHasherComponent implements IInPlaceFieldHasherComponent {

    private List<String> sourceFieldsToHash;
    private EHashType hashType;

    public InPlaceFieldHasherComponent(List<String> sourceFieldsToHash, EHashType hashType) {
        this.sourceFieldsToHash = sourceFieldsToHash;
        this.hashType = hashType;
    }

    @Override public List<String> getSourceFieldsToHash() {
        return sourceFieldsToHash;
    }

    @Override public EHashType getHashType() {
        return hashType;
    }
}
