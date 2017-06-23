package com.casa.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        mDriver = (EditText) findViewById(R.id.driver_input);


    }



}
