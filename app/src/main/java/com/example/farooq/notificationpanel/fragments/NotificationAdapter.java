package com.example.farooq.notificationpanel.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farooq.notificationpanel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>{

    private Context context;
    private List<Notification> list;

    private FirebaseFirestore mFirestore;
    public NotificationAdapter() { }
    public NotificationAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
        return new NotificationHolder(view);
    }
    @Override
    public void onBindViewHolder(final NotificationHolder holder, int position) {
        holder.mEmail.setText(list.get(position).getMessage());
        String id = list.get(position).getFrom();
        mFirestore= FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override public void onSuccess(DocumentSnapshot documentSnapshot) {
                Picasso.get().load(documentSnapshot.get("image").toString()).placeholder(R.drawable.profile).into(holder.mImage);
                holder.mName.setText(documentSnapshot.get("name").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Some thing went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mEmail;
        CircleImageView mImage;
        public NotificationHolder(View itemView) {
            super(itemView);
            mImage = (CircleImageView)itemView.findViewById(R.id.users_single_image);
            mName = (TextView) itemView.findViewById(R.id.users_single_name);
            mEmail = (TextView) itemView.findViewById(R.id.users_single_email);
        }
    }

}
