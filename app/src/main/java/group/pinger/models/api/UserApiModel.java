package group.pinger.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserApiModel extends ApiModel {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
