package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText EmailId;
    private EditText Pass;
    private Button btnLogin;
    private TextView ForgetPassword;
    private TextView Signup;
    private ProgressDialog Dialog;
    private FirebaseAuth Auth;
    private Object view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Auth = FirebaseAuth.getInstance();
        Dialog = new ProgressDialog(this);

        if (checkConnection()) {
            Log.i("TRUE", "Connected");
            Toast.makeText(getApplicationContext(), "Network Available", Toast.LENGTH_LONG).show();
        } else {
            Log.i("TRUE", "NO Network Available");
            Toast.makeText(getApplicationContext(), "NO Network Available", Toast.LENGTH_LONG).show();
        }

//        if (Auth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//        }

        loginDetails();
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void loginDetails() {
        EmailId = findViewById(R.id.email_login);
        Pass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        ForgetPassword = findViewById(R.id.forget_password);
        Signup = findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailId.getText().toString().trim();
                String pass = Pass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    EmailId.setError("Email Required..");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Pass.setError("Password Required..");
                    return;
                }
                Dialog.setMessage("Processing..");
                Dialog.show();

                Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Login Successful..", Toast.LENGTH_SHORT).show();
                        } else {
                            Dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        // Registration Activity
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        // Reset Activity
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });

    }


}

