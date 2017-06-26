package com.casa.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

    }


    //si se selecciona el boton back, se cierra la actividad actual
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
        /*if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);*/
    }
}
