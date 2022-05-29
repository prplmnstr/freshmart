package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class myOrders extends AppCompatActivity {


    String homeid,charge,charge_normal;
Dialog dialog,cancel_dialog,loader;


    List<order_details_model> order_details_models;
    order_details_rv_adopter orderDetailsRvAdopter;

    DatabaseReference get_current_orders, get_date,order_detail_db,delete_order,charge_db;


    TextView rupee1,rupee1_instant, date,no_orders,status_text,status_text_instant,charge_text,charge_text_normal;

    Button order_details,order_details_instant,cancel,cancel_instant;

    ConstraintLayout main,instant_layout;
    String status = cart_activity.status;



    private int r = 0,r_instant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);
        Objects.requireNonNull(getSupportActionBar()).hide();


        rupee1 = findViewById(R.id.rupee_text);
        rupee1_instant = findViewById(R.id.rupee_text_instant);
         homeid =  dashboard_activity.id;
        date = findViewById(R.id.date_text);

        cancel = findViewById(R.id.cancel_btn);
        cancel_instant = findViewById(R.id.cancel_btn_instant);
       order_details= findViewById(R.id.show_details);
       order_details_instant= findViewById(R.id.show_details_instant);
       no_orders  =findViewById(R.id.no_order_txt);
       status_text = findViewById(R.id.status_text);
       status_text_instant = findViewById(R.id.status_text_instant);
       instant_layout = findViewById(R.id.instant_order_layout);


       ////// instant details
       charge_text = findViewById(R.id.textView21_instant);
        charge_text_normal = findViewById(R.id.textView21);

        main = findViewById(R.id.main_layout);

        loader = new Dialog(myOrders.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();



        if(status.equals("1")){
            status_text.setText("Dispatched");
        }else{
            status_text.setText("Pending");
        }




        if(cart_activity.no_order !=1 && cart_activity.no_order_instant!=1) {


            main.setVisibility(View.GONE);
            instant_layout.setVisibility(View.GONE);
            no_orders.setVisibility(View.VISIBLE);
            loader.dismiss();

        }else if(cart_activity.no_order==1 && cart_activity.no_order_instant!=1) {
            main.setVisibility(View.VISIBLE);
            instant_layout.setVisibility(View.GONE);

            no_orders.setVisibility(View.INVISIBLE);


            get_delivery_date();
            check_charge_normal();


        }else if(cart_activity.no_order!=1 && cart_activity.no_order_instant==1){
            main.setVisibility(View.GONE);
            instant_layout.setVisibility(View.VISIBLE);
            check_charge();

        }else{
            no_orders.setVisibility(View.INVISIBLE);

            main.setVisibility(View.VISIBLE);
            instant_layout.setVisibility(View.VISIBLE);
            check_charge();
            get_delivery_date();
            check_charge_normal();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               show_dialog("current");

            }
        });

       cancel_instant.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               show_dialog("instant");
           }
       });











        order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               show_order_details("current");
            }
        });
        order_details_instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_order_details("instant");
            }
        });

    }

    private void check_charge_normal() {
        final String colony = homeid.replaceAll("[0-9]","").trim();
        charge_db = FirebaseDatabase.getInstance().getReference("charges");
        charge_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                charge_normal = snapshot.child(colony).getValue().toString();

                get_order_details2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_order_details(String which) {

        dialog = new Dialog(myOrders.this);
        dialog.setContentView(R.layout.order_details_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        RecyclerView recyclerView = dialog.findViewById(R.id.order_details_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        order_details_models = new ArrayList<order_details_model>();
        orderDetailsRvAdopter = new order_details_rv_adopter(order_details_models);
        recyclerView.setAdapter(orderDetailsRvAdopter);


        order_detail_db = FirebaseDatabase.getInstance().getReference("orders").child(which).child(homeid);
        order_detail_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Data : snapshot.getChildren()) {
                    order_details_models.add(new order_details_model(Data.child("icon1").getValue().toString(), Data.child("name").getValue().toString(), Data.child("price").getValue().toString(),  Data.child("qty").getValue().toString()));

                }
                orderDetailsRvAdopter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.show();
    }


    private void show_dialog(final String which) {
        cancel_dialog = new Dialog(myOrders.this);
        cancel_dialog.setContentView(R.layout.confirm_dialog);
        cancel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        cancel_dialog.setCancelable(true);
        cancel_dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        cancel_dialog.show();
        Button yes = cancel_dialog.findViewById(R.id.yes_button_chancel_dialog);
        Button no = cancel_dialog.findViewById(R.id.no_button_cancel_dialog);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference delete_from_instant_status = FirebaseDatabase.getInstance().getReference("instant").child("orders").child(homeid);
                delete_from_instant_status.setValue("");
                delete_order = FirebaseDatabase.getInstance().getReference("orders").child(which).child(homeid);
                delete_order.setValue(null);

                Toast.makeText(myOrders.this,"Odrer cancelled",Toast.LENGTH_LONG).show();
                cancel_dialog.dismiss();
                if(which.equals("current")) {
                    main.setVisibility(View.GONE);
                    if (instant_layout.getVisibility() == View.VISIBLE) {
                        no_orders.setVisibility(View.INVISIBLE);
                    } else {
                        no_orders.setVisibility(View.VISIBLE);

                    }

                }else if(which.equals("instant")){
                    instant_layout.setVisibility(View.GONE);
                    if(main.getVisibility()==View.VISIBLE){
                        no_orders.setVisibility(View.INVISIBLE);
                    }else {
                        no_orders.setVisibility(View.VISIBLE);
                }


                }

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_dialog.dismiss();
            }
        });

    }

    private void check_charge() {
        charge_db = FirebaseDatabase.getInstance().getReference("charges");
        charge_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                charge = snapshot.child("instant").getValue().toString();
               get_order_details_instant();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void get_order_details_instant() {
        get_current_orders = FirebaseDatabase.getInstance().getReference("orders").child("instant").child(homeid);
        get_current_orders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Data : snapshot.getChildren()) {
                    r_instant = r_instant + (Integer.parseInt(Data.child("price").getValue().toString())) * Integer.parseInt(Data.child("qty").getValue().toString());
                }
                rupee1_instant.setText("₹" + String.valueOf(r_instant+Integer.parseInt(charge)));
                charge_text.setText("₹" +charge);
              check_status_instant();





                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void check_status_instant() {
        DatabaseReference statusdb= FirebaseDatabase.getInstance().getReference("instant").child("orders").child(homeid);
        statusdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("1")){
                    status_text_instant.setText("Dispatched");
                }else{
                    status_text_instant.setText("Pending");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void get_delivery_date() {
        String colony = homeid.replaceAll("[0-9]","").trim();
        get_date = FirebaseDatabase.getInstance().getReference("Dates").child(colony);
        get_date.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void get_order_details2() {
        get_current_orders = FirebaseDatabase.getInstance().getReference("orders").child("current").child(homeid);
        get_current_orders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Data : snapshot.getChildren()) {
                    r = r + (Integer.parseInt(Data.child("price").getValue().toString())) * Integer.parseInt(Data.child("qty").getValue().toString());
                }
                if(charge_normal.equals("0")){
                    charge_text_normal.setText("FREE");
                    rupee1.setText("₹" + String.valueOf(r));
                }else
                {
                    charge_text_normal.setText("₹" +charge_normal);
                    rupee1.setText("₹" + String.valueOf(r+Integer.parseInt(charge_normal)));
                }





               loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void back(View view) {
        finish();
    }



}