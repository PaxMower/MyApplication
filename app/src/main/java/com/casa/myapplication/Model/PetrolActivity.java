package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.casa.myapplication.Logic.Petrol;
import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Gastby on 21/09/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class PetrolActivity extends AppCompatActivity{

    private EditText mPetrolDate, mPetrolHour, mPetrolKm, mPetrolLiters;
    private Button bSendPetrol;
    private Calendar mCalendarPicker = Calendar.getInstance();
    private Calendar mTimePicker = Calendar.getInstance();
    private ProgressDialog mProgressLoad;
    private DatabaseReference mDatabase;
    private String TAG = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol);

        mProgressLoad = new ProgressDialog(PetrolActivity.this);
        mProgressLoad.setTitle("Guardando");
        mProgressLoad.setMessage("Guardando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mPetrolDate = (EditText) findViewById(R.id.petrol_date);
        mPetrolHour = (EditText) findViewById(R.id.petrol_hour);
        mPetrolKm = (EditText) findViewById(R.id.petrol_km);
        mPetrolLiters = (EditText) findViewById(R.id.petrol_liters);
        bSendPetrol = (Button) findViewById(R.id.petrol_send);

        formData();
        SendFirebaseData();

    }

    public void formData(){

        mPetrolHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!mPetrolHour.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(PetrolActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una hora en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimePickerDialog timePickerDialog  = new TimePickerDialog(PetrolActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            mPetrolHour.setText(String.format("%02d:%02d",hourOfDay,minute));
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

                    TimePickerDialog timePickerDialog  = new TimePickerDialog(PetrolActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mPetrolHour.setText(String.format("%02d:%02d",hourOfDay,minute));
                        }
                    },mTimePicker.get(Calendar.HOUR_OF_DAY), mTimePicker.get(Calendar.MINUTE), true);
                    timePickerDialog.show();

                }
            }
        });

        mPetrolDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPetrolDate.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(PetrolActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(PetrolActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            mPetrolDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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

                    DatePickerDialog datePickerDialog = new DatePickerDialog(PetrolActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mPetrolDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });
    }

    private void SendFirebaseData() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Petrol");

        bSendPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Petrol newPetrol = new Petrol();

                newPetrol.setDate(mPetrolDate.getText().toString());
                newPetrol.setHour(mPetrolHour.getText().toString());
                newPetrol.setKm(mPetrolKm.getText().toString());
                newPetrol.setLiters(mPetrolLiters.getText().toString());


                if(mPetrolDate.getText().toString().equals("") || mPetrolHour.getText().toString().equals("") || mPetrolKm.getText().toString().equals("") || mPetrolLiters.getText().toString().equals("")){

                    new AlertDialog.Builder(PetrolActivity.this)
                            .setTitle("Campos en blanco")
                            .setMessage("No pueden haber campos en blanco para poder añadir nuevos clientes")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();

                }else{
                    mProgressLoad.show();

                    mDatabase.push().setValue(newPetrol).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Intent goToMainPage = new Intent(PetrolActivity.this, MenuActivity.class);
                                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToMainPage);
                                mProgressLoad.dismiss();
                                finish();

                            } else {
                                mProgressLoad.hide();
                                Toast.makeText(PetrolActivity.this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

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

}
