package com.example.farooq.notificationpanel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private Button mRegBtn;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String token_id;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if ( user != null ){
            sendToMain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        token_id= FirebaseInstanceId.getInstance().getToken();
        mEmailField = (EditText)findViewById(R.id.login_email);
        mPasswordField= (EditText) findViewById(R.id.login_password);
        mLoginBtn =(Button) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mRegBtn = (Button) findViewById(R.id.login_new_account);
        mRegBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar)  findViewById(R.id.login_progress_bar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }
    @Override
    public void onClick(View v) {
        if(v == mRegBtn){
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
            finish();
        }
        if(v==mLoginBtn){ signIn(); }
    }
    public void signIn(){
        String email=mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if(email.equals("")||password.equals("")){
            Toast.makeText(this, "Please Enter Your Information", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "signInWithEmail: checking");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: Sccess");
                            String current_id = mAuth.getCurrentUser().getUid();
                            Map tokenMap = new HashMap();
                            tokenMap.put("token_id",token_id);
                            mFirestore.collection("users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override public void onSuccess(Object o) {
                                    Log.d(TAG, "signInWithEmail: sucessfuly Uploaded data of toke id");
                                    logToast("Success");
                                    sendToMain();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override public void onFailure(@NonNull Exception e) {
                                    logToast("Failure");
                                }
                            });
                        }else{
                            logToast("Failure");
                        }
                    }
                });
        Log.d(TAG, "signInWithEmail: end");
    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void logToast(String result){
        Toast.makeText(LoginActivity.this,"Authentication "+ result, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
