package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class category_activity extends AppCompatActivity {


    //takes input position
    //and category or offer


    DatabaseReference get_categories,items_in_cart,checkcart;
    pager_adopter pagerAdopter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private long itemCount;
    TextView cart_item_count,home_id_txt;

    String homeid = dashboard_activity.id;
    String pos;

    Dialog loader;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.category_activity);
        final String type = getIntent().getStringExtra("i");
        cart_item_count = findViewById(R.id.item_count_category);
        check_cart();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager_id);
        home_id_txt = findViewById(R.id.home_id_txt);
        home_id_txt.setText(homeid);


         pos = getIntent().getStringExtra("pos");

        loader = new Dialog(category_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();




        setup_pager(viewPager,type);

        tabLayout.setupWithViewPager(viewPager);


          viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


              }

              @Override
              public void onPageSelected(int position) {
                  if (type.equals("category")) {
                      if (position == pagerAdopter.getCount() - 1) {
                          Intent intent = new Intent(category_activity.this, search_activity.class);
                          startActivity(intent);
                          finish();
                      }
                  }
              }

              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });
        // this u can do for slide effect


       /* viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(Integer.parseInt(pos));
            }
        },1000);*/


    }
    private void setup_pager(final ViewPager viewPager , final String type){









        get_categories = FirebaseDatabase.getInstance().getReference(type);//i is the variable which decides which items to fetch
        get_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 pagerAdopter = new pager_adopter(getSupportFragmentManager());
                for(DataSnapshot data:snapshot.getChildren()){
                    pagerAdopter.addFragment(new items_fragment(),data.child("name").getValue().toString(),type+"_items");//a means category items or dod items
                }
                     loader.dismiss();
                    viewPager.setAdapter(pagerAdopter);

                viewPager.setCurrentItem(Integer.parseInt(pos));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void check_cart() {
        checkcart = FirebaseDatabase.getInstance().getReference("carts");
        Query query = checkcart.orderByKey().equalTo(homeid);
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
        items_in_cart =  FirebaseDatabase.getInstance().getReference("carts").child(homeid);
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

    public void goto_cart_category(View view) {
        Intent intent = new Intent(category_activity.this, cart_activity.class);

        startActivity(intent);
    }
    public  void back(View view){

        finish();
    }


}


