package com.vector.com.card.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vector.com.card.R;

import java.util.Random;
import java.util.TimerTask;

public class PlayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_play);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void startGame(View v) {
    }
}
