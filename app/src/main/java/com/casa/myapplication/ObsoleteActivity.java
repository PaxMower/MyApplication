package com.casa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ObsoleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSalir;
    private TextView mUserData;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obsolete);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseAuth.getCurrentUser() == null ){
            finish();
            startActivity(new Intent(this, MainActivity.class));
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
            startActivity(new Intent(ObsoleteActivity.this, MainActivity.class));
        }

    }
}
