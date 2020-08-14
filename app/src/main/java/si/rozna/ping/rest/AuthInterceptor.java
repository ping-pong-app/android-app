package si.rozna.ping.rest;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import si.rozna.ping.auth.FirebaseService;

import static si.rozna.ping.Constants.AUTHORIZATION_HEADER;
import static si.rozna.ping.Constants.BEARER_TOKEN_PREFIX;

public class AuthInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        try {
            FirebaseUser user = FirebaseService.getCurrentUser().orElseThrow(() -> new RuntimeException("User is not logged in!"));

            Task<GetTokenResult> tokenResultTask = user.getIdToken(true);
            GetTokenResult tokenResult = Tasks.await(tokenResultTask);
            String accessToken = tokenResult.getToken();

            if (accessToken == null) {
                throw new RuntimeException("Token is null!");
            }

            Request modifiedRequest = request.newBuilder()
                    .addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + accessToken)
                    .build();

            return chain.proceed(modifiedRequest);
        } catch (ExecutionException | InterruptedException exc) {
            throw new IOException(exc);
        }
    }
}
