package com.casa.myapplication.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.casa.myapplication.Adapter.MaintenanceAdapter;
import com.casa.myapplication.Listener.RecyclerTouchListener;
import com.casa.myapplication.Model.Maintenance;
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

    private RecyclerView recyclerView;
    private List<Maintenance> maintenanceList = new ArrayList<>();
    private List<String> identifier = new ArrayList<>();
    private MaintenanceAdapter adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    private ProgressDialog mProgressLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_maintenance);

        //setting up the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMaint);
        adapter = new MaintenanceAdapter(maintenanceList);

        RecyclerView.LayoutManager mLauyoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLauyoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        //watch for load data
        mProgressLoad = new ProgressDialog(WatchMaintenanceActivity.this);
        mProgressLoad.setTitle("Cargando");
        mProgressLoad.setMessage("Cargando datos, por favor espere");
        mProgressLoad.setCanceledOnTouchOutside(false);
        mProgressLoad.show();

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        loadDataFirebase();
        itemSelected();
    }

    private void loadDataFirebase() {

        DatabaseReference mData = mFirebaseDatabase.getReference("Users").child(userID).child("Maintenance");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Maintenance m = ds.getValue(Maintenance.class);
                    maintenanceList.add(m);
                    identifier.add(ds.getKey());
                }

                adapter.notifyDataSetChanged();

                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void itemSelected() {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Maintenance maint = maintenanceList.get(position);
                String ident = identifier.get(position);
                Bundle bundle = new Bundle();
                Intent i = new Intent(WatchMaintenanceActivity.this, EditMaintenanceActivity.class);

                bundle.putString("comments", maint.getComments());
                bundle.putString("date", maint.getDate());
                bundle.putString("km", maint.getKm());
                bundle.putString("truckId", maint.getTruckId());
                bundle.putString("truckNum", maint.getTruckNum());
                bundle.putString("type", maint.getType());
                bundle.putString("id", ident);

                i.putExtras(bundle);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

