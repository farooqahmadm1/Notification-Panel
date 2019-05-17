package com.example.farooq.notificationpanel.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farooq.notificationpanel.R;
import com.example.farooq.notificationpanel.SendActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragmentAdapter extends RecyclerView.Adapter<UserFragmentAdapter.UserHolder> {
    private Context context;
    private List<Users> usersList;

    public UserFragmentAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder,final int position) {
        holder.mName.setText(usersList.get(position).getName());
        holder.mEmail.setText(usersList.get(position).getEmail());
        Picasso.get().load(usersList.get(position).getImage()).placeholder(R.drawable.profile).into(holder.mImage);
        final String user_id = usersList.get(position).userId;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, SendActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("user_name",usersList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mEmail;
        CircleImageView mImage;
        public UserHolder(View itemView) {
            super(itemView);

            mImage = (CircleImageView)itemView.findViewById(R.id.users_single_image);
            mName = (TextView) itemView.findViewById(R.id.users_single_name);
            mEmail = (TextView) itemView.findViewById(R.id.users_single_email);
        }
    }
}
