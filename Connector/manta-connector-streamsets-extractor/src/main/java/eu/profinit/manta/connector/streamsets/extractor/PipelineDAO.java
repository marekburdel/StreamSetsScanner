package eu.profinit.manta.connector.streamsets.extractor;

import org.apache.commons.lang3.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for StreamSets Pipeline object including its ID, title, labels and name.
 *
 * @author mburdel
 */
public class PipelineDAO {
    private String pipelineId;
    private String title;
    private List<String> labels;
    private String name;

    private PipelineDAO(String pipelineId, String title, List<String> labels, String name) {
        Validate.notNull(pipelineId, "pipelineId can't be null");
        Validate.notNull(title, "title can't be null");
        Validate.notNull(labels, "labels can't be null");
        Validate.notNull(name, "name can't be null");

        this.pipelineId = pipelineId;
        this.title = title;
        this.labels = labels;
        this.name = name;
    }

    PipelineDAO(JSONObject value) {
        this((String)value.get("pipelineId"),(String)value.get("title"),
                new ArrayList<>(),(String)value.get("name"));
        processLabels((JSONObject)value.get("metadata"));
    }

    private void processLabels(JSONObject jsonObject) {
        JSONArray value = (JSONArray)jsonObject.get("labels");
        for (Object object : value) {
            labels.add((String)object);
        }
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getName() {
        return name;
    }
}
