package com.example.farooq.notificationpanel;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private ProgressBar mProgressBar;
    private CircleImageView mImageBtn;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mRegBtn;
    private Button mBackLoginBtn;
    private Uri resultUri;
    private String token_id;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private StorageReference mStorage;
    private FirebaseFirestore mFireStore;
    private String mCurrentUser;
    @Override public void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if ( user != null ){
            sendToMain();
        }
    }
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        resultUri=null;
        token_id = FirebaseInstanceId.getInstance().getToken();
        mImageBtn= (CircleImageView) findViewById(R.id.reg_profile_image);
        mImageBtn.setOnClickListener(this);
        mNameField = (EditText) findViewById(R.id.reg_name);
        mEmailField = (EditText) findViewById(R.id.reg_email);
        mPasswordField = (EditText) findViewById(R.id.reg_password);
        mRegBtn =(Button) findViewById(R.id.reg_create_btn);
        mRegBtn.setOnClickListener(this);
        mBackLoginBtn= (Button)findViewById(R.id.reg_back_btn);
        mBackLoginBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);

        mAuth=FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mFireStore = FirebaseFirestore.getInstance();
    }

    @Override public void onClick(View v) {
        if (v == mBackLoginBtn) {
            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(i);
            finish(); }
        if (v == mRegBtn) {
            if(resultUri==null){
                Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = mNameField.getText().toString();
            String email= mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            if(email.equals("")||password.equals("")||name.equals("")){
                Toast.makeText(this, "Please Enter Your Information", Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            creatAccount(name,email,password);
        }
        if (v == mImageBtn) { cropImage(); }
    }
    public void creatAccount(final String name, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            mCurrentUser = currentUser.getUid().toString();
                            final String[] url = new String[1];
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "creatAccount :createUserWithEmail:success");
                            StorageReference reference=mStorage.child(mCurrentUser+".jpg");
                            reference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(TAG, "creatAccount : Image is  succesfully uploaded");
                                    url[0] =taskSnapshot.getDownloadUrl().toString();
                                    Log.d(TAG, "creatAccount : Image link "+ url[0]);
                                    Map userMap = new HashMap();
                                    userMap.put("name",name);
                                    userMap.put("email",email);
                                    userMap.put("token_id",token_id);
                                    userMap.put("image", url[0]);
                                    mFireStore.collection("users").document(mCurrentUser).set(userMap)
                                            .addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.d(TAG, "creatAccount : Data is Succesfullt is uploaded");
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    sendToMain();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            Log.d(TAG, "creatAccount : Data is not uploaded");
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "creatAccount : Image is not uploaded");
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "creatAccount : createUserWithEmail:failure", task.getException());
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void sendToMain() {
        Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    // get Image through This
    private void cropImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1).start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.get().load(resultUri).into(mImageBtn);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
