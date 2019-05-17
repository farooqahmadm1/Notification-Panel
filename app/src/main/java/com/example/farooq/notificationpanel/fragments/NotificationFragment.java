package com.example.farooq.notificationpanel.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.notificationpanel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private RecyclerView mRecyclerView;
    private View mView;
    private List<Notification> list;
    private NotificationAdapter adapter;

    public NotificationFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_notification, container, false);

        list = new ArrayList<>();
        mRecyclerView= mView.findViewById(R.id.notification_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mFirestore = FirebaseFirestore.getInstance();

        adapter = new NotificationAdapter(container.getContext(),list);
        mRecyclerView.setAdapter(adapter);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        list.clear();
        String current_id = FirebaseAuth.getInstance().getUid();
        mFirestore.collection("users/"+ current_id + "/Notifications").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType()== DocumentChange.Type.ADDED){
                        Notification notification = doc.getDocument().toObject(Notification.class);
                        list.add(notification);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }
}
