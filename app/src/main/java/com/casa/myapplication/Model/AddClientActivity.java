package com.casa.myapplication.Model;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casa.myapplication.Logic.Client;
import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddClientActivity extends AppCompatActivity {

    private EditText clientName, clientDir, clientPhone, clienteTime, clientLat, clientLon, clientCity, clientState, clientPC;
    private Button bAddClient;
    private Client newClient = new Client();
    private ProgressDialog mProgressLoad;
    private DatabaseReference mDatabase;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        clientName = (EditText) findViewById(R.id.client_name);
        clientDir = (EditText) findViewById(R.id.client_address);
        clientPhone = (EditText) findViewById(R.id.client_phone);
        clienteTime = (EditText) findViewById(R.id.client_time_schedule);
        clientLat = (EditText) findViewById(R.id.client_lat);
        clientLon = (EditText) findViewById(R.id.client_lon);
        clientCity = (EditText) findViewById(R.id.client_city);
        clientState = (EditText) findViewById(R.id.client_state);
        clientPC = (EditText) findViewById(R.id.client_PC);
        bAddClient = (Button) findViewById(R.id.add_client_send);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        LoadPosition();

        try {
            SendFirebaseData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadPosition() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.v("POSITION__", locationManager.toString());
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Log.v("POSITION__", location.toString());
            lat = location.getLatitude();
            lon = location.getLongitude();
            clientLat.setText(""+lat);
            clientLon.setText(""+lon);
        }
    }


    private void SendFirebaseData() throws IOException {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Clients");

        bAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newClient.setName(clientName.getText().toString());
                newClient.setAddress(clientDir.getText().toString());
                newClient.setPhone(clientPhone.getText().toString());
                newClient.setTimeSchedule(clienteTime.getText().toString());
                newClient.setLatitude(clientLat.getText().toString());
                newClient.setLongitude(clientLon.getText().toString());
                newClient.setCity(clientCity.getText().toString());
                newClient.setState(clientState.getText().toString());
                newClient.setPostalCode(clientPC.getText().toString());

                if(clientName.getText().toString().equals("") || clientDir.getText().toString().equals("") || clientPhone.getText().toString().equals("") ||
                        clienteTime.getText().toString().equals("") || clientState.getText().toString().equals("") ||
                        clientCity.getText().toString().equals("") || clientPC.getText().toString().equals("")){

                    new AlertDialog.Builder(AddClientActivity.this)
                            .setTitle("Campos en blanco")
                            .setMessage("No pueden haber campos en blanco para poder añadir nuevos clientes")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();

                }else{
                    mProgressLoad = new ProgressDialog(AddClientActivity.this);
                    mProgressLoad.setTitle("Guardando");
                    mProgressLoad.setMessage("Guardando datos, por favor espere");
                    mProgressLoad.setCanceledOnTouchOutside(false);
                    mProgressLoad.show();

                    mDatabase.push().setValue(newClient).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Intent goToMainPage = new Intent(AddClientActivity.this, MenuActivity.class);
                                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToMainPage);
                                mProgressLoad.dismiss();
                                finish();

                            } else {
                                mProgressLoad.hide();
                                Toast.makeText(AddClientActivity.this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
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
