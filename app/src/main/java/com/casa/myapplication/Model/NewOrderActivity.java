package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
    private EditText mContainerChargeDay, mContainerChargeHour, mClient, mCharger, mDestiny, mContainerNumber, mArrivalHour, mDepartureHour, mContainerDischargeHour, mContainerDischargeDay;
    private RadioButton mSimple, mMultimple, mTransfers;
    private Button bSend, bMap, bTime, bChargeDay, bChargeHour, bArrivalHour, bDepartureHour, bDischargeDay, bDischargeHour;
    private String TAG = "";
    private Boolean bucket = false; // false = empty; true = data inside
    private Calendar mCurrentDate = Calendar.getInstance();

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
        mContainerDischargeHour = (EditText) findViewById(R.id.discharging_order_hour);
        mContainerDischargeDay = (EditText) findViewById(R.id.discharging_order_day);
        mSimple = (RadioButton) findViewById(R.id.simpleOrder);
        mMultimple = (RadioButton) findViewById(R.id.multiple_order);
        mTransfers = (RadioButton) findViewById(R.id.transfers);
        bMap = (Button) findViewById(R.id.view_map);
        bSend = (Button) findViewById(R.id.save_order);
        bTime = (Button) findViewById(R.id.button_obtain_date);
        bChargeDay = (Button) findViewById(R.id.button_obtain_charge_day);
        bChargeHour = (Button) findViewById(R.id.button_obtain_charge_hour);
        bArrivalHour = (Button) findViewById(R.id.button_obtain_arrival_time);
        bDepartureHour = (Button) findViewById(R.id.button_obtain_departure_time);
        bDischargeDay = (Button) findViewById(R.id.button_obtain_discharge_day);
        bDischargeHour = (Button) findViewById(R.id.button_obtain_discharge_hour);

        saveSharedPreferences();
        formButtons();
        SendFirebaseData();

    }

    //Method for save data on device memory
    public void saveSharedPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("mDriver", mDriver.getText().toString());
        editor.apply();
        bucket = true;

    }

    //Method for load data on device memory
    public void loadSharedPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);

        String driver = sharedPref.getString("mDriver", "");
        mDriver.setText(driver);
    }

    public void formButtons(){
        //open map with the position
        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderActivity.this, MapsActivity.class));
            }});

        //put the current date
        bTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mDate.setText(getDate());
            }
        });

        bChargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mContainerChargeHour.setText(getHour());
            }
        });

        bDischargeHour.setOnClickListener(new View.OnClickListener() {
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

        bChargeDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mContainerChargeDay.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },mCurrentDate.get(Calendar.YEAR),mCurrentDate.get(Calendar.MONTH)+1, mCurrentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        bDischargeDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mContainerChargeDay.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },mCurrentDate.get(Calendar.YEAR),mCurrentDate.get(Calendar.MONTH)+1, mCurrentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
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
                //dataMap.put("truck ID",mTruckID.getText().toString());

                mDatabase.child("pepe@pepe").child("Orders").child(orderDate).push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewOrderActivity.this, "Datos enviados",Toast.LENGTH_LONG ).show();

                            bucket = false; //put false for indicate that there are no data on device memory
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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

    @Override
    protected void onStart(){
        Log.d(TAG, "onStart");
        if (bucket){
            loadSharedPreferences();
        }
        super.onStart();

    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume");
        if (bucket){
            loadSharedPreferences();
        }
        super.onResume();

    }

    @Override
    protected void onPause(){
        Log.d(TAG, "onPause");
        saveSharedPreferences();
        super.onPause();

    }

    @Override
    protected void onStop(){
        Log.d(TAG, "onStop");
        saveSharedPreferences();
        super.onStop();

    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy");
        saveSharedPreferences();
        super.onDestroy();

    }


}
