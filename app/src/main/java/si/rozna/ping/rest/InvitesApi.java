package si.rozna.ping.rest;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import si.rozna.ping.models.api.EntityIdentifierApiModel;
import si.rozna.ping.models.api.GroupApiModel;
import si.rozna.ping.models.api.InviteApiModel;

public interface InvitesApi {

    @GET("invites")
    Call<List<InviteApiModel>> getUserInvites();

    @POST("invites")
    Call<EntityIdentifierApiModel> inviteUser(@Body InviteApiModel invite);

    @POST("invites/{id}/accept")
    Call<GroupApiModel> acceptInvitation(@Path("id") String id);

    @DELETE("invites/{id}/reject")
    Call<Void> rejectInvitation(@Path("id") String id);

    @DELETE("invites/{id}/cancel")
    Call<Void> cancelInvitation(@Path("id") String id);

}
