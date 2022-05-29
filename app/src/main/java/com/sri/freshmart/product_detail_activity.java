package com.sri.freshmart;


////// Takes input only product id


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class product_detail_activity extends AppCompatActivity {

    ImageSlider imageSlider;
    RecyclerView rv_features,rv_highlights;

    private features_adaptor featuresAdaptor;
    private highlights_adaptor highlightsAdaptor;

  private List<features_model> features_modelList;
  private List<highlights_model> highlights_modelList;


  Dialog dialog,loader;
  NestedScrollView scrollView;




    TextView product_name,home_id,product_price,product_mrp,product_unit,unit_number,cart_item_count,description,brand,discount;
    Button add,plus,minus;

    DatabaseReference product,feature,highlight,checkcart,items_in_cart,check_item_in_cart;
    DatabaseReference   cart = FirebaseDatabase.getInstance().getReference("carts");
    String id ,homeID = dashboard_activity.id, icon1,mrp,price ,number;
    private long itemCount;

    private float discount_D;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.product_detail_activity);
         id   = getIntent().getStringExtra("id");
         check_internet();
        check_cart();


        check_item_in_cart1(id);

        loader = new Dialog(product_detail_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();

        product_name = findViewById(R.id.product_name);
        brand = findViewById(R.id.brand_text);
        description = findViewById(R.id.description_text);
        cart_item_count = findViewById(R.id.item_count_product_D);
        product_mrp = findViewById(R.id.product_mrp_text);
        product_mrp.setPaintFlags(product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        product_price = findViewById(R.id.product_price_text);
        product_unit = findViewById(R.id.product_unit_text);
        add =  findViewById(R.id.product_add_button);
        plus = findViewById(R.id.plus_btn);
        minus = findViewById(R.id.minus_btn);
        unit_number = findViewById(R.id.unit_nbr);
        discount = findViewById(R.id.product_discount_text);
        scrollView = findViewById(R.id.scroll_view);
        home_id = findViewById(R.id.home_id_txt);
        home_id.setText(homeID);



        get_product();
        getFeatures();
        getHighlights();

        // on add button click  //


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id.charAt(0)!='g'){
                    plus.setEnabled(false);
                }
                if(dashboard_activity.id.equals("CKDA00")){
                    Toast toast = Toast.makeText(product_detail_activity.this,"Please Login",Toast.LENGTH_SHORT);
                    toast.show();
                    Intent home = new Intent(product_detail_activity.this,login_activity.class);

                    startActivity(home);
                    finish();
                }else{
                    Toast toast = Toast.makeText(product_detail_activity.this,"item added to cart",Toast.LENGTH_SHORT);
                    toast.show();
                    add.setVisibility(View.INVISIBLE);
                    plus.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.VISIBLE);
                    unit_number.setText("1");
                    unit_number.setVisibility(View.VISIBLE);
                    update_to_cart();
                }

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int x = Integer.parseInt(unit_number.getText().toString());
                unit_number.setText(String.valueOf(x+1));
                update_to_cart();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x = Integer.parseInt(unit_number.getText().toString());
                if(x==1) {
                    remove_from_cart();
                    Toast toast = Toast.makeText(product_detail_activity.this,"item removed from cart",Toast.LENGTH_SHORT);
                    toast.show();
                  add.setVisibility(View.VISIBLE);
                   plus.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    unit_number.setVisibility(View.INVISIBLE);
                }
                else{

                  unit_number.setText(String.valueOf(x-1));
                    update_to_cart();

                }
            }
        });

        // on add button click  //

    }
    private void check_internet() {
        dialog = new Dialog(product_detail_activity.this);
        dialog.setContentView(R.layout.no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;


        Button connect = dialog.findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.dismiss();
                recreate();

            }
        });



        if(!connected(product_detail_activity.this)){
            dialog.show();
            loader.dismiss();




        }

    }

    private boolean connected(product_detail_activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    private void check_item_in_cart1(final String itemID) {
        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts");
        Query query = check_item_in_cart.orderByKey().equalTo(homeID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check_item_in_cart2(itemID);
                }else{
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check_item_in_cart2(final String itemid) {
        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeID);
        Query query = check_item_in_cart.orderByKey().equalTo(itemid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check_item_in_cart3(itemid);
                    add.setVisibility(View.INVISIBLE);
                    plus.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.VISIBLE);

                    unit_number.setVisibility(View.VISIBLE);
                }else {
                    add.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);

                    unit_number.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check_item_in_cart3(String itemid) {
        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeID).child(itemid);
        check_item_in_cart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number = snapshot.child("unit_number").getValue().toString();
                unit_number.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void remove_from_cart() {
        cart.child(homeID).child(id).removeValue();
    }


    private void update_to_cart() {

        String name,unit_n,unit;


        unit_n = unit_number.getText().toString();
         unit = product_unit.getText().toString();
        name = product_name.getText().toString();

        add_to_cart item = new add_to_cart(mrp,price,name,icon1,unit_n,unit,id);;
        cart.child(homeID).child(id).setValue(item);



    }

    private void getFeatures() {
        rv_features = findViewById(R.id.rv_features);
        rv_features.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_features.setLayoutManager(layoutManager);

        features_modelList = new ArrayList<features_model>();
        featuresAdaptor = new features_adaptor(features_modelList);
        rv_features.setAdapter(featuresAdaptor);

        feature = FirebaseDatabase.getInstance().getReference("global_items").child(id).child("features");
        feature.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    features_modelList.add(new features_model(Data.child("f").getValue().toString()));
                }
                featuresAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getHighlights() {
        rv_highlights = findViewById(R.id.rv_highlights);
        rv_highlights.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_highlights.setLayoutManager(layoutManager1);

        highlights_modelList = new ArrayList<highlights_model>();
        highlightsAdaptor = new highlights_adaptor(highlights_modelList);
        rv_highlights.setAdapter(highlightsAdaptor);

        highlight = FirebaseDatabase.getInstance().getReference("global_items").child(id).child("highlights");
        highlight.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    highlights_modelList.add(new highlights_model(Data.child("tag").getValue().toString(),Data.child("ans").getValue().toString()));
                }
                highlightsAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void get_product() {


        product = FirebaseDatabase.getInstance().getReference("global_items").child(id);
        product.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mrp = snapshot.child("mrp").getValue().toString();
                price = snapshot.child("price").getValue().toString();
               product_mrp.setText("₹"+mrp);

                product_price.setText("₹"+price);


                int m,p;
                m= Integer.parseInt(mrp);
                p = Integer.parseInt(price);
                discount_D =(int) Math.ceil((m-p)*100/m);

                if(mrp.equals(price) || discount_D==0){
                    product_mrp.setVisibility(View.GONE);
                    discount.setVisibility(View.GONE);
                }


                discount.setText(String.valueOf((int) discount_D)+" %off");
               product_name.setText(snapshot.child("name").getValue().toString());
                product_unit.setText(snapshot.child("unit").getValue().toString());
                description.setText(snapshot.child("description").getValue().toString());
                brand.setText(snapshot.child("brand").getValue().toString());



                imageSlider = (ImageSlider) findViewById(R.id.products_image_slider);
                final List<SlideModel> images = new ArrayList<>();
                icon1 = snapshot.child("icon1").getValue().toString();
                images.add(new SlideModel(icon1,"", ScaleTypes.CENTER_INSIDE));
                images.add(new SlideModel(snapshot.child("icon2").getValue().toString(),"", ScaleTypes.CENTER_INSIDE));
                images.add(new SlideModel(snapshot.child("icon3").getValue().toString(),"", ScaleTypes.CENTER_INSIDE));

                imageSlider.setImageList(images,ScaleTypes.CENTER_INSIDE);



                loader.dismiss();
                scrollView.setVisibility(View.VISIBLE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void check_cart() {
        checkcart = FirebaseDatabase.getInstance().getReference("carts");
        Query query = checkcart.orderByKey().equalTo(homeID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    count_items();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void count_items() {
        items_in_cart =  FirebaseDatabase.getInstance().getReference("carts").child(homeID);
        items_in_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemCount = snapshot.getChildrenCount();
                if (itemCount == 0) {
                    cart_item_count.setVisibility(View.INVISIBLE);
                } else {
                    cart_item_count.setText(String.valueOf(itemCount));
                    cart_item_count.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void goto_cart_product(View view){
        Intent intent = new Intent(product_detail_activity.this,cart_activity.class);

        startActivity(intent);


    }
    public  void back(View view){

        finish();
    }
}