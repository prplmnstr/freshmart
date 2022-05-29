package com.sri.freshmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class search_activity extends AppCompatActivity {


   public static SearchView searchView;
   public static RecyclerView search_items_rv,brand_rv,cat_rv;
  public static TextView no_item,cart_item_count,cat_text;
   DatabaseReference items_in_cart,get_brands;
   String id = dashboard_activity.id;
    long itemCount;
public  static NestedScrollView brand_layout;
    private brands_adaptor brandsAdaptor;
    private List<brands_modal> brands_modalList,cat_modal_list;
    public static Dialog loader;
    public static  TextView brand_title_text;
    private  cat_item_adaptor_in_search cat_adaptor;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.search_activity);

       searchView = findViewById(R.id.search_view);
       search_items_rv =findViewById(R.id.search_items);
       brand_rv  = findViewById(R.id.brand_rv);
         cat_rv= findViewById(R.id.cat_rv);

       brand_layout = findViewById(R.id.brand_layout);
       brand_title_text = findViewById(R.id.brand_title);


       no_item = findViewById(R.id.no_item_txt);

     cart_item_count= findViewById(R.id.item_count_search);



        loader = new Dialog(search_activity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loader.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loader.setCancelable(false);

        loader.getWindow().getAttributes().windowAnimations = R.style.animation;

        loader.show();



        count_items();



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        search_items_rv.setLayoutManager(layoutManager);




        get_brandss();
        get_cats();









       ///////////





        /////


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {




                final List<fragment_rv_model> list = new ArrayList<>();
                final List<String> ids = new ArrayList<>();

                final Adapter adapter = new Adapter(list);
                search_items_rv.setAdapter(adapter);
               loader.show();
                brand_layout.setVisibility(View.INVISIBLE);
                search_items_rv.setVisibility(View.VISIBLE);
                list.clear();
                ids.clear();




                final String[] tags = s.toLowerCase().split(" ");
                for(final String tag:tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("global_items").whereArrayContains("tags",tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                              for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                  fragment_rv_model model = new fragment_rv_model(documentSnapshot.get("price").toString(),
                                          documentSnapshot.get("mrp").toString(),
                                          documentSnapshot.get("icon").toString(),
                                          documentSnapshot.get("name").toString(),
                                          documentSnapshot.get("unit").toString(),
                                          documentSnapshot.get("id").toString());

                                  model.setTags((ArrayList<String>) documentSnapshot.get("tags"));

                                  if(!ids.contains(model.getItem_id())){
                                      list.add(model);
                                      ids.add(model.getItem_id());
                                  }

                              }
                              loader.dismiss();

                              if(tag.equals(tags[tags.length-1])){
                                  if(list.size()==0){
                                      search_items_rv.setVisibility(View.INVISIBLE);
                                      brand_layout.setVisibility(View.INVISIBLE);
                                      no_item.setVisibility(View.VISIBLE);


                                  }else{
                                      ////// item found
                                      /////recycler  visiblity gone
                                      adapter.getFilter().filter(s);
                                  }

                              }
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(search_activity.this,error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {



                return false;
            }
        });


    }

    private void get_brandss() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        brand_rv.setLayoutManager(layoutManager1);
       brands_modalList = new ArrayList<brands_modal>();
        brandsAdaptor = new brands_adaptor(brands_modalList);
      brand_rv.setAdapter(brandsAdaptor);

        get_brands = FirebaseDatabase.getInstance().getReference("brands_list");
        get_brands.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    brands_modalList.add(new brands_modal(
                            Data.child("i1").getValue().toString(),
                            Data.child("i2").getValue().toString(),
                            Data.child("t1").getValue().toString(),
                            Data.child("t2").getValue().toString()));
                }
                brandsAdaptor.notifyDataSetChanged();
                brand_layout.setVisibility(View.VISIBLE);
                loader.dismiss();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void get_cats() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        cat_rv.setLayoutManager(layoutManager1);
        cat_modal_list = new ArrayList<brands_modal>();
        cat_adaptor = new cat_item_adaptor_in_search(cat_modal_list,1);
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

    class Adapter extends fragment_rv_adopter implements Filterable {


        List<fragment_rv_model> original_list;

        public Adapter(List<fragment_rv_model> fragment_rv_modelList) {
            super(fragment_rv_modelList);

            original_list = fragment_rv_modelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    ////filter logic
                    FilterResults results = new FilterResults();
                    List<fragment_rv_model> filteredList =new ArrayList<>();
                    final String[] tags = charSequence.toString().toLowerCase().split(" ");


                    for(fragment_rv_model model:original_list){
                        ArrayList<String > present_tags = new ArrayList<>();
                        for (String tag:tags){
                            if(model.getTags().contains(tag)){
                                present_tags.add(tag);
                            }
                        }
                        model.setTags(present_tags);
                    }
                    for(int i=tags.length;i>0;i--){

                        for(fragment_rv_model model:original_list){
                            if(model.getTags().size()==i){
                                filteredList.add(model);
                            }
                        }

                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                  if(filterResults.count>0){
                      setFragment_rv_modelList((List<fragment_rv_model>) filterResults.values);

                  }

                 notifyDataSetChanged();
                    no_item.setVisibility(View.INVISIBLE);
                    search_items_rv.setVisibility(View.VISIBLE);

                }
            };
        }
    }

    public void go_back(View view){

        if(search_items_rv.getVisibility()==View.VISIBLE){
            no_item.setVisibility(View.INVISIBLE);
            brand_title_text.setVisibility(View.INVISIBLE);
            search_items_rv.setVisibility(View.INVISIBLE);
            brand_layout.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
        }else if(brand_layout.getVisibility()== View.VISIBLE){
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        if(searchView.hasFocus()){
            searchView.clearFocus();

            no_item.setVisibility(View.INVISIBLE);
            search_items_rv.setVisibility(View.INVISIBLE);
            brand_layout.setVisibility(View.VISIBLE);

        }else if(search_items_rv.getVisibility()==View.VISIBLE){
                no_item.setVisibility(View.INVISIBLE);
                brand_title_text.setVisibility(View.INVISIBLE);
                search_items_rv.setVisibility(View.INVISIBLE);
                brand_layout.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
    }else if(brand_layout.getVisibility()== View.VISIBLE){
            finish();
        }

    }


    public void gotocart(View view){
        Intent intent = new Intent(search_activity.this,cart_activity.class);
        startActivity(intent);
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


}