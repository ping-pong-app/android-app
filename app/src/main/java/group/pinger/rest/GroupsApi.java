package group.pinger.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import group.pinger.models.api.EntityIdentifierApiModel;
import group.pinger.models.api.GroupApiModel;

public interface GroupsApi {

    @GET("groups")
    Call<List<GroupApiModel>> getAllGroups();

    @GET("groups/{id}")
    Call<GroupApiModel> getGroupById(@Path("id") String id);

    @POST("groups")
    Call<EntityIdentifierApiModel> createGroup(@Body GroupApiModel group);

    @DELETE("groups/{id}")
    Call<Void> deleteGroup(@Path("id") String id);

    @DELETE("groups/{id}/leave")
    Call<Void> leaveGroup(@Path("id") String id);

}
