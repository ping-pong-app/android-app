package si.rozna.ping.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.models.api.PongApiModel;
import si.rozna.ping.rest.PingApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.utils.Utils;
import timber.log.Timber;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO: If user has no internet connection PONG will never be sent! -> Prevent PONG without internet connection

        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if(action == null || extras == null)
            return;

        String pingId = extras.getString(Constants.EXTRAS_PING_ID);
        String groupId = extras.getString(Constants.EXTRAS_GROUP_ID);

        switch (action) {
            case Constants.ACTION_PONG:
                FcmService.subscribeToPong(groupId);
                pong(pingId, "PONG");
                break;
            case Constants.ACTION_REJECT:
                FcmService.unsubscribeFromPong(groupId);
                pong(pingId, "REJECT");
                Toast.makeText(context, "You have REJECTED ping request! Shame on you!",
                        Toast.LENGTH_LONG).show();
                break;


        }

        // Cancel notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.cancel((int) extras.get(Constants.EXTRAS_NOTIFICATION_ID));
        }

    }

    private void pong(String pingId, String response){

        // TODO: More user friendly solution please..
        if(pingId == null)
            return;

        PongApiModel pongApiModel = new PongApiModel();
        pongApiModel.setPingId(pingId);
        pongApiModel.setResponse(response);

        PingApi pingApi = ServiceGenerator.createService(PingApi.class);
        pingApi.pong(pongApiModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()) {
                    Timber.e("PONG Request succeeded");
                } else {
                    Timber.e("PONG Request failed");
                }
                Utils.logResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e(t);
            }
        });
    }
}
