package com.casa.myapplication.Model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Gastby on 21/06/2017.
 */

public class WatchOrdersActivity extends AppCompatActivity {

    private ExpandableListView mOrderList;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_orders);

        mOrderList = (ExpandableListView) findViewById(R.id.expListView);

        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mostrará como un UP

    }











    @Override
    public boolean onCreateOptionsMenu(Menu the_menu) {

        MenuInflater menuInflater = getMenuInflater(); // para crear el menu de 3 puntos en la ActionBar
        menuInflater.inflate(R.menu.menu, the_menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(WatchOrdersActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_exit:
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
