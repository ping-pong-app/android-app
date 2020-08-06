package si.rozna.ping.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import si.rozna.ping.models.dto.GroupDTO;
import si.rozna.ping.models.dto.InviteDTO;

public interface RestAPI {

    /* Groups API */

    @GET("groups")
    Call<List<GroupDTO>> getAllGroups(@Header("Authorization") String token);

    @GET("groups/{id}")
    Call<GroupDTO> getGroupById(@Header("Authorization") String token, @Path("id") String id);

    @POST("groups")
    Call<GroupDTO> createGroup(@Header("Authorization") String token, @Body GroupDTO groupDTO);

    @DELETE("groups/{id}")
    Call<GroupDTO> deleteGroup(@Header("Authorization") String token, @Path("id") String id);

    /* Invites API */

//    @POST
//    Call<InviteDTO> inviteUser();

}
