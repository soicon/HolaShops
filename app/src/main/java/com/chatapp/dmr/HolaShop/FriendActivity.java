package com.chatapp.dmr.HolaShop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chatapp.dmr.HolaShop.Adapter.FriendListAdapter;
import com.chatapp.dmr.HolaShop.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        final ListView lv = (ListView)findViewById(R.id.friend_list);

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<User> userList = new ArrayList<>();
                for (DataSnapshot friend : dataSnapshot.getChildren()) {

                    Log.v("MyKey",""+ friend.getKey()); //displays the key for the node
                    User user = friend.getValue(User.class);
                    Log.v("MyKey",""+user);
                    userList.add(friend.getValue(User.class));
                    //User user = new User(childDataSnapshot.child("name").getValue().toString(),childDataSnapshot.child("email").getValue().toString());
                }
                FriendListAdapter friendListAdapter  = new FriendListAdapter(FriendActivity.this,R.layout.feed,userList);
                //ArrayAdapter<String> arrayAdapter  = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,userList);
                lv.setAdapter(friendListAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), MessageActivity.class);
                        String receiver = ((TextView)view.findViewById(R.id.friend_email)).getText().toString();
                        String receiverName = ((TextView)view.findViewById(R.id.my_friend)).getText().toString();
                        String userID = ((TextView)view.findViewById(R.id.friend_id)).getText().toString();
                        String userImage = ((TextView)view.findViewById(R.id.friend_image)).getText().toString();
                        Log.v("EMAIL",receiver);
                        intent.putExtra("RECEIVER_EMAIL",receiver);
                        intent.putExtra("RECEIVER_NAME",receiverName);
                        intent.putExtra("RECEIVER_ID",userID);
                        intent.putExtra("RECEIVER_IMAGE",userImage);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }

}
