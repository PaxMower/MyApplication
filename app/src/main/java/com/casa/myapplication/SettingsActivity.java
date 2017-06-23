package com.casa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsActivity extends AppCompatActivity{

    private TextView mTextView;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextView = (TextView) findViewById(R.id.textViewSettings);

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
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_exit:

                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



}