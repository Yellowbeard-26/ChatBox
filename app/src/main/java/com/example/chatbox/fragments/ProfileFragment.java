package com.example.chatbox.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chatbox.Model.Users;
import com.example.chatbox.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    TextView txtvw;
    ImageView imgView;

    DatabaseReference ref;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final int Image_REquest=1;
    private Uri imageuri;
    private StorageTask uploadtask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        imgView=view.findViewById(R.id.imageView2);
        txtvw=view.findViewById(R.id.textView5);

        storageReference= FirebaseStorage.getInstance().getReference("Uploads");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                txtvw.setText(users.getUsername());
                if((users.getImageUrl().equals("default"))){
                    imgView.setImageResource(R.drawable.avatar);
                }
                else {

                    Glide.with(getContext()).load(users.getImageUrl()).into(imgView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });





        return view;
    }

    public void selectimage()
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,Image_REquest);

    }
    private String getFileExention(Uri uri)
    {
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_REquest&&resultCode==RESULT_OK
        &&data!=null&&data.getData()!=null)
        {
            imageuri=data.getData();
            if(uploadtask!=null&& uploadtask.isInProgress())
            {
                Toast.makeText(getContext(),"Upload In Progress",Toast.LENGTH_SHORT).show();

            }
            else
            {
                UploadImage();
            }
        }

    }

    private void UploadImage()
    {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageuri!=null) {

            final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + getFileExention(imageuri));
            uploadtask = storageReference1.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),"khgdgxjh",Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                    return storageReference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        ref = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();

                        map.put("imageUrl", mUri);
                        ref.updateChildren(map);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "FAILED!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        }
        else
        {
            Toast.makeText(getContext(),"NO IMAGE SELECTED",Toast.LENGTH_SHORT).show();
        }


    }
}