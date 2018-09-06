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

import com.casa.myapplication.Adapter.PetrolAdapter;
import com.casa.myapplication.Listener.RecyclerTouchListener;
import com.casa.myapplication.Model.Petrol;
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

public class WatchPetrolActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Petrol> petrolList = new ArrayList<>();
    private List<String> identifier = new ArrayList<>();
    private PetrolAdapter adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;

    private ProgressDialog mProgressLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_petrol);

//        mProgressLoad.dismiss();

        //setting up the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerPetrl);
        adapter = new PetrolAdapter(petrolList);

        RecyclerView.LayoutManager mLauyoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLauyoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //watch for load data
        mProgressLoad = new ProgressDialog(WatchPetrolActivity.this);
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
        DatabaseReference mData = mFirebaseDatabase.getReference("Users").child(userID).child("Petrol");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Petrol m = ds.getValue(Petrol.class);
                    petrolList.add(m);
                    identifier.add(ds.getKey());
                }
                adapter.notifyDataSetChanged();
                if (mProgressLoad != null && mProgressLoad.isShowing()) {
                    mProgressLoad.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void itemSelected() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Petrol petrol = petrolList.get(position);
                String ident = identifier.get(position);
                Bundle bundle = new Bundle();
                Intent i = new Intent(WatchPetrolActivity.this, EditPetrolActivity.class);

                bundle.putString("date", petrol.getDate());
                bundle.putString("km", petrol.getKm());
                bundle.putString("truckId", petrol.getTruckId());
                bundle.putString("truckNum", petrol.getTruckNum());
                bundle.putString("liters", petrol.getLiters());
                bundle.putString("hour", petrol.getHour());
                bundle.putString("id", ident);

                i.putExtras(bundle);
                startActivity(i);
            }
            @Override
            public void onLongClick(View view, int position) {}
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
