package com.example.farooq.notificationpanel.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farooq.notificationpanel.LoginActivity;
import com.example.farooq.notificationpanel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment{

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseUser currentUser;
    private String mCurrentUser;

    private View mView;
    private Button mLogoutButton;
    private TextView mUserName;
    private CircleImageView mProfileImage;

    public ProfileFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mCurrentUser= currentUser.getUid();
        mFirestore = FirebaseFirestore.getInstance();

        mProfileImage = (CircleImageView) mView.findViewById(R.id.profile_img);
        mUserName = (TextView) mView.findViewById(R.id.profile_user_name);
        mLogoutButton = (Button) mView.findViewById(R.id.profile_logout_btn);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map removeToken= new HashMap();
                removeToken.put("token_id", "");
                mFirestore.collection("users").document(mCurrentUser).update(removeToken).addOnSuccessListener(new OnSuccessListener() {
                    @Override public void onSuccess(Object o) {
                        mAuth.signOut();
                        Intent intent= new Intent(container.getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loadFireStoreData();
        return mView;
    }

    private void loadFireStoreData() {
        mFirestore.collection("users").document(mCurrentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name= documentSnapshot.getString("name").toString();
                String image= documentSnapshot.getString("image").toString();
                mUserName.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.profile).into(mProfileImage);
            }
        });
    }
}
