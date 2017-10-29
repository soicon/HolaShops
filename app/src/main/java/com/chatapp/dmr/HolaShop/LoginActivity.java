package com.chatapp.dmr.HolaShop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";

    private Button loginBtn;
    private EditText emailText;
    private EditText passwordText;
    private TextView link_Signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.chatapp.dmr.HolaShop.R.layout.activity_logins);

        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn = (Button)findViewById(R.id.btn_login);
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText)findViewById(R.id.password);
        link_Signup = (TextView) findViewById(R.id.link_signup);

        progressDialog = new ProgressDialog(this);
        loginBtn.setOnClickListener(this);
        link_Signup.setOnClickListener(this);


    }


    private void LoginClick(){
        Log.d(TAG,"Login");
//        if(!validate()){
//            Toast.makeText(getBaseContext(),"Nhập Thông Tin Không Đúng.Hãy Thử Lại",Toast.LENGTH_LONG).show();
//            return;
//        }
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Đang xác thực...");
        progressDialog.show();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        (firebaseAuth.signInWithEmailAndPassword(email,password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Đăng Nhập Thành Công",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    @Override
    public void onClick(View v) {
        if(v == loginBtn){
            LoginClick();
        }
        if(v == link_Signup){

           startActivity(new Intent(this,SignUpActivity.class));
        }
    }
//    public boolean validate() {
//        boolean valid = true;
//
//        emailText = (EditText)findViewById(R.id.input_email);
//        passwordText = (EditText)findViewById(R.id.input_password);
//
//        String email = emailText.getText().toString();
//        String password = passwordText.getText().toString();
//
//
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10 || password == null) {
//            passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            passwordText.setError(null);
//        }
//
//        return valid;
//    }
}
