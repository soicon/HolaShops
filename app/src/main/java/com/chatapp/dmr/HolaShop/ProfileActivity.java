package com.chatapp.dmr.HolaShop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chatapp.dmr.HolaShop.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Uri imageUri = Uri.EMPTY;
    private static final int GALLERY_INTENT = 2;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        String userName = firebaseAuth.getCurrentUser().getDisplayName();
        TextView name = (TextView)findViewById(R.id.user_profile_name);
        CircleImageView profile = (CircleImageView)findViewById(R.id.profile_image);
        name.setText(userName);
        TextView changeProfile = (TextView) findViewById(R.id.change_profile);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        Picasso.with(ProfileActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).fit().into(profile);


    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/^");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            imageUri = data.getData();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User_Profile").child(imageUri.getLastPathSegment());
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang Thay Đổi...");
            progressDialog.show();
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    UserProfileChangeRequest userProfileChangeRequest  = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri)
                            .build();
                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest);
                    FirebaseUser  user =  FirebaseAuth.getInstance().getCurrentUser();
                    User temp = new User(user.getUid(),user.getDisplayName(),user.getEmail(),imageUri.getLastPathSegment());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(temp);
                    progressDialog.dismiss();
                    CircleImageView profile = (CircleImageView)findViewById(R.id.profile_image);
                    Picasso.with(ProfileActivity.this).invalidate(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                    Picasso.with(ProfileActivity.this).load(downloadUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(profile);

                }
            });

        }

    }
}
