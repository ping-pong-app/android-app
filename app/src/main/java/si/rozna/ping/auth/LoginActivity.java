package si.rozna.ping.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

import si.rozna.ping.R;
import si.rozna.ping.ui.MainActivity;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    /* Static variables */
    private static final int RC_SIGN_IN = 420;
    private static final boolean IS_SMART_LOCK_ENABLED = false;

    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Authentication providers (Google, Facebook, Twitter,...)
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.GitHubBuilder().build()
        );

        // Create sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(IS_SMART_LOCK_ENABLED)
                        .setLogo(R.drawable.ic_baseline_person_pin_192)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        AuthService.getCurrentUser().ifPresent(user -> {
            logInSucceeded();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                // User logged in successfully
                logInSucceeded();
            } else {
                logInFailed(response);
            }
        }

    }

    private void logInSucceeded(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void logInFailed(IdpResponse response){
        if (response == null) {
            // User canceled login procedure
            Timber.d("User canceled log in");
        } else {
            Timber.e("ErrorCode: " + response.getError().getErrorCode() +
                    "\nErrorMessage: " + response.getError().getMessage());

            Toast.makeText(this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
        }
    }

}