package com.casa.myapplication.Model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPetrolActivity extends AppCompatActivity {

    private String km, truckId, truckNum, date, id, hour, liters;
    private EditText mKm, mTruckId, mTruckNum,  mDate, mHour, mLiters;
    private Button mSend;

    private ProgressDialog mProgressLoad;


    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userAuth = mAuth.getCurrentUser();
    private String userID = userAuth.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_petrol);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mHour = (EditText) findViewById(R.id.edit_petrol_hour);
        mLiters = (EditText) findViewById(R.id.edit_petrol_liters);
        mKm = (EditText) findViewById(R.id.edit_petrol_km);
        mTruckId = (EditText) findViewById(R.id.edit_petrol_truckId);
        mTruckNum = (EditText) findViewById(R.id.edit_petrol_truckNum);
        mDate = (EditText) findViewById(R.id.edit_petrol_date);
        mSend = (Button) findViewById(R.id.edit_petrol_send);

        loadBundle();
        sendData();
    }

    private void loadBundle() {
        Bundle bundle = getIntent().getExtras();
        hour = bundle.getString("hour");
        liters = bundle.getString("liters");
        km = bundle.getString("km");
        truckId = bundle.getString("truckId");
        truckNum = bundle.getString("truckNum");
        date = bundle.getString("date");
        id = bundle.getString("id");

        mHour.setText(hour);
        mLiters.setText(liters);
        mKm.setText(km);
        mTruckId.setText(truckId);
        mTruckNum.setText(truckNum);
        mDate.setText(date);
    }

    private void sendData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Petrol");

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressLoad = new ProgressDialog(EditPetrolActivity.this);
                mProgressLoad.setTitle("Guardando");
                mProgressLoad.setMessage("Guardando datos, por favor espere");
                mProgressLoad.setCanceledOnTouchOutside(false);
                mProgressLoad.show();

                mDatabase.child(id).child("hour").setValue(mHour.getText().toString());
                mDatabase.child(id).child("date").setValue(mDate.getText().toString());
                mDatabase.child(id).child("km").setValue(mKm.getText().toString());
                mDatabase.child(id).child("truckId").setValue(mTruckId.getText().toString());
                mDatabase.child(id).child("truckNum").setValue(mTruckNum.getText().toString());
                mDatabase.child(id).child("liters").setValue(mLiters.getText().toString());

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }

                Intent goToMainPage = new Intent(EditPetrolActivity.this, WatchPetrolActivity.class);
                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMainPage);
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
