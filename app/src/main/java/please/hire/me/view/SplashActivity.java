package please.hire.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import please.hire.me.Util;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler delayHandler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {

                Intent intent;

                if (Util.isLogin()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                finish();
                startActivity(intent);
            }
        };
        delayHandler.postDelayed(r, 3000);
    }

}
