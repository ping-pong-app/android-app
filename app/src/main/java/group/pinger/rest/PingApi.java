package group.pinger.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import group.pinger.models.api.PingApiModel;
import group.pinger.models.api.PongApiModel;

public interface PingApi {

    @POST("ping")
    Call<Void> pingGroup(@Body PingApiModel ping);

    @POST("ping/response")
    Call<Void> pong(@Body PongApiModel pong);



}
