package com.casa.myapplication.Model;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mDate, mTruckID, mTruckNumber, mPlatformNumber;
    private EditText mContainerChargeDay, mContainerChargeHour, mClient, mCharger, mDestiny, mContainerNumber, mArrivalHour, mDepartureHour;
    private RadioButton mSimple, mMultimple, mTransfers;
    private Button bSend, bMap, bTime, bChargeDay, bChargeHour, bArrivalHour, bDepartureHour, bDischargeDay, bDischargeHour;
    private String TAG = "";

    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mDriver = (EditText) findViewById(R.id.driver);
        mDate = (EditText) findViewById(R.id.date);
        mTruckID = (EditText) findViewById(R.id.truck_num);
        mTruckNumber = (EditText) findViewById(R.id.truck_id);
        mPlatformNumber = (EditText) findViewById(R.id.platform_id);
        mContainerChargeDay = (EditText) findViewById(R.id.charging_order_day);
        mContainerChargeHour = (EditText) findViewById(R.id.charging_order_hour);
        mClient = (EditText) findViewById(R.id.client);
        mCharger = (EditText) findViewById(R.id.charger);
        mDestiny = (EditText) findViewById(R.id.destiny);
        mContainerNumber = (EditText) findViewById(R.id.container_number);
        mArrivalHour = (EditText) findViewById(R.id.arrival_time);
        mDepartureHour = (EditText) findViewById(R.id.departure_time);
        mSimple = (RadioButton) findViewById(R.id.simpleOrder);
        mMultimple = (RadioButton) findViewById(R.id.multiple_order);
        mTransfers = (RadioButton) findViewById(R.id.transfers);
        bMap = (Button) findViewById(R.id.view_map);
        bSend = (Button) findViewById(R.id.save_order);
        bTime = (Button) findViewById(R.id.button_obtain_date);
        bChargeDay = (Button) findViewById(R.id.button_obtain_charge_day);
        bChargeHour = (Button) findViewById(R.id.button_obtain_charge_hour);
        bArrivalHour = (Button) findViewById(R.id.button_obtain_arrival_time);
        bDepartureHour = (Button) findViewById(R.id.button_obtain_charge_hour);
        bDischargeDay = (Button) findViewById(R.id.button_obtain_discharge_hour);
        bDischargeHour = (Button) findViewById(R.id.button_obtain_discharge_hour);

        formButtons();
        SendFirebaseData();

    }

    public void formButtons(){
        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderActivity.this, MapsActivity.class));
            }});

        bTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //mDate.
            }
        });

        bChargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mContainerChargeHour.setText(getHour());
            }
        });

        bArrivalHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mArrivalHour.setText(getHour());
            }
        });

        bDepartureHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mDepartureHour.setText(getHour());
            }
        });



    }


    //En este método irán las llamadas a las BBDD
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SendFirebaseData(){

        final String orderDate = getDate();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        bSend.setOnClickListener(new View.OnClickListener() { //listener que se ejecuta cuando se pulsa el botón
            @Override
            public void onClick(View v) {

                HashMap<String, String> dataMap = new HashMap<String, String>();

                dataMap.put("driver",mDriver.getText().toString());
                dataMap.put("truck ID",mTruckID.getText().toString());

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


    //Close actual activity when back button is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getHour(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        DecimalFormat hourFormat = new DecimalFormat("00");
        DecimalFormat minuteFormat = new DecimalFormat("00");
        DecimalFormat secondFormat = new DecimalFormat("00");

        String hour = hourFormat.format(Calendar.HOUR_OF_DAY);
        String minute = hourFormat.format(Calendar.MINUTE);
        String second = hourFormat.format(Calendar.SECOND);

        String date = hour+":"+minute+":"+second;

        return date;
    }



    /*
    *
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int date = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    *
    *
    *
    * */


}
