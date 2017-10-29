package com.chatapp.dmr.HolaShop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chatapp.dmr.HolaShop.Model.User;
import com.chatapp.dmr.HolaShop.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by dmr on 9/11/2017.
 */

public class FriendListAdapter extends ArrayAdapter<User> {
    Context con;
    int res;
    public FriendListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.con = context;
        this.res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if(convertView==null){
            LayoutInflater inflator=LayoutInflater.from(con);
            convertView=inflator.inflate(res,parent,false);
        }
        CircleImageView profile = (CircleImageView) convertView.findViewById(R.id.profile_image_in_friend_list);
        Log.e("Image",user.getProfileUrl());
        Picasso.with(convertView.getContext()).load(user.getProfileUrl()).fit().into(profile);
        TextView name = (TextView) convertView.findViewById(R.id.my_friend);
        name.setText(user.getName());
        TextView email = (TextView) convertView.findViewById(R.id.friend_email);
        email.setText(user.getEmail());
        TextView id = (TextView) convertView.findViewById(R.id.friend_id);
        id.setText(user.getId());
        TextView image = (TextView) convertView.findViewById(R.id.friend_image);
        image.setText(user.getProfileUrl());
        return convertView;
    }
}
