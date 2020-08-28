package si.rozna.ping.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import si.rozna.ping.models.api.PingApiModel;
import si.rozna.ping.models.api.PongApiModel;

public interface PingApi {

    @POST("ping")
    Call<Void> pingGroup(@Body PingApiModel ping);

    @POST("ping/response")
    Call<Void> pong(@Body PongApiModel pong);



}
