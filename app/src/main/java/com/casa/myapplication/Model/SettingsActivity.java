package com.casa.myapplication.Model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity{

    private CircleImageView mImage;
    private TextView mTruckId, mTruckNum, mPlatformId;
    private Button mChangeTruckId, mChangeTruckNum, mChangePlatformId;


    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference mDatabase;
    private FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = current_user.getUid();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mImage = (CircleImageView) findViewById(R.id.profile_image);
        mTruckId = (TextView) findViewById(R.id.truck_id_settings);
        mTruckNum = (TextView) findViewById(R.id.truck_num_settings);
        mPlatformId = (TextView) findViewById(R.id.platform_id_settings);
        mChangeTruckId = (Button) findViewById(R.id.edit_truck_num);
        mChangeTruckNum = (Button) findViewById(R.id.edit_truck_id);
        mChangePlatformId = (Button) findViewById(R.id.edit_platform_id);

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        LoadProfile();
        ChangeSettings();
    }

    private void LoadProfile() {

    }

    private void ChangeSettings() {
        mChangeTruckId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertBuild = new AlertDialog.Builder(SettingsActivity.this);
                alertBuild.setMessage("Inserte nuevo número de camión")
                    .setCancelable(false)
                    .set
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