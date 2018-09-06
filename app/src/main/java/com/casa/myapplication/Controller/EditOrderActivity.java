package com.casa.myapplication.Controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditOrderActivity extends AppCompatActivity {

    private String  id, month, year;
    private EditText mDriver, mTruckId, mTruckNum, mPlatform,
            mDate, mContainer, mCharger, mClient, mAddress,
            mCity, mState, mPhone, mSchedule, mArrivalHour,
            mDepartureHour, mChargeDay, mChargeHour,
            mDischargeHour, mDischargeDay, mComments;
    private Button mSend;
    private TextView mPrice;
    private NewOrderActivity noa;
    private ProgressDialog mProgressLoad;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

//        mProgressLoad.dismiss();

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        noa = new NewOrderActivity();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        userID = userAuth.getUid();

        mDriver = (EditText) findViewById(R.id.edit_expandable_driver);
        mTruckId = (EditText) findViewById(R.id.edit_expandable_truckId);
        mTruckNum = (EditText) findViewById(R.id.edit_expandable_truckNum);
        mPlatform = (EditText) findViewById(R.id.edit_expandable_platform);
        mDate = (EditText) findViewById(R.id.edit_expandable_date);
        mContainer = (EditText) findViewById(R.id.edit_expandable_container);
        mCharger = (EditText) findViewById(R.id.edit_expandable_charger);
        mClient = (EditText) findViewById(R.id.edit_expandable_client);
        mAddress = (EditText) findViewById(R.id.edit_expandable_address);
        mCity = (EditText) findViewById(R.id.edit_expandable_city);
        mState = (EditText) findViewById(R.id.edit_expandable_state);
        mPhone = (EditText) findViewById(R.id.edit_expandable_phone);
        mSchedule = (EditText) findViewById(R.id.edit_expandable_schedule);
        mArrivalHour = (EditText) findViewById(R.id.edit_expandable_arrivalTime);
        mDepartureHour = (EditText) findViewById(R.id.edit_expandable_departureTime);
        mChargeDay = (EditText) findViewById(R.id.edit_expandable_chargerDay);
        mChargeHour = (EditText) findViewById(R.id.edit_expandable_chargerHour);
        mDischargeHour = (EditText) findViewById(R.id.edit_expandable_dischargerHour);
        mDischargeDay = (EditText) findViewById(R.id.edit_expandable_dischargerDay);
        mComments = (EditText) findViewById(R.id.edit_expandable_textArea);
        mPrice = (TextView) findViewById(R.id.edit_expandable_price);
        mSend = (Button) findViewById(R.id.edit_expandable_button);

        loadBundle();
        sendData();
    }

    private void loadBundle() {
        Bundle bundle = getIntent().getExtras();
        year = bundle.getString("year");
        month = bundle.getString("month");
        id = bundle.getString("id");
        mTruckId.setText(bundle.getString("truckId"));
        mTruckNum.setText(bundle.getString("truckNum"));
        mPlatform.setText(bundle.getString("platformId"));
        mDate.setText(bundle.getString("date"));
        mContainer.setText(bundle.getString("containerNum"));
        mDriver.setText(bundle.getString("driver"));
        mCharger.setText(bundle.getString("charger"));
        mClient.setText(bundle.getString("client"));
        mAddress.setText(bundle.getString("address"));
        mCity.setText(bundle.getString("city"));
        mState.setText(bundle.getString("state"));
        mPhone.setText(bundle.getString("phone"));
        mSchedule.setText(bundle.getString("timeSchedule"));
        mArrivalHour.setText(bundle.getString("arrivalHour"));
        mDepartureHour.setText(bundle.getString("departureHour"));
        mChargeDay.setText(bundle.getString("chargingDay"));
        mChargeHour.setText(bundle.getString("chargingHour"));
        mDischargeHour.setText(bundle.getString("dischargingHour"));
        mDischargeDay.setText(bundle.getString("dischargingDay"));
        mComments.setText(bundle.getString("textArea"));
        mPrice.setText(bundle.getString("price"));
    }

    private void sendData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Orders").child(year+"-"+month);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDate.getText().toString().equals("") || mDriver.getText().toString().equals("") || mTruckNum.getText().toString().equals("") || mTruckId.getText().toString().equals("") ||
                        mPlatform.getText().toString().equals("") || mClient.getText().toString().equals("") || mCharger.getText().toString().equals("") || mAddress.getText().toString().equals("") ||
                        mSchedule.getText().toString().equals("") || mPhone.getText().toString().equals("") || mContainer.getText().toString().equals("") ||
                        mArrivalHour.getText().toString().equals("") || mDepartureHour.getText().toString().equals("") || mChargeDay.getText().toString().equals("") || mChargeHour.getText().toString().equals("") ||
                        mDischargeDay.getText().toString().equals("") || mDischargeHour.getText().toString().equals("") || mState.getText().toString().equals("")
                        || mCity.getText().toString().equals("") || mPrice.getText().toString().equals("")){
                    new AlertDialog.Builder(EditOrderActivity.this)
                            .setTitle("Campos en blanco")
                            .setMessage("Solo el campo Observaciones puede quedar vacío")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();
                } else {
                    mProgressLoad = new ProgressDialog(EditOrderActivity.this);
                    mProgressLoad.setTitle("Guardando");
                    mProgressLoad.setMessage("Guardando datos, por favor espere");
                    mProgressLoad.setCanceledOnTouchOutside(false);
                    mProgressLoad.show();

                    mDatabase.child(id).child("driver").setValue(mDriver.getText().toString());
                    mDatabase.child(id).child("truckId").setValue(mTruckId.getText().toString());
                    mDatabase.child(id).child("truckNumber").setValue(mTruckNum.getText().toString());
                    mDatabase.child(id).child("platformId").setValue(mPlatform.getText().toString());
                    mDatabase.child(id).child("date").setValue(mDate.getText().toString());
                    mDatabase.child(id).child("containerNumber").setValue(mContainer.getText().toString());
                    mDatabase.child(id).child("price").setValue(mPrice.getText().toString());
                    mDatabase.child(id).child("charger").setValue(mCharger.getText().toString());
                    mDatabase.child(id).child("client").setValue(mClient.getText().toString());
                    mDatabase.child(id).child("address").setValue(mAddress.getText().toString());
                    mDatabase.child(id).child("city").setValue(mCity.getText().toString());
                    mDatabase.child(id).child("state").setValue(mState.getText().toString());
                    mDatabase.child(id).child("phone").setValue(mPhone.getText().toString());
                    mDatabase.child(id).child("apertureHour").setValue(mSchedule.getText().toString());
                    mDatabase.child(id).child("arrivalHour").setValue(mArrivalHour.getText().toString());
                    mDatabase.child(id).child("departureHour").setValue(mDepartureHour.getText().toString());
                    mDatabase.child(id).child("containerChargeDay").setValue(mChargeDay.getText().toString());
                    mDatabase.child(id).child("containerChargeHour").setValue(mChargeHour.getText().toString());
                    mDatabase.child(id).child("containerDischargeDay").setValue(mDischargeDay.getText().toString());
                    mDatabase.child(id).child("containerDischargeHour").setValue(mDischargeHour.getText().toString());
                    mDatabase.child(id).child("textArea").setValue(mComments.getText().toString());


                    if (mProgressLoad != null && mProgressLoad.isShowing()) {
                        mProgressLoad.dismiss();
                    }

                    Intent goToMainPage = new Intent(EditOrderActivity.this, WatchOrdersActivity.class);
                    goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToMainPage);
                }
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
