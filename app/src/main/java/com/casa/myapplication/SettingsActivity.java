package com.casa.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Gastby on 21/06/2017.
 */

public class SettingsActivity extends AppCompatActivity{

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextView = (TextView) findViewById(R.id.textViewSettings);

    }
}
