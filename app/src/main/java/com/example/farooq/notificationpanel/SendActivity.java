package com.example.farooq.notificationpanel;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SendActivity";
    private FirebaseFirestore mFirestore;

    private String mCurrentId;
    private String mUserId;
    private String userName;
    //Views
    private TextView mUserName;
    private EditText mMessage;
    private Button mSendBtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        progressBar = (ProgressBar) findViewById(R.id.send_progress_bar);
        mSendBtn = (Button) findViewById(R.id.send_message_btn);
        mMessage = (EditText) findViewById(R.id.send_message);
        mUserName= (TextView) findViewById(R.id.send_user_name);
        mUserId  =  getIntent().getStringExtra("user_id");
        userName =  getIntent().getStringExtra("user_name");
        mUserName.setText("Send to "+userName);

        mFirestore = FirebaseFirestore.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getUid();

        mSendBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick : start");
        String message = mMessage.getText().toString();
        if (message.equals("")){
            Log.d(TAG,"onClick : Message Notification Field Empty ");
            Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Map notification = new HashMap();
        notification.put("message",message);
        notification.put("from",mCurrentId);
        mFirestore.collection("users/"+ mUserId + "/Notifications").add(notification).addOnSuccessListener(new OnSuccessListener() {
            @Override public void onSuccess(Object o) {
                mMessage.setText("");
                logToast("Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                logToast("Failure");
            }
        });
    }
    private void logToast(String result){
        Toast.makeText(SendActivity.this, result, Toast.LENGTH_SHORT).show();
        Log.d(TAG," onClick : "+result);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
