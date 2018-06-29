package com.casa.myapplication.Model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_orders);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabase = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mostrará como un UP

        addControl();
        customExpandableListView = new CustomExpandableListView(WatchOrdersActivity.this, listDataHeader, listDataChild);
        expandableListView.setAdapter(customExpandableListView);
    }

    private void addControl() {

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<Order>>();


        DatabaseReference mData = mFirebaseDatabase.getReference().child("Users").child(userID).child("Orders");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int x = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    listDataHeader.add(ds.getKey());
                    List<Order> uno = new ArrayList<Order>();

                    for(DataSnapshot ds2 : ds.getChildren()){
                        //List<Order> uno = new ArrayList<Order>();

                        Order order = ds2.getValue(Order.class);
                        Log.v("FIREBASE_DATA", order.toString());

                        uno.add(order);

                        for(DataSnapshot ds3 : ds2.getChildren()){
                            //String min = ds3.child("minutes").getValue(String.class);

                            //User user = dataSnapshot.getValue(User.class);
                            //Order order = ds3.getValue(Order.class);
                            //SaveUsers(order);

                            //String min = ds3.toString();
                            //Log.v("FIREBASE_DATA", ds3.getValue().toString());
                            //System.out.println(user);
                            //uno.add(order);
                        }
                        listDataChild.put(listDataHeader.get(x), uno);

                    }
                    x++;
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //listDataHeader.add("Header uno");
        //listDataHeader.add("Header dos");

        //List<String> uno = new ArrayList<String>();
        //List<String> dos = new ArrayList<String>();

/*        uno.add("asdfasd");
        uno.add("13223e");
        dos.add("0909099")*/;

        /*for(int x = 0; x< listDataHeader.size(); x++){
            listDataChild.put(listDataHeader.get(x), uno);
        }*/

        //listDataChild.put(listDataHeader.get(0), uno);
        //listDataChild.put(listDataHeader.get(1), dos);

    }

    private void SaveUsers(Order order) {
        //Order order = ds3.getValue(Order.class);
        Log.v("USUARIOS!!!!!", order.toString());
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

