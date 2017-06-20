package com.casa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSalir;
    private TextView mUserData;
    private Toolbar toolbar;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseAuth.getCurrentUser() == null ){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mSalir = (Button) findViewById(R.id.exitButton);
        mUserData = (TextView) findViewById(R.id.userDataField);

        mUserData.setText("Bienvenido "+ mFirebaseUser.getEmail());
        mSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == mSalir){
            mFirebaseAuth.signOut();
            finish();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater(); // para crear el menu de 3 puntos en la actionBar
        menuInflater.inflate(R.menu.menu, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MenuActivity.this, "Error usuario y/o contraseña", Toast.LENGTH_LONG).show();
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
