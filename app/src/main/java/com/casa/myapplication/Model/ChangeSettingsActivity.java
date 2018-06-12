package com.casa.myapplication.Model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.casa.myapplication.Logic.User;
import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeSettingsActivity extends AppCompatActivity {

    private TextView mNewTruckId, mNewTruckNum, mNewPlatformId;
    private Button mSaveNewData;
    User changeUser = new User();
    private ProgressDialog mProgressLoad;
    private DatabaseReference mDatabase;
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = current_user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settings);
        mNewTruckId = (TextView)findViewById(R.id.newTruckId);
        mNewTruckNum = (TextView)findViewById(R.id.newTruckNum);
        mNewPlatformId = (TextView)findViewById(R.id.newPlatformId);
        mSaveNewData = (Button) findViewById(R.id.saveNewData);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        SaveNewData();
    }

    private void SaveNewData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings");

        mSaveNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*

                newUser.setTruckIdSettings(mNewTruckId.getText().toString());
                newUser.setTruckNumSettings(mNewTruckNum.getText().toString());
                newUser.setPlatformIdSettings(mNewPlatformId.getText().toString());
*/

                mProgressLoad = new ProgressDialog(ChangeSettingsActivity.this);
                mProgressLoad.setTitle("Guardando");
                mProgressLoad.setMessage("Guardando datos, por favor espere");
                mProgressLoad.setCanceledOnTouchOutside(false);
                mProgressLoad.show();


                mDatabase.child("platformIdSettings").setValue(mNewPlatformId.getText().toString());
                mDatabase.child("truckIdSettings").setValue(mNewTruckId.getText().toString());
                mDatabase.child("truckNumSettings").setValue(mNewTruckNum.getText().toString());

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }

                Intent goToMainPage = new Intent(ChangeSettingsActivity.this, SettingsActivity.class);
                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMainPage);



                //mDatabase.child("PlatformId").updateChildren();
                /*
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDatabase.child("PlatformId").setValue(newUser.getPlatformIdSettings());
                        mDatabase.child("TruckId").setValue(newUser.getTruckIdSettings());
                        mDatabase.child("TruckNumber").setValue(newUser.getTruckNumSettings());
                        mProgressLoad.dismiss();

                        Intent goToMainPage = new Intent(ChangeSettingsActivity.this, SettingsActivity.class);
                        goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToMainPage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ChangeSettingsActivity.this, "Error al cargar los datos", Toast.LENGTH_LONG).show();
                    }
                });*/

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
}