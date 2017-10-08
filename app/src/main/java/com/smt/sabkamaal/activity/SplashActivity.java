package com.smt.sabkamaal.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.smt.sabkamaal.R;


/**
 * Created by abhisheik on 24/2/17.
 */
public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    SharedPreferences sharedPreferences;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_activity);

        sharedPreferences = this.getSharedPreferences("login_status", Context.MODE_PRIVATE);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(sharedPreferences.getString("status","").equals("1")){
                    Intent intent = new Intent(SplashActivity.this, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
