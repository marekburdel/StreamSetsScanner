package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.component.IRenameMappingComponent;

/**
 *
 * @author mburdel
 */
public class RenameMappingComponent implements IRenameMappingComponent {

    private String fromFieldExpression;
    private String toFieldExpression;

    public RenameMappingComponent(String fromFieldExpression, String toFieldExpression) {
        this.fromFieldExpression = fromFieldExpression;
        this.toFieldExpression = toFieldExpression;
    }

    @Override public String getFromFieldExpression() {
        return fromFieldExpression;
    }

    @Override public String getToFieldExpression() {
        return toFieldExpression;
    }
}
