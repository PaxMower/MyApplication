package com.casa.myapplication.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.casa.myapplication.Model.User;
import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity{

    private CircleImageView mImage;
    private TextView mTruckId, mTruckNum, mPlatformId, mEmployeeName;
    private Button mChangeSettings;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    private ProgressDialog mProgressLoad;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        mProgressLoad.dismiss();

        //mDatabase = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mImage = (CircleImageView) findViewById(R.id.profile_image);
        mTruckId = (TextView) findViewById(R.id.truck_id_settings);
        mTruckNum = (TextView) findViewById(R.id.truck_num_settings);
        mPlatformId = (TextView) findViewById(R.id.platform_id_settings);
        mEmployeeName = (TextView) findViewById(R.id.driver_name);
        mChangeSettings = (Button) findViewById(R.id.button_change_settings);

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        LoadProfile();
        ChangeSettings();
    }

    public void LoadProfile() {

        mProgressLoad = new ProgressDialog(SettingsActivity.this);
        mProgressLoad.setTitle("Cargando");
        mProgressLoad.setMessage("Cargando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);
        mProgressLoad.show();

        DatabaseReference mSettings = mFirebaseDatabase.getReference().child("Users").child(userID).child("Settings");
        mSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                user = dataSnapshot.getValue(User.class);

                mEmployeeName.setText(user.getEmployeeNameSettings().toString());
                mTruckId.setText(user.getTruckIdSettings().toString());
                mTruckNum.setText(user.getTruckNumSettings().toString());
                mPlatformId.setText(user.getPlatformIdSettings().toString());


                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, "Error al cargar los datos", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void ChangeSettings() {
        mChangeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeSettingsActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu the_menu) {

        MenuInflater menuInflater = getMenuInflater(); // para crear el menu de 3 puntos en la ActionBar
        menuInflater.inflate(R.menu.menu, the_menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_exit:
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }





}