package com.waheed.reypostre.recipes.projects.android.reypostre;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by waheed on 11/17/2017.
 */

public class IntroActivity extends AppCompatActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(IntroActivity.this,WelcomeActivity.class);
                IntroActivity.this.startActivity(mainIntent);
                IntroActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
