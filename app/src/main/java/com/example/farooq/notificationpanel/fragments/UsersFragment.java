package com.example.farooq.notificationpanel.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.notificationpanel.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private View mView;
    private RecyclerView recyclerView;
    private List<Users> usersList;
    private UserFragmentAdapter adapter;

    private FirebaseFirestore mFirestore;
    public UsersFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_users, container, false);

        usersList= new ArrayList<Users>();
        recyclerView= (RecyclerView) mView.findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter=  new UserFragmentAdapter(container.getContext(),usersList);
        recyclerView.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        usersList.clear();
        mFirestore.collection("users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType()== DocumentChange.Type.ADDED){

                        String userId= doc.getDocument().getId();
                        Users users = doc.getDocument().toObject(Users.class).withId(userId);
                        usersList.add(users);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
