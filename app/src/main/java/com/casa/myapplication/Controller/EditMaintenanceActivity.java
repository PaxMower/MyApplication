package com.casa.myapplication.Controller;

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

public class EditMaintenanceActivity extends AppCompatActivity {
    private String comments, type, km, truckId, truckNum, date, id;
    private EditText mComments, mType, mKm, mTruckId, mTruckNum,  mDate;
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
        setContentView(R.layout.activity_edit_maintenance);

//        mProgressLoad.dismiss();

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mComments = (EditText) findViewById(R.id.edit_maintenance_comments);
        mType = (EditText) findViewById(R.id.edit_maintenance_type);
        mKm = (EditText) findViewById(R.id.edit_maintenance_km);
        mTruckId = (EditText) findViewById(R.id.edit_maintenance_truckId);
        mTruckNum = (EditText) findViewById(R.id.edit_maintenance_truckNum);
        mDate = (EditText) findViewById(R.id.edit_maintenance_date);
        mSend = (Button) findViewById(R.id.edit_maintenance_send);

        loadBundle();
        sendData();
    }

    private void loadBundle() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        mComments.setText(bundle.getString("comments"));
        mType.setText(bundle.getString("type"));
        mKm.setText(bundle.getString("km"));
        mTruckId.setText(bundle.getString("truckId"));
        mTruckNum.setText(bundle.getString("truckNum"));
        mDate.setText(bundle.getString("date"));
    }

    private void sendData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Maintenance");
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressLoad = new ProgressDialog(EditMaintenanceActivity.this);
                mProgressLoad.setTitle("Guardando");
                mProgressLoad.setMessage("Guardando datos, por favor espere");
                mProgressLoad.setCanceledOnTouchOutside(false);
                mProgressLoad.show();

                mDatabase.child(id).child("comments").setValue(mComments.getText().toString());
                mDatabase.child(id).child("date").setValue(mDate.getText().toString());
                mDatabase.child(id).child("km").setValue(mKm.getText().toString());
                mDatabase.child(id).child("truckId").setValue(mTruckId.getText().toString());
                mDatabase.child(id).child("truckNum").setValue(mTruckNum.getText().toString());
                mDatabase.child(id).child("type").setValue(mType.getText().toString());

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }
                Intent goToMainPage = new Intent(EditMaintenanceActivity.this,
                        WatchMaintenanceActivity.class);
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
