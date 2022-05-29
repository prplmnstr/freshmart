package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class verify_activity extends AppCompatActivity {


    Button login_button;
    EditText OTP_edit_text;
    TextView resend_otp;

    String varificationcode;
    RelativeLayout relativeLayout1;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.verify_activity);



        login_button = findViewById(R.id.login_button);
        OTP_edit_text = findViewById(R.id.mobile_editTxt);
        resend_otp = findViewById(R.id.resend_otp);
        relativeLayout1 = findViewById(R.id.rv1);


        loader = new Dialog(verify_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;








        //get mobile number from login activity//
        final String mobile=getIntent().getStringExtra("mobile");
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendotp(mobile);
                Toast.makeText(verify_activity.this,"OTP Resent Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(verify_activity.this,"OTP sent successfully",Toast.LENGTH_SHORT).show();
        sendotp(mobile);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout1.setVisibility(View.INVISIBLE);
                loader.show();
                String userEnteredOTP = OTP_edit_text.getText().toString();
                if(userEnteredOTP.isEmpty()){
                    Toast.makeText(verify_activity.this,"Enter OTP",Toast.LENGTH_SHORT).show();
                       loader.dismiss();
                }
                else {
                    varifycode(userEnteredOTP);

                }
            }
        });


    }

    private void sendotp(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ mobile,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            varificationcode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){

                varifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(verify_activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
    private void varifycode(String codebyuser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(varificationcode , codebyuser);
        signInTheUser(credential);

    }

    private void signInTheUser(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(verify_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

//
                    Intent home = new Intent(getApplicationContext(), dashboard_activity.class);

                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    home.putExtra("skip","n");
                    startActivity(home);

                }
                else {
                    Toast.makeText(verify_activity.this," Wrong OTP, Please Enter Correct OTP",Toast.LENGTH_LONG).show();
                    loader.dismiss();
                }
            }
        });



    }
    }
