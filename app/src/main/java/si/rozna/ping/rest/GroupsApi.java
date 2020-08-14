package si.rozna.ping.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import si.rozna.ping.models.EntityIdentifier;
import si.rozna.ping.models.Group;

public interface GroupsApi {

    @GET("groups")
    Call<List<Group>> getAllGroups();

    @GET("groups/{id}")
    Call<Group> getGroupById(@Path("id") String id);

    @POST("groups")
    Call<EntityIdentifier> createGroup(@Body Group group);

    @DELETE("groups/{id}")
    Call<Void> deleteGroup(@Path("id") String id);

}
