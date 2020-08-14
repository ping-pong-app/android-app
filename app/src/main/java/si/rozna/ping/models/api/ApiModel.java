package si.rozna.ping.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class ApiModel implements Comparable<ApiModel> {

    @SerializedName("id")
    @Expose
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(ApiModel apiModel) {
        return this.id.compareTo(apiModel.id);
    }
}
