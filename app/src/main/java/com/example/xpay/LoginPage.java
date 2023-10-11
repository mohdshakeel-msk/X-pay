package com.example.xpay;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    EditText enteredNumber;
    Button getOTP;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        enteredNumber = findViewById(R.id.input_number);
        getOTP = findViewById(R.id.buttongetotp);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ProgressBar progressBar = findViewById(R.id.progressBar_getOtp);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!enteredNumber.getText().toString().trim().isEmpty()){
                    if ((enteredNumber.getText().toString().trim()).length() == 10){

                        progressBar.setVisibility(view.VISIBLE);
                        getOTP.setVisibility(view.INVISIBLE);

                        Intent intent = new Intent(getApplicationContext(),OtpVerification.class);
                        intent.putExtra("mobile", enteredNumber.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginPage.this,"Please Enter 10 digit Correct number",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginPage.this,"Please enter your Number",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}