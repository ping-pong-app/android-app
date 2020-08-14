package si.rozna.ping.rest;

import retrofit2.Call;
import retrofit2.http.POST;
import si.rozna.ping.models.Ping;

public interface PingApi {

    @POST("ping")
    Call<Void> pingGroup(Ping ping);

}
