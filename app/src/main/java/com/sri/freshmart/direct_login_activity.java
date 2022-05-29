package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class direct_login_activity extends AppCompatActivity {


    EditText home_ID_input, Mobile_input;
    Button login_button;
    TextView new_user_text,skip;
    String x;
    int h=0;    //for checking homeid registered or not//

    //firebase//
    DatabaseReference validatePhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.direct_login_activity);


        Objects.requireNonNull(getSupportActionBar()).hide();
        Mobile_input = findViewById(R.id.mobile_editTxt);
        home_ID_input = findViewById(R.id.homeid_editText);
        login_button = findViewById(R.id.login_button);
        new_user_text =findViewById(R.id.resend_otp);
        skip = findViewById(R.id.skip);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(direct_login_activity.this,dashboard_activity.class);
                home.putExtra("skip","s");
                startActivity(home);
                finish();

            }
        });





        Mobile_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {

                    home_ID_input.setText("");
                }
                if(!b){
                    ismobile();
                }





            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String val1 =Mobile_input.getText().toString();
                String val2 = home_ID_input.getText().toString();

                if(val2.isEmpty()){
                    home_ID_input.setError("Please Enter Home ID");
                }
                else if (val1.length() != 10){
                    Mobile_input.setError("Please Enter Correct Mobile No");
                    home_ID_input.setText("");
                }
                else if(h==1){
                    //user registered

                    String mobile = val1;

                    Intent login = new Intent(direct_login_activity.this, verify_activity.class);
                    login.putExtra("mobile", mobile);


                    startActivity(login);
                    finish();
                }
                else{
                    Mobile_input.setError("Mobile Number Not Registered");
                    home_ID_input.setText("");
                }



            }
        });

        new_user_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_user = new Intent(direct_login_activity.this,login_activity.class);
                startActivity(new_user);
            }
        });


    }

    private void ismobile() {
        final String val = Mobile_input.getText().toString();
        validatePhone = FirebaseDatabase.getInstance().getReference("customers");
        Query validate= validatePhone.orderByChild("mobile").equalTo(val);

        validate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    h =1;


                } else {
                    h = 2;

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(direct_login_activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }



}
