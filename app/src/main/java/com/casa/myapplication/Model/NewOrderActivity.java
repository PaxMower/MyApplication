package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

/**
 * Created by Gastby on 21/06/2017.
 */

public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mDate, mTruckID, mTruckNumber, mPlatformID, mAddress, mPhone, mTextArea, mApertureHour;
    private EditText mContainerChargeDay, mContainerChargeHour, mClient, mCharger, mDestiny, mContainerNumber, mArrivalHour, mDepartureHour, mContainerDischargeHour, mContainerDischargeDay;
    private Button bSend, bMap, bTime, bChargeDay, bChargeHour, bDischargeDay, bDischargeHour, bPetrol;
    private String TAG = "";
    private boolean isDated = false;
    private Calendar mCalendarPicker = Calendar.getInstance();
    private Calendar mTimePicker = Calendar.getInstance();
    private ProgressDialog mProgressLoad;

    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        mProgressLoad = new ProgressDialog(NewOrderActivity.this);
        mProgressLoad.setTitle("Guardando");
        mProgressLoad.setMessage("Guardando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mDriver = (EditText) findViewById(R.id.driver);
        mAddress = (EditText) findViewById(R.id.address);
        mPhone = (EditText) findViewById(R.id.phone);
        mDate = (EditText) findViewById(R.id.date);
        mTruckID = (EditText) findViewById(R.id.truck_num);
        mTruckNumber = (EditText) findViewById(R.id.truck_id);
        mPlatformID = (EditText) findViewById(R.id.platform_id);
        mContainerChargeDay = (EditText) findViewById(R.id.button_obtain_charge_day);
        mContainerChargeHour = (EditText) findViewById(R.id.button_obtain_charge_hour);
        mClient = (EditText) findViewById(R.id.client);
        mApertureHour = (EditText) findViewById(R.id.aperture_hour);
        mCharger = (EditText) findViewById(R.id.charger);
        mContainerNumber = (EditText) findViewById(R.id.container_number);
        mContainerDischargeHour = (EditText) findViewById(R.id.button_obtain_discharge_hour);
        mContainerDischargeDay = (EditText) findViewById(R.id.button_obtain_discharge_day);
        bMap = (Button) findViewById(R.id.view_map);
        bSend = (Button) findViewById(R.id.save_order);
        bPetrol = (Button) findViewById(R.id.petrol);
        mTextArea = (EditText) findViewById(R.id.text_area);
        mArrivalHour = (EditText) findViewById(R.id.button_obtain_arrival_time);
        mDepartureHour = (EditText) findViewById(R.id.button_obtain_departure_time);

        loadSharedPreferences();
        formData();
        SendFirebaseData();

    }

    //Method for save data on device memory
    public void saveSharedPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("mDate", mDate.getText().toString());
        editor.putString("mDriver", mDriver.getText().toString());
        editor.commit();
    }

    //Method for load data on device memory
    public void loadSharedPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);

        String date = sharedPref.getString("mDate", "");
        mDate.setText(date);
        String driver = sharedPref.getString("mDriver", "");
        mDriver.setText(driver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void formData(){

        //put the current date on the form
        mDate.setText(getDay() + "/" + getMonth() + "/" + getYear());

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


    //Método para guardar la orden en firebase
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SendFirebaseData(){


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Orders");

        bSend.setOnClickListener(new View.OnClickListener() { //listener que se ejecuta cuando se pulsa el botón
            @Override
            public void onClick(View v) {

                Order newOrder = new Order();

                newOrder.setDay(getDay());
                newOrder.setMonth(getMonth());
                newOrder.setYear(getYear());
                newOrder.setHour(getHour());
                newOrder.setMinutes(getMinutes());

                newOrder.setDate(mDate.getText().toString());
                newOrder.setDriver(mDriver.getText().toString());
                newOrder.setTruckNumber(mTruckNumber.getText().toString());
                newOrder.setTruckID(mTruckID.getText().toString());
                newOrder.setPlatformID(mPlatformID.getText().toString());

                newOrder.setClient(mClient.getText().toString());
                newOrder.setCharger(mCharger.getText().toString());
                newOrder.setAddress(mAddress.getText().toString());
                newOrder.setApertureHour(mApertureHour.getText().toString());
                newOrder.setPhone(mPhone.getText().toString());
                newOrder.setContainerNumber(mContainerNumber.getText().toString());
                newOrder.setArrivalHour(mArrivalHour.getText().toString());
                newOrder.setDepartureHour(mDepartureHour.getText().toString());

                newOrder.setContainerChargeDay(mContainerChargeDay.getText().toString());
                newOrder.setContainerChargeHour(mContainerChargeHour.getText().toString());
                newOrder.setContainerDischargeDay(mContainerDischargeDay.getText().toString());
                newOrder.setContainerDischargeHour(mContainerDischargeHour.getText().toString());

                newOrder.setTextArea(mTextArea.getText().toString());

                mProgressLoad.show();

                mDatabase.push().setValue(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Intent goToMainPage = new Intent(NewOrderActivity.this, MenuActivity.class);
                            goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToMainPage);
                            mProgressLoad.dismiss();
                            finish();
                        } else {
                            mProgressLoad.hide();
                            Toast.makeText(NewOrderActivity.this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
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

    public String getDay(){
        Calendar c = Calendar.getInstance();
        DecimalFormat formatTime = new DecimalFormat("00");
        String date = formatTime.format(c.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public String getMonth(){
        Calendar c = Calendar.getInstance();
        DecimalFormat formatTime = new DecimalFormat("00");
        String date = formatTime.format(c.get(Calendar.MONTH)+1);
        return date;
    }

    public String getYear(){
        Calendar c = Calendar.getInstance();
        DecimalFormat formatTime = new DecimalFormat("00");
        String date = formatTime.format(c.get(Calendar.YEAR));
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getHour(){
        DecimalFormat formatTime = new DecimalFormat("00");
        String hour = formatTime.format(mTimePicker.get(Calendar.HOUR_OF_DAY));

        return hour;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getMinutes(){
        DecimalFormat formatTime = new DecimalFormat("00");
        String minute = formatTime.format(mTimePicker.get(Calendar.MINUTE));

        return minute;
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        saveSharedPreferences();
    }

}