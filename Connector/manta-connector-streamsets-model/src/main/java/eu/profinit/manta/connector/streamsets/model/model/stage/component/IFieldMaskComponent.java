package eu.profinit.manta.connector.streamsets.model.model.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.EMaskType;
import eu.profinit.manta.connector.streamsets.model.model.stage.IStageComponent;

import java.util.List;

/**
 * Component of Field Masker Stage
 *
 * @author mburdel
 */
public interface IFieldMaskComponent extends IStageComponent {

    /**
     *
     * @return masked field(s)
     */
    List<String> getFields();

    /**
     *
     * @return mask type for selected field(s)
     */
    EMaskType getMaskType();

    /**
     *
     * @return regex used in regular expression(mask type - REGEX)
     */
    String getRegex();

    /**
     * Groups are delimited by regex.
     *
     * @return number of groups to be shown
     */
    String getGroupsToShow();

    /**
     *
     * @return custom mask
     */
    String getMask();
}
