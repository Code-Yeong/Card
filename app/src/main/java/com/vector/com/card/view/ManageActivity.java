package com.vector.com.card.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vector.com.card.R;
import com.vector.com.card.daoimpl.TempDataDaoImpl;
import com.vector.com.card.domian.TempData;
import com.vector.com.card.utils.Utils;

public class ManageActivity extends BaseActivity {

    private ImageView iv_vibrate, iv_audio;
    boolean isVibrateOn = false, isAudioOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_manage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_audio = (ImageView) findViewById(R.id.icon_switch_audio);
        iv_vibrate = (ImageView) findViewById(R.id.icon_switch_vibrate);

        iv_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAudioOn) {
                    isAudioOn = false;
                    iv_audio.setImageResource(R.drawable.icon_switch_left);
                } else {
                    isAudioOn = true;
                    iv_audio.setImageResource(R.drawable.icon_switch_right);
                }
                TempData tempData = Utils.readFromFile(ManageActivity.this);
                tempData.setAudio(isAudioOn ? "on" : "off");
                new TempDataDaoImpl().saveLoginInfo(ManageActivity.this, tempData);
            }
        });

        iv_vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVibrateOn) {
                    isVibrateOn = false;
                    iv_vibrate.setImageResource(R.drawable.icon_switch_left);
                } else {
                    isVibrateOn = true;
                    iv_vibrate.setImageResource(R.drawable.icon_switch_right);
                }
                TempData tempData = Utils.readFromFile(ManageActivity.this);
                tempData.setVibrate(isVibrateOn ? "on" : "off");
                new TempDataDaoImpl().saveLoginInfo(ManageActivity.this, tempData);
            }
        });

        init();
    }

    public void openHelp(View v) {

    }

    public void openVersion(View v) {
        startActivity(new Intent(ManageActivity.this, VersionActivity.class));
    }

    public void doReLogin(View v) {
        backToFirst();
    }


    public void doExit(View v) {
        removeAll();
    }

    public void init() {
        TempData tempData = Utils.readFromFile(this);
        String vibrate = tempData.getVibrate();
        String audio = tempData.getAudio();
        if (vibrate.equals("on")) {
            isVibrateOn = true;
            iv_vibrate.setImageResource(R.drawable.icon_switch_right);
        } else {
            isVibrateOn = false;
            iv_vibrate.setImageResource(R.drawable.icon_switch_left);
        }
        if (audio.equals("on")) {
            isAudioOn = true;
            iv_audio.setImageResource(R.drawable.icon_switch_right);
        } else {
            isAudioOn = false;
            iv_audio.setImageResource(R.drawable.icon_switch_left);
        }
    }
}