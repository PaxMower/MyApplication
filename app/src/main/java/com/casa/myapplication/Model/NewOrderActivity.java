package com.casa.myapplication.Model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mTruckID, mOrder, mDay, mMonth, mYear;
    private Button mSend;
    private String TAG = "";

    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference("Viajes");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mDriver = (EditText) findViewById(R.id.driver);
        mTruckID = (EditText) findViewById(R.id.truck_id);
        mOrder = (EditText) findViewById(R.id.order_id);
        mDay = (EditText) findViewById(R.id.day);
        mMonth = (EditText) findViewById(R.id.month);
        mYear = (EditText) findViewById(R.id.year);
        mSend = (Button) findViewById(R.id.sendOrder);

        firebaseData();

    }

    //En este método irán las llamadas a las BBDD
    public void firebaseData(){

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Viajes");

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> dataMap = new HashMap<String, String>();

                dataMap.put("Chofer", mDriver.getText().toString());
                dataMap.put("Matrícula camión", mTruckID.getText().toString());
                dataMap.put("Orden", mOrder.getText().toString());
                dataMap.put("Día", mDay.getText().toString());
                dataMap.put("Mes", mMonth.getText().toString());
                dataMap.put("Año", mYear.getText().toString());

                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewOrderActivity.this, "Datos enviados",Toast.LENGTH_LONG ).show();
                        }else{
                            Toast.makeText(NewOrderActivity.this, "Error al enviar los datos...",Toast.LENGTH_LONG ).show();
                        }
                    }
                });

            }
        });


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
