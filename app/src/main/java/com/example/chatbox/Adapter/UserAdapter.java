package com.example.chatbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatbox.MessageActivity;
import com.example.chatbox.R;
import com.example.chatbox.Model.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String TAG = "UserAdapter";
        private Context context;
        private List<Users> Musers1;
        private boolean Ischat;

    public UserAdapter(Context context, List<Users> musers,boolean Ischat) {
        this.context = context;
        this.Musers1 = musers;
        this.Ischat=Ischat;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Users users=Musers1.get(position);

        holder.Username.setText(users.getUsername());

        String help=users.getImageUrl();
        Log.e(TAG, "onBindViewHolder: IMAGEURL= "+help );


        if((users.getImageUrl().equals("default"))){
            holder.dp.setImageResource(R.drawable.avatar);
        }
        else {

            Glide.with(context).load(users.getImageUrl()).into(holder.dp);
        }

        if(Ischat)
        {
            if(users.getStatus().equalsIgnoreCase("Online"))
            {
                holder.status.setText("Online");
            }
            else
            {
                holder.status.setText("Offline");
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, MessageActivity.class);
                i.putExtra("userid",users.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Musers1.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Username;
        public ImageView dp;
        public TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Username=itemView.findViewById(R.id.username);
            dp=itemView.findViewById(R.id.dp);
            status=itemView.findViewById(R.id.status);
        }
    }
}



