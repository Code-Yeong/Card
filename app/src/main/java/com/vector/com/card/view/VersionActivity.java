package com.vector.com.card.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.utils.Utils;

public class VersionActivity extends BaseActivity {

    private TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_version);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_version=(TextView)findViewById(R.id.version_code);
        tv_version.setText("版本号:"+ Utils.getVersionName(this));
    }
}
