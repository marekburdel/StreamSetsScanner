package eu.profinit.manta.connector.streamsets.resolver.model.impl.stage.component;

import eu.profinit.manta.connector.streamsets.model.model.stage.EMaskType;
import eu.profinit.manta.connector.streamsets.model.model.stage.component.IFieldMaskComponent;

import java.util.List;

/**
 * @author mburdel
 */
public class FieldMaskComponent implements IFieldMaskComponent {

    private List<String> fields;
    private EMaskType maskType;
    private String regex;
    private String groupsToShow;
    private String mask;

    public FieldMaskComponent(List<String> fields, EMaskType maskType, String regex, String groupsToShow, String mask) {
        this.fields = fields;
        this.maskType = maskType;
        this.regex = regex;
        this.groupsToShow = groupsToShow;
        this.mask = mask;
    }

    @Override public List<String> getFields() {
        return fields;
    }

    @Override public EMaskType getMaskType() {
        return maskType;
    }

    @Override public String getRegex() {
        return regex;
    }

    @Override public String getGroupsToShow() {
        return groupsToShow;
    }

    @Override public String getMask() {
        return mask;
    }
}
