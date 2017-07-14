package com.casa.myapplication.Model;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mTruckID, mOrder, mDay, mMonth, mYear;
    private Button mSend;
    private String TAG = "";

    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //*****AÑADIR BOTÓN BACK AL TOOLBAR*****//
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mDriver = (EditText) findViewById(R.id.driver);
        mTruckID = (EditText) findViewById(R.id.truck_id);
        mOrder = (EditText) findViewById(R.id.order_id);
        mDay = (EditText) findViewById(R.id.day);
        mMonth = (EditText) findViewById(R.id.month);
        mYear = (EditText) findViewById(R.id.year);
        mSend = (Button) findViewById(R.id.sendOrder);

        RetrieveFirebaseData();

    }

    //En este método irán las llamadas a las BBDD
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void RetrieveFirebaseData(){

        final String orderDate = getCalendarDateTime();



        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSend.setOnClickListener(new View.OnClickListener() { //listener que se ejecuta cuando se pulsa el botón
            @Override
            public void onClick(View v) {

                HashMap<String, String> dataMap = new HashMap<String, String>();

                dataMap.put("driver",mDriver.getText().toString());
                dataMap.put("truck ID",mTruckID.getText().toString());
                dataMap.put("Order",mOrder.getText().toString());
                dataMap.put("Day",mDay.getText().toString());
                dataMap.put("Month",mMonth.getText().toString());
                dataMap.put("Year",mYear.getText().toString());

                mDatabase.child("pepe@pepe").child("Orders").child(orderDate).push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewOrderActivity.this, "Datos enviados",Toast.LENGTH_LONG ).show();
                        }else{
                            Toast.makeText(NewOrderActivity.this, "Error",Toast.LENGTH_LONG ).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCalendarDateTime(){
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());

        System.out.println("**************************************"+formattedDate.toString());
        return formattedDate;
    }



}
