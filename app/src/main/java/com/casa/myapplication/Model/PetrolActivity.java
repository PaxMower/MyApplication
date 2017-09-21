package com.casa.myapplication.Model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.casa.myapplication.R;

/**
 * Created by Gastby on 21/09/2017.
 */

public class PetrolActivity extends AppCompatActivity{

    private EditText mPetrolDate, mPetrolHor, mPetrolKm, mPetrolLiters;
    private Button bSendPetrol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol);

        mPetrolDate = (EditText) findViewById(R.id.petrol_date);
        mPetrolHor = (EditText) findViewById(R.id.petrol_hour);
        mPetrolKm = (EditText) findViewById(R.id.petrol_km);
        mPetrolLiters = (EditText) findViewById(R.id.petrol_liters);
        bSendPetrol = (Button) findViewById(R.id.petrol_send);

        formData();

    }


    public void formData(){
        bSendPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
