package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class cart_activity extends AppCompatActivity {

    DatabaseReference getItems,instant_status, place_order, take_order,empty_cart, check_cart, checkorder,delivery_status_db,get_date;

    private List<cart_item_model> cart_item_modelList;
    private cart_item_adaptor cartItemAdaptor;

    String homeid, id_, name_, qty_, price_, qty_number,icon1,mrp;
    public static String status="0",status_instant="0";
    ImageView orders;
    Dialog confirm_dialog,loader;
   public static RadioButton normal,instant;




    RecyclerView recyclerView;
    public static NestedScrollView scrollView;
    public static ConstraintLayout place_order_layout, empty_cart_layout;
    public static TextView price_amt, place_order_button ,discount_amt, total_amt, saving,order_notifier,delivery_charge,delivery_date_cart;
 public static   int no_order  = 0,no_order_instant  = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.cart_activity);




        recyclerView = findViewById(R.id.rv_cart);
        scrollView = findViewById(R.id.scrollView_cart);
        place_order_layout = findViewById(R.id.place_holder_layout);
        empty_cart_layout = findViewById(R.id.empty_cart_layout);
        recyclerView.setNestedScrollingEnabled(false);
        price_amt = findViewById(R.id.price_number);
        discount_amt = findViewById(R.id.discount_number);
        total_amt = findViewById(R.id.total_amt_number);
        delivery_date_cart = findViewById(R.id.delivery_date_cart);

        orders = findViewById(R.id.orders_img);
        delivery_charge = findViewById(R.id.delivery_charge_nbr);
        normal = findViewById(R.id.week_delivery);
        instant = findViewById(R.id.instant_delivey);
        place_order_button = findViewById(R.id.total_text2);


        saving = findViewById(R.id.saving_detail_text);
        order_notifier = findViewById(R.id.notify_order);

        homeid = dashboard_activity.id;



        char colony = homeid.charAt(0);
        if(colony != 'C'){
            TextView instant_d = findViewById(R.id.textView24);
            TextView instant_d_ = findViewById(R.id.textView27);
            instant_d.setVisibility(View.GONE);
            instant_d_.setVisibility(View.GONE);
            instant.setVisibility(View.GONE);

        }




        //loader
        loader = new Dialog(cart_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();


        //delivery_status

          get_delivery_date();
        delivery_status();



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        cart_item_modelList = new ArrayList<cart_item_model>();
        cartItemAdaptor = new cart_item_adaptor(cart_item_modelList);
        recyclerView.setAdapter(cartItemAdaptor);




        ////  get items   ////

        check_cart_empty_or_not();


        ////  get items   ////

        check_order();

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cart_activity.this, myOrders.class);
                startActivity(intent);
            }
        });


        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instant.setChecked(false);
                normal.setChecked(true);
                cartItemAdaptor.set_price_details();
            }
        });

        instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normal.setChecked(false);
                instant.setChecked(true);
               cartItemAdaptor.set_price_details();


            }
        });


        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
    }
    private void get_delivery_date() {
        String colony = homeid.replaceAll("[0-9]","").trim();
        get_date = FirebaseDatabase.getInstance().getReference("Dates").child(colony);
        get_date.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                delivery_date_cart.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void delivery_status_instant() {
        delivery_status_db  = FirebaseDatabase.getInstance().getReference("instant").child("orders").child(homeid);
        delivery_status_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                status_instant = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });    }


    private void delivery_status() {
        final String colony = homeid.replaceAll("[0-9]","").trim();

        delivery_status_db  = FirebaseDatabase.getInstance().getReference("Delivery").child(colony);
        delivery_status_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                status = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void check_cart_empty_or_not() {
        check_cart = FirebaseDatabase.getInstance().getReference("carts");
        Query check = check_cart.orderByKey().equalTo(homeid);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    getitems();
                    scrollView.setVisibility(View.VISIBLE);
                    place_order_layout.setVisibility(View.VISIBLE);

                } else {
                    empty_cart_layout.setVisibility(View.VISIBLE);
                }
                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void placeOrder() {

          if(normal.isChecked()) {
              if (status.equals("1")) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(cart_activity.this);
                  builder.setTitle("Sorry!")
                          .setMessage("The Normal Delivery process is already started in your area. New orders will be taken soon.Please try after sometime OR select Instant Delivery.")
                          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {

                              }
                          });
                  builder.create();
                  builder.show();

              } else {

                  confirm_dialog = new Dialog(cart_activity.this);
                  confirm_dialog.setContentView(R.layout.order_confirm_dialog);
                  confirm_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                  confirm_dialog.setCancelable(true);
                  confirm_dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                  confirm_dialog.show();
                  Button yes = confirm_dialog.findViewById(R.id.yes_button_order_dialog);
                  Button no = confirm_dialog.findViewById(R.id.no_button_order_dialog);

                  yes.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {


                          // toast  ////
                          LayoutInflater inflater = getLayoutInflater();
                          View layout = inflater.inflate(R.layout.success_toast, (ViewGroup) findViewById(R.id.successful_layout));

                          final Toast toast = new Toast(getApplicationContext());
                          toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                          toast.setDuration(Toast.LENGTH_LONG);
                          toast.setView(layout);
                          toast.show();

                          // toast  ////


                          take_order = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
                          place_order = FirebaseDatabase.getInstance().getReference("orders").child("current");

                          empty_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
                          take_order.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  for (DataSnapshot Data : snapshot.getChildren()) {
                                      id_ = Data.child("id").getValue().toString();
                                      name_ = Data.child("name").getValue().toString();
                                      qty_ = Data.child("unit").getValue().toString();
                                      price_ = Data.child("price").getValue().toString();
                                      qty_number = Data.child("unit_number").getValue().toString();
                                      icon1 = Data.child("icon1").getValue().toString();
                                      mrp = Data.child("mrp").getValue().toString();


                                      add_to_orders new_order = new add_to_orders(id_, name_, price_, qty_, qty_number, icon1, mrp);
                                      place_order.child(homeid).child(id_).setValue(new_order);


                                  }
                                  empty_cart.removeValue();

                                  new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          Intent intent = new Intent(cart_activity.this, myOrders.class);
                                          startActivity(intent);
                                      }
                                  }, 3000);


                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });

                          confirm_dialog.dismiss();
                      }
                  });

                  no.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          confirm_dialog.dismiss();
                      }
                  });


              }
          }else if(instant.isChecked()){
              if (status_instant.equals("1")) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(cart_activity.this);
                  builder.setTitle("Sorry!")
                          .setMessage("Your last instant order is already dispatched. You can place new order after the delivery.")
                          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {

                              }
                          });
                  builder.create();
                  builder.show();

              }else {
                  confirm_dialog = new Dialog(cart_activity.this);
                  confirm_dialog.setContentView(R.layout.order_confirm_dialog);
                  confirm_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                  confirm_dialog.setCancelable(true);
                  confirm_dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                  confirm_dialog.show();
                  Button yes = confirm_dialog.findViewById(R.id.yes_button_order_dialog);
                  Button no = confirm_dialog.findViewById(R.id.no_button_order_dialog);

                  yes.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {


                          // toast  ////


                          // toast  ////


                          take_order = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
                          place_order = FirebaseDatabase.getInstance().getReference("orders").child("instant");

                          empty_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
                          take_order.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  for (DataSnapshot Data : snapshot.getChildren()) {
                                      id_ = Data.child("id").getValue().toString();
                                      name_ = Data.child("name").getValue().toString();
                                      qty_ = Data.child("unit").getValue().toString();
                                      price_ = Data.child("price").getValue().toString();
                                      qty_number = Data.child("unit_number").getValue().toString();
                                      icon1 = Data.child("icon1").getValue().toString();
                                      mrp = Data.child("mrp").getValue().toString();


                                      add_to_orders new_order = new add_to_orders(id_, name_, price_, qty_, qty_number, icon1, mrp);
                                      place_order.child(homeid).child(id_).setValue(new_order);


                                  }

                                  instant_status = FirebaseDatabase.getInstance().getReference("instant").child("orders");
                                  instant_status.child(homeid).setValue("0");
                                  empty_cart.removeValue();



                                          Intent intent = new Intent(cart_activity.this, myOrders.class);
                                          startActivity(intent);


                                  LayoutInflater inflater = getLayoutInflater();
                                  View layout_ = inflater.inflate(R.layout.success_toast, (ViewGroup) findViewById(R.id.successful_layout));

                                  final Toast toast = new Toast(getApplicationContext());
                                  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                  toast.setDuration(Toast.LENGTH_LONG);
                                  toast.setView(layout_);
                                  toast.show();
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });

                          confirm_dialog.dismiss();
                      }
                  });

                  no.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          confirm_dialog.dismiss();
                      }
                  });

              }

          }

    }

    public void back(View view) {

        finish();
    }


    private void getitems() {

        getItems = FirebaseDatabase.getInstance().getReference("carts").child(homeid);


        getItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Data : snapshot.getChildren()) {
                    cart_item_modelList.add(new cart_item_model(Data.child("icon1").getValue().toString(), Data.child("name").getValue().toString(), Data.child("price").getValue().toString(), Data.child("mrp").getValue().toString(), Data.child("unit").getValue().toString(), Data.child("unit_number").getValue().toString(), Data.child("id").getValue().toString()));

                }
                cartItemAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void check_order() {
        checkorder = FirebaseDatabase.getInstance().getReference("orders");
        Query query = checkorder.child("current").orderByKey().equalTo(homeid);
        Query query_instant = checkorder.child("instant").orderByKey().equalTo(homeid);

        query_instant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    no_order_instant = 1;
                    delivery_status_instant();



                }
                else{

                    no_order_instant = 0;
                }
                set_notifier();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                no_order = 1;



                }
                else{
                    no_order = 0;
                }
                set_notifier();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void set_notifier() {
        if(no_order==1 || no_order_instant==1){
            order_notifier.setVisibility(View.VISIBLE);


        }else{
            order_notifier.setVisibility(View.INVISIBLE);

        }
    }

}