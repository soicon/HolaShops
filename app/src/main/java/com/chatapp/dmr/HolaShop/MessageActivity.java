package com.chatapp.dmr.HolaShop;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;


import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chatapp.dmr.HolaShop.Adapter.FriendListAdapter;
import com.chatapp.dmr.HolaShop.Adapter.MessageListAdapter;
import com.chatapp.dmr.HolaShop.Model.Message;

import com.chatapp.dmr.HolaShop.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MessageActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT = 2;
    Uri imageURI = Uri.EMPTY;
    private MessageListAdapter mAdapter;
    private String receiver;
    private String userId;
    private String keyData;
    private String receiverUrl;
    EmojiconEditText emojiconEditText;
    EmojIconActions emojIconActions;
    private ProgressDialog progressDialog;
    ImageView emojButton,submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);

        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        receiver = intent.getStringExtra("RECEIVER_EMAIL");
        userId = intent.getStringExtra("RECEIVER_ID");
        final String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String receiverName = intent.getStringExtra("RECEIVER_NAME");
        final String receiverImage = intent.getStringExtra("RECEIVER_IMAGE");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        keyData= senderId+"-TO-"+userId;
        Log.e("OLD_Keydata",keyData);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(keyData)) {
                    keyData = userId+"-TO-"+senderId;
                    Log.e("Temp_Keydata",keyData);
                    mAdapter = new MessageListAdapter(MessageActivity.this,Message.class,R.layout.message_in,FirebaseDatabase.getInstance().getReference().child(keyData),receiver,receiverImage);
                    ListView messages = (ListView) findViewById(R.id.listview_message_list);
                    messages.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.icons_choose).setVisibility(View.INVISIBLE);
            }
        });

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View v = getSupportActionBar().getCustomView();
        TextView title = (TextView) v.findViewById(R.id.custom_action_bar_title);
        title.setText(receiverName);
        // Apply the layout parameters to TextView widget



        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findViewById(R.id.icons_choose).getVisibility() == View.INVISIBLE){
                    findViewById(R.id.icons_choose).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.icons_choose).setVisibility(View.INVISIBLE);
                }
            }
        });

        LinearLayout icon = (LinearLayout)findViewById(R.id.icons_choose);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView photo_btn = (ImageView)findViewById(R.id.add_photo_button);
                photo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPhoto();
                    }
                });
            }
        });


//        ImageView camera_btn = (ImageView)findViewById(R.id.camera_photo_button);
//        photo_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callCamera();
//            }
//        });


        Button button = (Button)findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        emojButton = (ImageView) findViewById(R.id.emoj_button);
        submitButton = (ImageView) findViewById(R.id.emoj_send);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emoj_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),relativeLayout,emojiconEditText,emojButton);
        emojIconActions.ShowEmojIcon();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                FirebaseDatabase.getInstance().getReference().child(keyData).push().setValue(
                        new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                receiver,
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                emojiconEditText.getText().toString(),
                                dateFormat.format(date)));
                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
            }
        });
        Log.e("New_Keydata",keyData);
        mAdapter = new MessageListAdapter(MessageActivity.this,Message.class,R.layout.message_in,FirebaseDatabase.getInstance().getReference().child(keyData),receiver,receiverImage);
        ListView messages = (ListView) findViewById(R.id.listview_message_list);
        messages.setAdapter(mAdapter);
    }

    private void callCamera(){

    }



    private void addPhoto(){
        Log.e("PHOTO","IN PROCESS");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("ACTIVITY","COMMING IN");
        Log.d("LOGGED", " requestCode : " + requestCode+" resultCode : " + resultCode+" DATA "+data);
        if(requestCode == GALLERY_INTENT && resultCode ==  RESULT_OK){
            Log.d("LOGGED", "starting upload");
            imageURI = data.getData();
            StorageReference firePath = FirebaseStorage.getInstance().getReference().child("Chat_images").child(imageURI.getLastPathSegment());
            progressDialog.setMessage("Đang tải lên....");
            progressDialog.show();
            firePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Uri dowloadUri = taskSnapshot.getDownloadUrl();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    Message tempMess=  new Message(
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                            receiver,
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            emojiconEditText.getText().toString(),
                            dowloadUri.toString(),
                            dateFormat.format(date));
                    FirebaseDatabase.getInstance().getReference().child(keyData).push().setValue(tempMess);
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void checkPermission(){

    }
}
