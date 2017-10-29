package com.chatapp.dmr.HolaShop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;


import com.chatapp.dmr.HolaShop.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup(){
        if(!validate()){
            Toast.makeText(getBaseContext(),"Nhập Thông Tin Không Đúng.Hãy Thử Lại",Toast.LENGTH_LONG).show();
            return;
        }
        nameText = (EditText)findViewById(R.id.input_name);
        emailText = (EditText)findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Đang Tạo Tài Khoản");
        progressDialog.show();
        (firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(!task.isSuccessful()){
                    Log.e("ERROR",task.getException().getMessage());
                    Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }else{
                    createNewUser(task.getResult().getUser());
                    Toast.makeText(SignUpActivity.this,"Đăng Ký Thành Công",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void createNewUser(FirebaseUser user){
        String username = nameText.getText().toString();
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/HolaShop-45294.appspot.com/o/blank-profile-picture-973461_960_720.png?alt=media&token=adb981be-b017-4117-99e5-fb1c52f45ba1");
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(uri)
                .build();
        List<User> list = new ArrayList<>();
        User newUser = new User(user.getUid(),username,user.getEmail(),uri.toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(newUser);
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("friend_list").setValue(list);
        user.updateProfile(profileUpdate);
    }
    public boolean validate() {
        boolean valid = true;

        emailText = (EditText)findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
