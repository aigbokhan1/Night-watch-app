package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText EmailId;
    private EditText Pass;
    private Button btnReg;
    private TextView mSignin;
    private ProgressDialog Dialog;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Auth = FirebaseAuth.getInstance();
        Dialog = new ProgressDialog(this);
//        checkConnection();
        registration();
    }

//    public void checkConnection(){
//        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
//        if (null!=activeNetwork){
//            if (activeNetwork.getType()== ConnectivityManager.TYPE_WIFI){
//                Toast.makeText(getApplicationContext(),"WIFI ENABLED.", Toast.LENGTH_LONG).show();
//            }
//            else if (activeNetwork.getType()== ConnectivityManager.TYPE_MOBILE){
//                Toast.makeText(getApplicationContext(),"DATA NETWORK ENABLED.", Toast.LENGTH_LONG).show();
//            }
//            else{
//                Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
//            }
//
//        }
//    }

    private void registration() {

        EmailId = findViewById(R.id.email_reg);
        Pass = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_reg);
        mSignin = findViewById(R.id.signin_here);

        btnReg.setOnClickListener(new View.OnClickListener() {
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
                }
                Dialog.setMessage("Processing");
                Dialog.show();

                Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registation Completed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Failed", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
