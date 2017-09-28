package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.casa.myapplication.Logic.Order;
import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mDate, mTruckID, mTruckNumber, mPlatformNumber, mAddress, mPhone, mTextArea, mApertureHour;
    private EditText mContainerChargeDay, mContainerChargeHour, mClient, mCharger, mDestiny, mContainerNumber, mArrivalHour, mDepartureHour, mContainerDischargeHour, mContainerDischargeDay;
    private Button bSend, bMap, bTime, bChargeDay, bChargeHour, bDischargeDay, bDischargeHour, bPetrol;
    private String TAG = "";
    private Boolean bucket = false; // false = empty; true = data inside
    private Calendar mCalendarPicker = Calendar.getInstance();
    //private Calendar mCalendarDischarge = Calendar.getInstance();
    private Calendar mTimePicker = Calendar.getInstance();
    //private Calendar mTimeDischarge = Calendar.getInstance();

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
        mAddress = (EditText) findViewById(R.id.address);
        mPhone = (EditText) findViewById(R.id.phone);
        mDate = (EditText) findViewById(R.id.date);
        mTruckID = (EditText) findViewById(R.id.truck_num);
        mTruckNumber = (EditText) findViewById(R.id.truck_id);
        mPlatformNumber = (EditText) findViewById(R.id.platform_id);
        mContainerChargeDay = (EditText) findViewById(R.id.button_obtain_charge_day);
        mContainerChargeHour = (EditText) findViewById(R.id.button_obtain_charge_hour);
        mClient = (EditText) findViewById(R.id.client);
        mApertureHour = (EditText) findViewById(R.id.aperture_hour);
        mCharger = (EditText) findViewById(R.id.charger);
        mContainerNumber = (EditText) findViewById(R.id.container_number);
        //mArrivalHour = (EditText) findViewById(R.id.arrival_time);
        //mDepartureHour = (EditText) findViewById(R.id.departure_time);
        mContainerDischargeHour = (EditText) findViewById(R.id.button_obtain_discharge_hour);
        mContainerDischargeDay = (EditText) findViewById(R.id.button_obtain_discharge_day);
        bMap = (Button) findViewById(R.id.view_map);
        bSend = (Button) findViewById(R.id.save_order);
        bPetrol = (Button) findViewById(R.id.petrol);
        mTextArea = (EditText) findViewById(R.id.text_area);
        //bTime = (Button) findViewById(R.id.button_obtain_date);
        //bChargeDay = (Button) findViewById(R.id.button_obtain_charge_day);
        //bChargeHour = (Button) findViewById(R.id.button_obtain_charge_hour);
        mArrivalHour = (EditText) findViewById(R.id.button_obtain_arrival_time);
        mDepartureHour = (EditText) findViewById(R.id.button_obtain_departure_time);
        //bDischargeDay = (Button) findViewById(R.id.button_obtain_discharge_day);
        //bDischargeHour = (Button) findViewById(R.id.button_obtain_discharge_hour);



        saveSharedPreferences();
        formData();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void formData(){

        //put the current date on the form
        mDate.setText(getDate());

        //open map with the position
        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderActivity.this, MapsActivity.class));
            }});

        //add refuel option
        bPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderActivity.this, PetrolActivity.class));
            }
        });

        //Put the hour which the company charged you
        mContainerChargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerChargeHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mContainerChargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mContainerChargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

            }
        });

        //Put the hour which the company discharged you
        mContainerDischargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerDischargeHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mContainerDischargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mContainerDischargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

            }
        });

        //add the hour when you arrive to the company
        mArrivalHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mArrivalHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mArrivalHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mArrivalHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

            }
        });
        //add the hour when you leave to the company
        mDepartureHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mDepartureHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mDepartureHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mDepartureHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

            }
        });

        //add the day when the container is charged
        mContainerChargeDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerChargeDay.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            mContainerChargeDay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                                        }
                                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                                    datePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mContainerChargeDay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

                }

            }
        });

        //add the day when the container is discharged
        mContainerDischargeDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerDischargeDay.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            mContainerDischargeDay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                                        }
                                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                                    datePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mContainerDischargeDay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

                }

            }
        });

        ////add the hour when the container is charged
        mContainerChargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerChargeHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mContainerChargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mContainerChargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

            }
        });

        //add the day when the container is discharged
        mContainerDischargeHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(!mContainerDischargeHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mContainerDischargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                                        }
                                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                                    timePickerDialog.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();

                }else {

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mContainerDischargeHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }

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

                //HashMap<String, String> dataMap = new HashMap<String, String>();

               // dataMap.put("driver",mDriver.getText().toString());
                //dataMap.put("truck ID",mTruckID.getText().toString());

                Order newOrder = new Order();

                newOrder.setDate(mDate.getText().toString());
                newOrder.setDriver(mDriver.getText().toString());
                newOrder.setTruckNumber(mTruckNumber.getText().toString());
                newOrder.setTruckID(mTruckID.getText().toString());
                newOrder.setPlatformNumber(mPlatformNumber.getText().toString());


                //   mDatabase.child("Users1").child("Orders1").child(orderDate).push().setValue(newOrder)
                mDatabase.child(orderDate).push().setValue(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        //Calendar calendar = Calendar.getInstance();
        DecimalFormat formatTime = new DecimalFormat("00");
        //DecimalFormat minuteFormat = new DecimalFormat("00");

        String hour = formatTime.format(mTimePicker.get(Calendar.HOUR_OF_DAY));
        String minute = formatTime.format(mTimePicker.get(Calendar.MINUTE));

        String date = hour+":"+minute;

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
