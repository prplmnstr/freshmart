package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class new_user_activity extends AppCompatActivity {


    ImageView call,whatsapp;
    DatabaseReference get_number;
    String cl,wtp;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_activity);
        Objects.requireNonNull(getSupportActionBar()).hide();


      call = findViewById(R.id.call_img);
      whatsapp  = findViewById(R.id.whatsapp_img);

      get_numbers();

      call.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent dial = new Intent(Intent.ACTION_DIAL);
              dial.setData(Uri.parse("tel:"+cl));
              startActivity(dial);
          }
      });

      whatsapp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              boolean installed = isappinstalled("com.whatsapp");

              if(installed){
                  Intent whatsapp= new Intent(Intent.ACTION_VIEW);
                  whatsapp.setData(Uri.parse("http://api.whatsapp.com/send?phone="+wtp));
                  startActivity(whatsapp);
              }else {
                  Toast.makeText(new_user_activity.this,"Whatsapp Not Installed",Toast.LENGTH_LONG).show();
              }
          }
      });

        loader = new Dialog(new_user_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();

    }

    private boolean isappinstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try{
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }

    private void get_numbers() {

        get_number = FirebaseDatabase.getInstance().getReference("contact");
        get_number.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cl  = snapshot.child("call").getValue().toString();
                wtp = snapshot.child("whatsapp").getValue().toString();
                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}