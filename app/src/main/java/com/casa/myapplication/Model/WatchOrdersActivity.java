package com.casa.myapplication.Model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.casa.myapplication.Adapter.CustomExpandableListView;
import com.casa.myapplication.Logic.Order;
import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WatchOrdersActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<Order>> listDataChild;
    CustomExpandableListView customExpandableListView;

    //private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;
    private Double amount = 0.0;

    private ProgressDialog mProgressLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_orders);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<Order>>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabase = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mostrará como un UP

        mProgressLoad = new ProgressDialog(WatchOrdersActivity.this);
        mProgressLoad.setTitle("Cargando");
        mProgressLoad.setMessage("Cargando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);
        mProgressLoad.show();

//        addControl();

        new Thread(){
            @Override
            public void run(){
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mProgressLoad = new ProgressDialog(WatchOrdersActivity.this);
//                            mProgressLoad.setTitle("Cargando");
//                            mProgressLoad.setMessage("Cargando datos, por favor espere");
//                            mProgressLoad.setCanceledOnTouchOutside(false);
//                            mProgressLoad.show();
                            addControl();
                        }
                    });
                }catch (final Exception ex){
                    Log.i("THREAD EXCEPTION", ex.toString());
                }
            }
        }.start();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.v("LOG_TESTING-->", String.valueOf(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAddress()));

                Bundle bundle = new Bundle();
                Intent i = new Intent(WatchOrdersActivity.this, EditOrderActivity.class);

                bundle.putString("date", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDate());
                bundle.putString("driver", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDriver());
                bundle.putString("truckNum", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTruckNumber());
                bundle.putString("truckId", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTruckID());
                bundle.putString("platformId", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPlatformID());
                bundle.putString("client", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getClient());
                bundle.putString("charger", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCharger());
                bundle.putString("address", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAddress());
                bundle.putString("city", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCity());
                bundle.putString("state", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getState());
                bundle.putString("price", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPrice());
                bundle.putString("timeSchedule", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getApertureHour());
                bundle.putString("phone", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPhone());
                bundle.putString("containerNum", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getContainerNumber());
                bundle.putString("arrivalHour", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getArrivalHour());
                bundle.putString("departureHour", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDepartureHour());
                bundle.putString("chargingDay", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getContainerChargeDay());
                bundle.putString("chargingHour", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getContainerChargeHour());
                bundle.putString("dischargingDay", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getContainerDischargeDay());
                bundle.putString("dischargingHour", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getContainerDischargeHour());
                bundle.putString("textArea", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTextArea());

                i.putExtras(bundle);
                startActivity(i);


//                Intent goToMainPage = new Intent(WatchOrdersActivity.this, EditOrderActivity.class);
//                goToMainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(goToMainPage);

                return false;
            }
        });

    }

    private void addControl() {

//        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//        listDataHeader = new ArrayList<>();
//        listDataChild = new HashMap<String, List<Order>>();


//        DatabaseReference mData = mFirebaseDatabase.getReference().child("Users").child(userID).child("Orders");
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Orders");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int x = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    amount = 0.0;

                    for(DataSnapshot ds2 : ds.getChildren()){
                        Order order = ds2.getValue(Order.class);
                        amount += Double.parseDouble(order.getPrice());
                    }

                    listDataHeader.add(ds.getKey()+"                         "+amount+" €");
                    List<Order> uno = new ArrayList<Order>();

                    for(DataSnapshot ds2 : ds.getChildren()){
                        Order order = ds2.getValue(Order.class);
                        uno.add(order);
                        listDataChild.put(listDataHeader.get(x), uno);
                        //amount += Double.parseDouble(order.getPrice());
                    }
                    x++;
                }

                customExpandableListView = new CustomExpandableListView(WatchOrdersActivity.this, listDataHeader, listDataChild);
                expandableListView.setAdapter(customExpandableListView);

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

