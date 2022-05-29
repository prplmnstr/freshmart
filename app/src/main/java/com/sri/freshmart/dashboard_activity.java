
package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
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
import java.util.Timer;
import java.util.TimerTask;

public class dashboard_activity extends AppCompatActivity {


    ImageSlider imageSlider;
    DatabaseReference slider_FB , category_FB, DOD_FB, get_homeID_FB,items_in_cart,checkcart;
    RecyclerView cat_recycler, HP_products_recycler;
    public static RecyclerView search_items_rv;
    ConstraintLayout cat_layout,dod_layout;
    public  static  RecyclerView cat_rv,retail_rv;
    private List<brands_modal> cat_modal_list,retail_modal_list;
    public static NestedScrollView dash_layout;

    public static ImageView cart_image,back_img;
   public static TextView homeID,homeID2,cart_item_count, deals_text,   more1,more2,logo_txt;


   public static String id="CKDA00" ;
    private  cat_item_adaptor_in_search cat_adaptor,retail_adaptor;


   long itemCount;

  public static Dialog dialog,loader;



    private catogary_adopter catogaryAdopter;
    private horizontal_products_adopter HP_Adopter;



    private List<catogary_model> modelList;
    private List<fragment_rv_model> HP_modelList;

    FirebaseUser user;
    String user_number;
    String skip;












    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard_activity);


        loader = new Dialog(dashboard_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();

       check_internet();














        homeID = findViewById(R.id.home_id_txt);
        homeID2 = findViewById(R.id.home_id_txt2);
        cart_item_count = findViewById(R.id.item_count_dashboard);
        deals_text = findViewById(R.id.deals_text);
        cat_rv = findViewById(R.id.cat_rv_dash);
        retail_rv = findViewById(R.id.cat_rv_retail);
        search_items_rv =findViewById(R.id.search_items);
        dash_layout = findViewById(R.id.dash_layout);
        back_img = findViewById(R.id.back_button_);
        logo_txt = findViewById(R.id.logo_txt);



        cart_image = findViewById(R.id.cart_image_dashboard);
        cat_layout = findViewById(R.id.cat_layout);
        dod_layout = findViewById(R.id.dod_layout);

        more1 = findViewById(R.id.cat_more_text);
        more2 = findViewById(R.id.dod_more_text);

        Objects.requireNonNull(getSupportActionBar()).hide();

            skip = getIntent().getStringExtra("skip");


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        search_items_rv.setLayoutManager(layoutManager);


        //// get home id ///

        if(skip.equals("s")){
            homeID.setText(id);
            cart_image.setVisibility(View.VISIBLE);
            Deals_of_the_day();
            check_cart();
        }else{
            user=FirebaseAuth.getInstance().getCurrentUser();
            user_number   =  Objects.requireNonNull(user).getPhoneNumber();
            get_id(user_number);
        }


        get_cats();
        get_retailers();

        //// get home id ///



        ///// Banner  slider   ////
        banner_slider();

        ///// Banner  slider   ////

        ///catagory items//


        category_item();

        ///catagory items//


        /////rv items




        more1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard_activity.this,search_activity.class);

                startActivity(intent);
            }
        });
        more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard_activity.this,category_activity.class);
                intent.putExtra("i", "offers");
                intent.putExtra("pos", "0");
                startActivity(intent);
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_items_rv.setVisibility(View.GONE);
                back_img.setVisibility(View.GONE);
                homeID2.setVisibility(View.GONE);
                homeID.setVisibility(View.VISIBLE);
                dash_layout.setVisibility(View.VISIBLE);


                logo_txt.setVisibility(View.VISIBLE);


            }
        });

    }

    private void get_id(String user_number) {

        get_homeID_FB = FirebaseDatabase.getInstance().getReference("customers").child(user_number);
        get_homeID_FB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id =  snapshot.child("homeid").getValue().toString();
                homeID.setText(id);
                cart_image.setVisibility(View.VISIBLE);
                Deals_of_the_day();
                check_cart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void check_internet() {
        dialog = new Dialog(dashboard_activity.this);
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



        if(!connected(dashboard_activity.this)){
            dialog.show();
            loader.dismiss();




        }
    }

    private boolean connected(dashboard_activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }


    private void check_cart() {
        checkcart = FirebaseDatabase.getInstance().getReference("carts");
        Query query = checkcart.orderByKey().equalTo(id);
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
       items_in_cart =  FirebaseDatabase.getInstance().getReference("carts").child(id);
       items_in_cart.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               itemCount = snapshot.getChildrenCount();
                   if(itemCount==0) {
                       cart_item_count.setVisibility(View.INVISIBLE);
                   }
                   else {
                       cart_item_count.setText(String.valueOf(itemCount));
                       cart_item_count.setVisibility(View.VISIBLE);
                   }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


    private void Deals_of_the_day() {
        HP_products_recycler = findViewById(R.id.rv2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        HP_products_recycler.setLayoutManager(layoutManager2);

        HP_modelList = new ArrayList<fragment_rv_model>();

        HP_Adopter = new horizontal_products_adopter(HP_modelList);
        HP_products_recycler.setAdapter(HP_Adopter);

        DatabaseReference dod_title = FirebaseDatabase.getInstance().getReference("dod_title");
        dod_title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deals_text.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DOD_FB = FirebaseDatabase.getInstance().getReference("DOD");
        DOD_FB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    HP_modelList.add(new fragment_rv_model(Data.child("price").getValue().toString(),Data.child("mrp").getValue().toString(),Data.child("icon").getValue().toString(),Data.child("name").getValue().toString(),Data.child("unit").getValue().toString(),Data.child("id").getValue().toString()));
                loader.dismiss();
                dash_layout.setVisibility(View.VISIBLE);
                }


                HP_Adopter.notifyDataSetChanged();
                dod_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void category_item() {

        cat_recycler = findViewById(R.id.rv1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        cat_recycler.setLayoutManager(layoutManager1);

        modelList = new ArrayList<catogary_model>();


        catogaryAdopter = new catogary_adopter(modelList);
        cat_recycler.setAdapter(catogaryAdopter);



        category_FB = FirebaseDatabase.getInstance().getReference("category");
        category_FB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot Data:snapshot.getChildren())
                {
                    modelList.add(new catogary_model(Data.child("icon").getValue().toString(),Data.child("name").getValue().toString()));
                }
                catogaryAdopter.notifyDataSetChanged();
                cat_layout.setVisibility(View.VISIBLE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void banner_slider() {
        imageSlider = (ImageSlider) findViewById(R.id.image_slider);
        final List<SlideModel> images = new ArrayList<>();
        slider_FB = FirebaseDatabase.getInstance().getReference("slider");
        slider_FB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                for(DataSnapshot Data:snapshot.getChildren()){
                    images.add(new SlideModel(Data.child("url").getValue().toString(),"",ScaleTypes.FIT));
                }
                imageSlider.setImageList(images,ScaleTypes.FIT);


                imageSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                     String type =   snapshot.child(String.valueOf(i)).child("type").getValue().toString();
                     if(type.startsWith("g")){
                         Intent intent = new Intent(dashboard_activity.this,product_detail_activity.class);
                         intent.putExtra("id",type);
                         startActivity(intent);

                     }else {
                         Intent intent = new Intent(dashboard_activity.this,category_activity.class);
                         intent.putExtra("i", "offers");
                         intent.putExtra("pos", type.replaceAll("[A-Z,a-z]","").trim());
                         startActivity(intent);

                     }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













    }
    public void goto_cart(View view){
        Intent intent = new Intent(dashboard_activity.this,cart_activity.class);

        startActivity(intent);


    }
    public void goto_search(View view) {
        Intent intent = new Intent(dashboard_activity.this, search_activity.class);

        startActivity(intent);
    }




    private void get_cats() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        cat_rv.setLayoutManager(layoutManager1);
        cat_modal_list = new ArrayList<brands_modal>();
        cat_adaptor = new cat_item_adaptor_in_search(cat_modal_list,0);
        cat_rv.setAdapter(cat_adaptor);

        DatabaseReference get_categories = FirebaseDatabase.getInstance().getReference("category_list");
        get_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    cat_modal_list.add(new brands_modal(
                            Data.child("i1").getValue().toString(),
                            Data.child("i2").getValue().toString(),
                            Data.child("t1").getValue().toString(),
                            Data.child("t2").getValue().toString()));
                }
                cat_adaptor.notifyDataSetChanged();
                loader.dismiss();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void get_retailers() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        retail_rv.setLayoutManager(layoutManager1);
        retail_modal_list = new ArrayList<brands_modal>();
        retail_adaptor = new cat_item_adaptor_in_search(retail_modal_list,2);
        retail_rv.setAdapter(retail_adaptor);

        DatabaseReference get_categories = FirebaseDatabase.getInstance().getReference("dummy");
        get_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    retail_modal_list.add(new brands_modal(
                            Data.child("i1").getValue().toString(),
                            Data.child("i2").getValue().toString(),
                            Data.child("t1").getValue().toString(),
                            Data.child("t2").getValue().toString()));
                }
                retail_adaptor.notifyDataSetChanged();
                loader.dismiss();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(search_items_rv.getVisibility()==View.VISIBLE){
            search_items_rv.setVisibility(View.GONE);
            back_img.setVisibility(View.GONE);
            homeID2.setVisibility(View.GONE);
            homeID.setVisibility(View.VISIBLE);
            dash_layout.setVisibility(View.VISIBLE);


            logo_txt.setVisibility(View.VISIBLE);
        }else{
            finish();
        }

    }
}