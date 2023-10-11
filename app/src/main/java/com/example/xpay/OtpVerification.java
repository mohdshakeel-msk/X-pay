package com.example.xpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    EditText input1, input2, input3, input4, input5, input6;
    Button verify;
    TextView resendOTP;
    String enteredNumber;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String backEndOtp;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    PhoneAuthCredential credential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        input1 = findViewById(R.id.inputOpt1);
        input2 = findViewById(R.id.inputOpt2);
        input3 = findViewById(R.id.inputOpt3);
        input4 = findViewById(R.id.inputOpt4);
        input5 = findViewById(R.id.inputOpt5);
        input6 = findViewById(R.id.inputOpt6);
        verify = findViewById(R.id.verifOtp);
        resendOTP = findViewById(R.id.resendOtp);

        TextView showNumber = findViewById(R.id.text_input_number);
        showNumber.setText(String.format("+91-%s", getIntent().getStringExtra("mobile")));

        enteredNumber = getIntent().getExtras().getString("mobile");

        sendOpt("+91"+enteredNumber, false);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() &&
                        !input3.getText().toString().trim().isEmpty() && !input4.getText().toString().trim().isEmpty() &&
                        !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty()) {

                    // String concatination in Entered OTP by the User
                    String enteredOtp = input1.getText().toString() + input2.getText().toString() + input3.getText().toString() +
                            input4.getText().toString() + input5.getText().toString() + input6.getText().toString();

                    credential = PhoneAuthProvider.getCredential(backEndOtp, enteredOtp);
                    signIn(credential);
                    Toast.makeText(OtpVerification.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtpVerification.this, "Please Enter All 6 digit OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextInputBoxMove();

    }


    void sendOpt(String enteredNumber, boolean isResend){
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(enteredNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerification.this, "something error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);

                                backEndOtp = s;
                                resendingToken = forceResendingToken;
                                Toast.makeText(OtpVerification.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(OtpVerification.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void nextInputBoxMove() {
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input1.toString().trim().isEmpty()) {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input2.toString().trim().isEmpty()) {
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input3.toString().trim().isEmpty()) {
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input4.toString().trim().isEmpty()) {
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input5.toString().trim().isEmpty()) {
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}