package si.rozna.ping.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.models.api.PongApiModel;
import si.rozna.ping.rest.PingApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.utils.Utils;
import timber.log.Timber;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(action == null)
            return;

        String pingId = (String) Objects.requireNonNull(intent.getExtras()).get(Constants.EXTRAS_PING_ID);
        Timber.i("Ping id: %s", pingId);

        switch (action) {
            case Constants.ACTION_PONG:
                String pongTopic = (String) Objects.requireNonNull(intent.getExtras()).get(Constants.EXTRAS_PONG_TOPIC);
                FcmService.subscribe(pongTopic);
                pong(pingId, "PONG");
                break;
            case Constants.ACTION_REJECT:
                pong(pingId, "REJECT");
                Toast.makeText(context, "You have REJECTED ping request! Shame on you!",
                        Toast.LENGTH_LONG).show();
                break;


        }

        Timber.i("MESSSSSAGEEEEEEEE ACTIOOOOOOON RECEIVED!!!!!!!!");

    }

    private void pong(String pingId, String response){

        Optional<String> userId = AuthService.getCurrentUserId();
        if(!userId.isPresent())
            return;

        PongApiModel pongApiModel = new PongApiModel();
        pongApiModel.setPingId(pingId);
        pongApiModel.setUserId(userId.get());
        pongApiModel.setResponse(response);

        PingApi pingApi = ServiceGenerator.createService(PingApi.class);
        pingApi.pong(pongApiModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()) {
                    Utils.logResponse(response);
                } else {
                    Utils.logResponse(response);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e(t);
            }
        });
    }
}
