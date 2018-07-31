package com.casa.myapplication.Model;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.casa.myapplication.Logic.Maintenance;
import com.casa.myapplication.Logic.User;
import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MaintenanceActivity extends AppCompatActivity {

    private EditText mMaintenanceDate, mMaintenanceTruckId, mMaintenanceTruckNum, mMaintenanceKm, mMaintenanceType, mMaintenanceComment;
    private Button bSendMaintenance;
    private Calendar mCalendarPicker = Calendar.getInstance();
    private Calendar mTimePicker = Calendar.getInstance();
    private ProgressDialog mProgressLoad;

    private User user = new User();

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        mProgressLoad = new ProgressDialog(MaintenanceActivity.this);
        mProgressLoad.setTitle("Cargando");
        mProgressLoad.setMessage("Cargando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);
        mProgressLoad.show();

        //prepare firebase variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mMaintenanceDate = (EditText) findViewById(R.id.maintenande_date);
        mMaintenanceTruckId = (EditText) findViewById(R.id.maintenande_truckId);
        mMaintenanceTruckNum = (EditText) findViewById(R.id.maintenande_truckNum);
        mMaintenanceKm = (EditText) findViewById(R.id.maintenande_km);
        mMaintenanceType = (EditText) findViewById(R.id.maintenande_type);
        mMaintenanceComment = (EditText) findViewById(R.id.maintenande_comment);
        bSendMaintenance = (Button) findViewById(R.id.maintenance_send);

        loadSettings();
        formatDate();
        sendFirebase();

    }

    private void loadSettings() {
        DatabaseReference mSettings = mFirebaseDatabase.getReference().child("Users").child(userID).child("Settings");
        mSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                mMaintenanceTruckNum.setText(user.getTruckNumSettings());
                mMaintenanceTruckId.setText(user.getTruckIdSettings());

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void formatDate(){

        mMaintenanceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMaintenanceDate.getText().toString().matches("")){

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(MaintenanceActivity.this);
                    alertBuild.setMessage("Ya se ha anotado una fecha en este campo, ¿Desea cambiarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            mMaintenanceDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mMaintenanceDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },mCalendarPicker.get(Calendar.YEAR),mCalendarPicker.get(Calendar.MONTH), mCalendarPicker.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });
    }

    private void sendFirebase() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Maintenance");

        bSendMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Maintenance newMaintenance = new Maintenance();

                newMaintenance.setDate(mMaintenanceDate.getText().toString());
                newMaintenance.setTruckId(mMaintenanceTruckId.getText().toString());
                newMaintenance.setTruckNum(mMaintenanceTruckNum.getText().toString());
                newMaintenance.setKm(mMaintenanceKm.getText().toString());
                newMaintenance.setType(mMaintenanceType.getText().toString());
                newMaintenance.setComments(mMaintenanceComment.getText().toString());

                if(mMaintenanceDate.getText().toString().equals("") || mMaintenanceTruckId.getText().toString().equals("") || mMaintenanceTruckNum.getText().toString().equals("") || mMaintenanceKm.getText().toString().equals("") || mMaintenanceType.getText().toString().equals("") || mMaintenanceComment.getText().toString().equals("")){

                    new AlertDialog.Builder(MaintenanceActivity.this)
                            .setTitle("Campos en blanco")
                            .setMessage("No pueden haber campos en blanco para poder añadir nuevos clientes")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();

                }else{
                    //load progres dialog
                    mProgressLoad = new ProgressDialog(MaintenanceActivity.this);
                    mProgressLoad.setTitle("Guardando");
                    mProgressLoad.setMessage("Guardando datos, por favor espere");
                    mProgressLoad.setCanceledOnTouchOutside(false);
                    mProgressLoad.show();

                    mDatabase.push().setValue(newMaintenance).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Intent goToMainPage = new Intent(MaintenanceActivity.this, MenuActivity.class);
                                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToMainPage);
                                mProgressLoad.dismiss();
                                finish();

                            } else {
                                mProgressLoad.hide();
                                Toast.makeText(MaintenanceActivity.this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
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
