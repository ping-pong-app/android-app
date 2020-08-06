package si.rozna.ping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import si.rozna.ping.R;
import si.rozna.ping.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_SCREEN_DURATION_IN_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, SPLASH_SCREEN_DURATION_IN_MILLIS);
    }
}