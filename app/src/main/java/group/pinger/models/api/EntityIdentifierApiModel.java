package group.pinger.models.api;

import androidx.annotation.NonNull;

public class EntityIdentifierApiModel extends ApiModel {

    @NonNull
    @Override
    public String toString() {
        return String.format("ID: %s", this.id);
    }

}
