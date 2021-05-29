package com.example.chatbox.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbox.Adapter.UserAdapter;
import com.example.chatbox.Model.Chatslists;
import com.example.chatbox.Model.Users;
import com.example.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    private UserAdapter userAdapter;
    private List<Users> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference2;
    private List<Chatslists> chatslistsList;
    private List<Chatslists> recieverlist;

    RecyclerView recyclerView2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView2 = view.findViewById(R.id.chatslist);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        chatslistsList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        Log.e(TAG, "onCreateView: refeerence =" + reference.toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatslistsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chatslists chatslists = snapshot1.getValue(Chatslists.class);
                    chatslistsList.add(chatslists);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recieverlist = new ArrayList<>();
        reference2 = FirebaseDatabase.getInstance().getReference("ChatList");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recieverlist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        Chatslists chatslists = snapshot2.getValue(Chatslists.class);
                        if (chatslists.getId().equals(firebaseUser.getUid())) {
                            recieverlist.add(chatslists);
                        }
                    }

                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users users = snapshot1.getValue(Users.class);
                    for (Chatslists chatslists : chatslistsList) {
                        recieverlist.remove(chatslists);
                        if (users.getId().equals(chatslists.getId())) {
                            mUsers.add(users);
                        }
                    }
                    for (Chatslists rec : recieverlist) {
                        if (users.getId().equals(rec.getSendid())) {
                            mUsers.add(users);
                        }
                    }
                    mUsers = new ArrayList<Users>(new LinkedHashSet<Users>(mUsers));
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView2.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
