package com.example.ntchat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;


    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent,false);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);



        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;


        public MessageViewHolder(View view){
            super(view);
            messageText = (TextView)view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView)view.findViewById(R.id.message_profile_layout);
            displayName = (TextView)view.findViewById(R.id.name_text_layout);
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages c = mMessageList.get(position);

        String from_user = c.getFrom();

        if (from_user.equals(current_user_id)){
            mUsersDatabase.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    final String userName = snapshot.child("name").getValue().toString();
                    holder.displayName.setText(userName);
                    Picasso.get().load(snapshot.child("image").getValue().toString()).placeholder(R.drawable.default_avatar).into(holder.profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            RelativeLayout.LayoutParams params = new RelativeLayout.
//                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            holder.messageText.setLayoutParams(params);
//
//
            holder.messageText.setBackgroundResource(R.drawable.message_text_current);
            holder.messageText.setTextColor(Color.BLACK);
//            holder.profileImage.setVisibility(View.GONE);

        }else {
            mUsersDatabase.child(from_user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String userName = snapshot.child("name").getValue().toString();
                    holder.displayName.setText(userName);
                    Picasso.get().load(snapshot.child("image").getValue().toString()).placeholder(R.drawable.default_avatar).into(holder.profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);

        }


        holder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}




