package com.vector.com.card.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vector.com.card.R;

public class PlayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_play);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
