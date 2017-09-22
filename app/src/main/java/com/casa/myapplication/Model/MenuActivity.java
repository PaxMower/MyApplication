package com.casa.myapplication.Model;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.casa.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {//ActionBarActivity {//AppCompatActivity{

    private boolean isUserClickedBackButton = false;

    private TextView mUserData;
    private Button bNewOrder, bWatchOrders, bPetrol;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUserData = (TextView) findViewById(R.id.userDataField);
        mUserData.setText("Bienvenido "+ mFirebaseUser.getEmail());

        onClickButtons();

    }

    public void onClickButtons(){
        bNewOrder = (Button) findViewById(R.id.new_order);
        bWatchOrders = (Button) findViewById(R.id.watch_orders);
        bPetrol = (Button) findViewById(R.id.petrol_activity);

                bNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewOrderActivity.class));
            }
        });

        bWatchOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, WatchOrdersActivity.class));
            }
        });

        bPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PetrolActivity.class));
            }
        });
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
                startActivity(new Intent(this, SettingsActivity.class));
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

    @Override
    public void onBackPressed(){
        if(!isUserClickedBackButton){
            Toast.makeText(this, "Vuelva a presionar para salir",Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        }else{
            super.onBackPressed();
        }

        new CountDownTimer(1500, 1000){
            @Override
            public void onTick(long millisUntilFinished){

            }
            @Override
            public void onFinish(){
                isUserClickedBackButton = false;
            }
        }.start();
    }


}
