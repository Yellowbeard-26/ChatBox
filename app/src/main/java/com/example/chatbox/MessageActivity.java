package com.example.chatbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatbox.Adapter.MessageAdapter;
import com.example.chatbox.Model.Chats;
import com.example.chatbox.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    TextView Username;
    ImageView displaypicture;
    RecyclerView chatscroll;
    EditText text;
    ImageButton sendbtn;
    Users user;


    FirebaseUser fuser;
    DatabaseReference ref;
    String userid;

    MessageAdapter messageAdapter;
    List<Chats> chatsList;

    RecyclerView recyclerView;
    Intent intent;
    ValueEventListener seenListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        displaypicture = (ImageView) findViewById(R.id.Displaypicture);
        Username = (TextView) findViewById(R.id.Name);

        text=(EditText) findViewById(R.id.txtsend);
        sendbtn=(ImageButton) findViewById(R.id.imageButton);

        recyclerView=(RecyclerView) findViewById(R.id.chatscroll);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent=getIntent();
        userid=intent.getStringExtra("userid");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users us=snapshot.getValue(Users.class);
                Username.setText(us.getUsername());


                if((us.getImageUrl().equals("default"))){
                    displaypicture.setImageResource(R.drawable.avatar);
                }
                else
                {
                    final Context context = getApplication().getApplicationContext();

                    if (isValidContextForGlide(context)){
                        // Load image via Glide lib using context
                        Glide.with(context).load(us.getImageUrl()).into(displaypicture);
                    }


                }

                readMessages(fuser.getUid(),userid,us.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text.getText().toString();
                if(!msg.equals(""))
                {
                    sendmessage(fuser.getUid(),userid,msg);
                }
                else
                {
                    Toast.makeText(MessageActivity.this, "Cannot Send An Empty Message", Toast.LENGTH_SHORT).show();
                }

                text.setText("");
            }
        });


        //SeenMessage(userid);
    }





//   // private void SeenMessage(String u)
//    {
//        ref=FirebaseDatabase.getInstance().getReference("Chats");
//
//        seenListener=ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1:snapshot.getChildren())
//                {
//                    Chats chats=snapshot1.getValue(Chats.class);
//                    if(chats.getReciever().equals(fuser.getUid())&&chats.getSender().equals(u))
//                    {
//                        HashMap<String,Object> h=new HashMap<>();
//                        h.put("isSeen",true);
//                        snapshot1.getRef().updateChildren(h);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void sendmessage(String Sender,String Reciever,String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats").push();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Sender",Sender);
        hashMap.put("Reciever",Reciever);
        hashMap.put("Message",message);
        //hashMap.put("isSeen",false);

        reference.setValue(hashMap);



        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList")
        .child(fuser.getUid()).child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                    chatRef.child("Sendid").setValue(fuser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void readMessages(String myid,String userid,String imageurl)
    {
        chatsList=new ArrayList<>();
        ref=FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatsList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Chats chats=snapshot1.getValue(Chats.class);

                    if(chats.getReciever().equals(myid)&&chats.getSender().equals(userid)||chats.getReciever().equals(userid)&&chats.getSender().equals(myid))
                    {
                        chatsList.add(chats);
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,chatsList,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkStatus(String Status)
    {
        ref=FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Status",Status);
        ref.updateChildren(hashMap);


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus("Online");
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //ref.removeEventListener(seenListener);
        checkStatus("Offline");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}
