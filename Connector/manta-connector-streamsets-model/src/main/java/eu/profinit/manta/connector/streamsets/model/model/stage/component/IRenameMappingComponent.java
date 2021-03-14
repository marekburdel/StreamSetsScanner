package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 * @author mburdel
 */
public interface IRenameMappingComponent extends IStageComponent {

    /**
     *
     * @return existing source field(s) (regex) to rename
     */
    String getFromFieldExpression();

    /**
     *
     * @return new name for the field (regex)
     */
    String getToFieldExpression();
}
