package com.example.chatbox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatbox.Model.Chats;
import com.example.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = "UserAdapter";
    private Context context;
    private List<Chats> Mchats;
    private String imgurl;
    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    public MessageAdapter(Context context, List<Chats> mChat,String img)
    {
        this.context = context;
        Mchats = mChat;
        imgurl=img;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chats chats=Mchats.get(position);

        holder.Showmessage.setText(chats.getMessage());

        if((imgurl.equals("default"))){
            holder.ProfileImage.setImageResource(R.drawable.avatar);
        }
        else {

            Glide.with(context).load(imgurl).into(holder.ProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return Mchats.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Showmessage;
        public ImageView ProfileImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Showmessage=itemView.findViewById(R.id.showMessage);
            ProfileImage=itemView.findViewById(R.id.profilepic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(Mchats.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}



