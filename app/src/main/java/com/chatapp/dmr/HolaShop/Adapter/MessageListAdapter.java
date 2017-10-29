package com.chatapp.dmr.HolaShop.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.chatapp.dmr.HolaShop.FriendActivity;
import com.chatapp.dmr.HolaShop.LoginActivity;
import com.chatapp.dmr.HolaShop.MessageActivity;
import com.chatapp.dmr.HolaShop.Model.Message;
import com.chatapp.dmr.HolaShop.Model.User;
import com.chatapp.dmr.HolaShop.ProfileActivity;
import com.chatapp.dmr.HolaShop.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by dmr on 9/8/2017.
 */

public class MessageListAdapter extends FirebaseListAdapter<Message> {

    private MessageActivity messageActivity;
    FirebaseAuth firebaseAuth;
    String receiver;
    String imageUrls;
    private String receiverUrl;


    public MessageListAdapter(MessageActivity activity, Class<Message> modelClass, @LayoutRes int modelLayout, Query query, String Receiver,String imageUrl) {
        super(activity, modelClass, modelLayout, query);
        this.messageActivity = activity;
        receiver = Receiver;
        imageUrls = imageUrl;

    }

    @Override
    protected void populateView(View v, Message model, int position) {

        EmojiconTextView messages = (EmojiconTextView) v.findViewById(R.id.message_text);



        messages.setText(model.getMessage());
        ImageView imgView = (ImageView) v.findViewById(R.id.picture_message);

        Glide.with(v.getContext())
                .load(model.getImage())
                .into(imgView);




    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Message messages = getItem(position);
        Log.e("link image", "" + imageUrls);
        String uid = firebaseAuth.getInstance().getCurrentUser().getEmail();
        if (messages.getUserID() != null) {

            if (messages.getSenderEmail().equals(uid)) {
                view = messageActivity.getLayoutInflater().inflate(R.layout.message_out, viewGroup, false);
                ImageView profile = (ImageView) view.findViewById(R.id.profile_image_message);
                Picasso.with(view.getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).
                        networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(profile);


            } else{
                view = messageActivity.getLayoutInflater().inflate(R.layout.message_in, viewGroup, false);
                ImageView profile = (ImageView) view.findViewById(R.id.profile_image_message_in);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User_Profile").child(imageUrls);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        receiverUrl = uri.toString(); /// The string(file link) that you need
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                Picasso.with(view.getContext()).load(receiverUrl).
                        networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(profile);
            }

            populateView(view, messages, position);
            return view;

        } else {
            view = messageActivity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            return view;
        }


    }


}

