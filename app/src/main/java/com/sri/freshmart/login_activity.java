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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class login_activity extends AppCompatActivity {


    EditText home_ID_input, Mobile_input,name_input;
    Button login_button;
    TextView new_user_text,skip;
    String homeID;
    int h=0;    //for checking homeid registered or not//

    //firebase//
    DatabaseReference reference,validatePhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login_activity);


        Objects.requireNonNull(getSupportActionBar()).hide();
        Mobile_input = findViewById(R.id.mobile_editTxt);
        home_ID_input = findViewById(R.id.homeid_editText);
        login_button = findViewById(R.id.login_button);
        new_user_text =findViewById(R.id.resend_otp);
        skip = findViewById(R.id.skip);
        name_input = findViewById(R.id.name_editText);


       skip.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent home = new Intent(login_activity.this,dashboard_activity.class);
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
                String val3 = name_input.getText().toString();



                if(val3.isEmpty()){
                    name_input.setError("Please Enter Name");

                }
              else  if(val2.isEmpty()){
                    home_ID_input.setError("Please Enter Home Address");
                }
                else if (val1.length() != 10){
                    Mobile_input.setError("Please Enter Correct Mobile No");

                }else if(h==1){
                    Mobile_input.setError("Mobile Number Already Taken");
                    home_ID_input.setText("");
                }
                else {
                    //user registered

                    get_home_id(val3,val1,val2);

                    String mobile = val1;

                    Intent login = new Intent(login_activity.this, verify_activity.class);
                    login.putExtra("mobile", mobile);


                    startActivity(login);
                    finish();
                }




            }
        });

        new_user_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_user = new Intent(login_activity.this,new_user_activity.class);
                startActivity(new_user);
            }
        });


     }

    private void get_home_id(final String name, final String mobile, final String address) {
        DatabaseReference hm = FirebaseDatabase.getInstance().getReference("homeid");
        hm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              homeID=snapshot.child("CKDA").getValue().toString();
              register_user(name,mobile,"CKDA"+homeID,address);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void register_user(String name_,String phone_,String homeid_,String adress_) {

        user_register register = new user_register(name_,phone_,homeid_,adress_);
        reference = FirebaseDatabase.getInstance().getReference("customers");
        reference.child("+91"+phone_).setValue(register).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(login_activity.this,"Registration Successful",Toast.LENGTH_LONG).show();
            }
        });
        update_home_id();

        customer_register new_customer  = new customer_register(name_,phone_,homeid_,adress_,"1","2");
        FirebaseFirestore.getInstance().collection("customers").document(homeid_).set(new_customer);

    }

    private void update_home_id() {

        Integer s = Integer.parseInt(homeID)+1;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("homeid");
        db.child("CKDA").setValue(String.valueOf(s));
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
                Toast.makeText(login_activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }



    }
