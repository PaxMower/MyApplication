package com.casa.myapplication.Model;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.casa.myapplication.Adapter.AdapterMaintenance;
import com.casa.myapplication.Logic.Maintenance;
import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchMaintenanceActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Maintenance> maintenanceList = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    AdapterMaintenance adapter = new AdapterMaintenance(maintenanceList);

    private ProgressDialog mProgressLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //watch for load data
//        mProgressLoad = new ProgressDialog(WatchMaintenanceActivity.this);
//        mProgressLoad.setTitle("Cargando");
//        mProgressLoad.setMessage("Cargando datos, por favor espere");
//        mProgressLoad.setCanceledOnTouchOutside(false);
//        mProgressLoad.show();

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        rv = (RecyclerView) findViewById(R.id.recycler_maintenance);
//        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(adapter);

        loadDataFirebase();


    }

    private void loadDataFirebase() {

        DatabaseReference mData = mFirebaseDatabase.getReference("Users").child(userID).child("Maintenance");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                maintenanceList.removeAll(maintenanceList);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Maintenance m = ds.getValue(Maintenance.class);
                    maintenanceList.add(m);
                }
                Log.v("LOG_DATA_MAINTENANCE", maintenanceList.get(0).getType());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
