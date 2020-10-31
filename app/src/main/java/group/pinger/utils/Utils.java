package group.pinger.utils;

import retrofit2.Response;
import timber.log.Timber;

public class Utils {

    public static void logResponse(Response response){
        if(response == null)
            return;

        Timber.i("Code: %s", response.code());
        Timber.i("isSuccessful: %s", response.isSuccessful());
        Timber.i("Message: %s", response.message());
        Timber.i("Body: %s", response.body());

    }

}
