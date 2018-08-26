package com.casa.myapplication.Controller;

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
import com.casa.myapplication.Model.Order;
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
import java.util.Map;
import java.util.TreeMap;


public class WatchOrdersActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private CustomExpandableListView customExpandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<Order>> listDataChild;


    ////////////////////////////////////////////////////////
    private List<String> identifiersChild;
    private HashMap<String, List<String>> identifiersHeader;
    private Map<String, List<String>> sortedMap;//order map select the correct child id
    ////////////////////////////////////////////////////////


    //private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID, identifier;
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

        buttonClick();

    }


    private void addControl() {

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Orders");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                identifiersHeader = new HashMap<String, List<String>>();//hashMap for take identities
                int x = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    amount = 0.0;
                    identifiersChild = new ArrayList<String>();//list for save identities

                    for(DataSnapshot ds2 : ds.getChildren()){ //obtain total amount for every month
                        Order order = ds2.getValue(Order.class);
                        amount += Double.parseDouble(order.getPrice());
                    }

                    listDataHeader.add(ds.getKey()+"                         "+amount+" €");
                    List<Order> uno = new ArrayList<Order>();

                    for(DataSnapshot ds2 : ds.getChildren()){
                        Order order = ds2.getValue(Order.class);

                        uno.add(order);
                        listDataChild.put(listDataHeader.get(x), uno);

                        identifiersChild.add(ds2.getKey());
                    }

                    identifiersHeader.put(ds.getKey(), identifiersChild);

                    x++;


                }
                sortedMap = new TreeMap<String, List<String>>(identifiersHeader);

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

    private void loadIdentifier(int groupPosition, int childPosition){
        String y = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getYear();
        String m =listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getMonth();

        identifier = identifiersHeader.get(y + "-" + m).get(childPosition);
    }

    private void buttonClick() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                loadIdentifier(groupPosition, childPosition);

                Bundle bundle = new Bundle();
                Intent i = new Intent(WatchOrdersActivity.this, EditOrderActivity.class);

                bundle.putString("date", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDate());
                bundle.putString("driver", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDriver());
                bundle.putString("truckNum", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTruckNumber());
                bundle.putString("truckId", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTruckId());
                bundle.putString("platformId", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPlatformId());
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
                bundle.putString("month", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getMonth());
                bundle.putString("year", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getYear());
                bundle.putString("id", identifier);


                i.putExtras(bundle);
                startActivity(i);

                return false;
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

