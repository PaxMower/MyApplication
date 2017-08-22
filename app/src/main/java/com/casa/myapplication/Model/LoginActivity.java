package com.casa.myapplication.Model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casa.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mMailField, mPassField;
    private Button mButtonLog;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mMailField = (EditText) findViewById(R.id.mailField);
        mPassField = (EditText) findViewById(R.id.passField);
        mButtonLog = (Button) findViewById(R.id.buttonLog);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){ //el usuario está logeado ya
                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                }
            }
        };


        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

    }

    private void startLogin(){

        String email = mMailField.getText().toString().trim();
        String pass = mPassField.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)/*probar --> email.isEmpty() || pass.isEmpty()*/){

            Toast.makeText(LoginActivity.this, "Campos vacios", Toast.LENGTH_LONG).show();

        }else{

            mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        System.out.println(task.getException().toString());
                        Toast.makeText(LoginActivity.this, "Error usuario y/o contraseña", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }


}
