package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
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
import java.util.Date;


public class NewOrderActivity extends AppCompatActivity {

    private EditText mDriver, mDate, mTruckID, mTruckNumber, mPlatformID, mAddress, mPhone, mTextArea, mApertureHour;
    private EditText mContainerChargeDay, mContainerChargeHour, mClient, mCharger, mDestiny, mContainerNumber, mArrivalHour, mDepartureHour, mContainerDischargeHour, mContainerDischargeDay;
    private Button bSend, bMap, bTime, bChargeDay, bChargeHour, bDischargeDay, bDischargeHour, bPetrol;
    private String TAG = "";
    private boolean isDated = false;
    private Calendar mCalendarPicker = Calendar.getInstance();
    private Calendar mTimePicker = Calendar.getInstance();
    private ProgressDialog mProgressLoad;

    SharedPreferences sharedPref;

    //private User user = new User();

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
        mTruckID = (EditText) findViewById(R.id.truck_num_settings);
        mTruckNumber = (EditText) findViewById(R.id.truck_id_settings);
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

        sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("mDate", mDate.getText().toString());
        editor.putString("mDriver", mDriver.getText().toString());
        editor.putString("mClient", mClient.getText().toString());
        editor.putString("mCharger", mCharger.getText().toString());
        editor.putString("mAddress", mAddress.getText().toString());
        editor.putString("mApertureHour", mApertureHour.getText().toString());
        editor.putString("mContainerNumber", mContainerNumber.getText().toString());
        editor.putString("mArrivalHour", mArrivalHour.getText().toString());
        editor.putString("mDepartureHour", mDepartureHour.getText().toString());
        editor.putString("mContainerChargeDay", mContainerChargeDay.getText().toString());
        editor.putString("mContainerChargeHour", mContainerChargeHour.getText().toString());
        editor.putString("mContainerDischargeHour", mContainerDischargeHour.getText().toString());
        editor.putString("mContainerDischargeDay", mContainerDischargeDay.getText().toString());
        editor.putString("mTextArea", mTextArea.getText().toString());

        editor.commit();

    }

    //Method for load data on device memory
    public void loadSharedPreferences(){
        sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);

        mDate.setText(sharedPref.getString("mDate", ""));
        mDriver.setText(sharedPref.getString("mDriver", ""));
        mClient.setText(sharedPref.getString("mClient", ""));
        mCharger.setText(sharedPref.getString("mCharger", ""));
        mAddress.setText(sharedPref.getString("mAddress", ""));
        mApertureHour.setText(sharedPref.getString("mApertureHour", ""));
        mContainerNumber.setText(sharedPref.getString("mContainerNumber", ""));
        mArrivalHour.setText(sharedPref.getString("mArrivalHour", ""));
        mDepartureHour.setText(sharedPref.getString("mDepartureHour", ""));
        mContainerChargeDay.setText(sharedPref.getString("mContainerChargeDay", ""));
        mContainerChargeHour.setText(sharedPref.getString("mContainerChargeHour", ""));
        mContainerDischargeHour.setText(sharedPref.getString("mContainerDischargeHour", ""));
        mContainerDischargeDay.setText(sharedPref.getString("mContainerDischargeDay", ""));
        mTextArea.setText(sharedPref.getString("mTextArea", ""));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void formData(){

        //put the current date on the form
        mDate.setText(getDay() + "/" + getMonth() + "/" + getYear());

        //mDriver.setText(user.getEmployeeNameSettings().toString());
        //mTruckNumber.setText(user.getTruckNumSettings().toString());
        //mTruckID.setText(user.getTruckIdSettings().toString());
        //mPlatformID.setText(user.getPlatformIdSettings().toString());

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
                                    final TimePickerDialog timePickerDialog  = new TimePickerDialog(NewOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                    if (!mContainerChargeDay.getText().toString().matches("")) {

                        AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                        alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                mContainerChargeDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                            }
                                        }, mCalendarPicker.get(Calendar.YEAR), mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
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

                    } else {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mContainerChargeDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, mCalendarPicker.get(Calendar.YEAR), mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }

                    if(!mContainerDischargeDay.getText().toString().equals("") && !compareDates(mContainerChargeDay.getText().toString(), mContainerDischargeDay.getText().toString())){
                        AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                        alertBuild.setMessage("Fecha de carga posterior a la fecha de descarga")
                                .setCancelable(false)
                                .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                mContainerChargeDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                            }
                                        }, mCalendarPicker.get(Calendar.YEAR), mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                                        datePickerDialog.show();
                                    }
                                });
                        alertBuild.create();
                        alertBuild.show();
                    }
                }
        });

        //add the day when the container is discharged
        mContainerDischargeDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                if(!mContainerChargeDay.getText().toString().matches("")) {

                    if (!mContainerDischargeDay.getText().toString().matches("")) {

                        AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                        alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                mContainerDischargeDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                            }
                                        }, mCalendarPicker.get(Calendar.YEAR), mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
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

                    } else {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mContainerDischargeDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, mCalendarPicker.get(Calendar.YEAR), mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();

                    }
                }else if (mContainerChargeDay.getText().toString().matches("")){
                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(NewOrderActivity.this);
                    alertBuild.setMessage("Primero debe indicar el día de carga")
                            .setCancelable(false)
                            .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertBuild.create();
                    alertBuild.show();
                }

            }
        });

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

    }


    //Método para guardar la orden en firebase
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SendFirebaseData(){

        mProgressLoad = new ProgressDialog(NewOrderActivity.this);
        mProgressLoad.setTitle("Guardando");
        mProgressLoad.setMessage("Guardando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Orders").child(getYear()).child(getMonth());

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

                            clearSharedPreferences();
                            //clearData();

                            //Load main page when the order is sent
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

    private void clearData() {
        mDriver.setText("");

    }

    //Clear data from the previous form
    private void clearSharedPreferences() {

        sharedPref = getSharedPreferences("orderInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //sharedPref.edit().remove("mDriver").apply();
        editor.clear();
        editor.apply();
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

    public boolean compareDates(String after, String before){

        SimpleDateFormat formatAfter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatBefore = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateAfter = formatAfter.parse(after);
            Date dateBefore = formatBefore.parse(before);
            System.out.println(dateAfter+"//////////////"+dateBefore);
            if(dateAfter.after(dateBefore) || dateAfter.equals(dateBefore)){
                System.out.println("Es bien");
                return true;
            }else {
                System.out.println("No mal");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "Orden guardada", Toast.LENGTH_SHORT).show();
        saveSharedPreferences();
    }

}