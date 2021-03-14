package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

/**
 *
 * @author mburdel
 */
public interface IColumnNameComponent extends IStageComponent {

    String getParamValue();

    String getDataType();

    String getColumnName();

    String getFieldName();

    String getDefaultValue();

}
