package com.sri.freshmart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class cat_item_adaptor_in_search extends RecyclerView.Adapter<cat_item_adaptor_in_search.ViewHolder> {

    List<brands_modal> brands_modalList;
    String cat;
    Context context;
    int from;
    private List<fragment_rv_model> models;
    private fragment_rv_adopter fragmentRvAdopter;


    public cat_item_adaptor_in_search(List<brands_modal> brands_modalList,int from) {
        this.brands_modalList = brands_modalList;
        this.from = from;
    }

    @NonNull
    @Override
    public cat_item_adaptor_in_search.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_by_cat_items, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final cat_item_adaptor_in_search.ViewHolder holder, int position) {
        String te1  = brands_modalList.get(position).getT1();
        String te2  = brands_modalList.get(position).getT2();
        String im1  = brands_modalList.get(position).getI1();
        String im2  = brands_modalList.get(position).getI2();
        holder.setI1(im1);
        holder.setI2(im2);
        holder.setT1(te1);
        holder.setT2(te2);

        holder.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat = holder.t1.getText().toString();

                if(from==1) {
                    search_activity.searchView.setVisibility(View.INVISIBLE);
                    search_activity.brand_title_text.setText(cat);
                    search_activity.brand_title_text.setVisibility(View.VISIBLE);


                }else{
                    dashboard_activity.homeID2.setText(cat);
                    dashboard_activity.homeID.setVisibility(View.GONE);
                    dashboard_activity.homeID2.setVisibility(View.VISIBLE);
                    dashboard_activity.logo_txt.setVisibility(View.INVISIBLE);
                    dashboard_activity.back_img.setVisibility(View.VISIBLE);
                }
                bring_brand_items(cat);
            }
        });

        holder.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat = holder.t2.getText().toString();
                if(from==1) {
                    search_activity.searchView.setVisibility(View.INVISIBLE);
                    search_activity.brand_title_text.setText(cat);
                    search_activity.brand_title_text.setVisibility(View.VISIBLE);


                }else{
                    dashboard_activity.homeID2.setText(cat);
                    dashboard_activity.homeID.setVisibility(View.GONE);
                    dashboard_activity.homeID2.setVisibility(View.VISIBLE);
                    dashboard_activity.logo_txt.setVisibility(View.INVISIBLE);
                    dashboard_activity.back_img.setVisibility(View.VISIBLE);
                }
                bring_brand_items(cat);

            }
        });


    }

    private void bring_brand_items(String brand) {
        if(from ==1){
            search_activity.loader.show();
            models = new ArrayList<fragment_rv_model>();
            fragmentRvAdopter = new fragment_rv_adopter(models);
            search_activity.search_items_rv.setAdapter(fragmentRvAdopter);


            FirebaseFirestore.getInstance().collection("global_items").whereEqualTo("category",brand)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                            models.add(new fragment_rv_model(documentSnapshot.get("price").toString(),
                                    documentSnapshot.get("mrp").toString(),
                                    documentSnapshot.get("icon").toString(),
                                    documentSnapshot.get("name").toString(),
                                    documentSnapshot.get("unit").toString(),
                                    documentSnapshot.get("id").toString()));





                        }

                        fragmentRvAdopter.notifyDataSetChanged();
                        search_activity.loader.dismiss();


                    }
                }
            });

            search_activity.brand_layout.setVisibility(View.INVISIBLE);
            search_activity.no_item.setVisibility(View.INVISIBLE);
            search_activity.search_items_rv.setVisibility(View.VISIBLE);

        }else {
            dashboard_activity.loader.show();
            models = new ArrayList<fragment_rv_model>();
            fragmentRvAdopter = new fragment_rv_adopter(models);
            dashboard_activity.search_items_rv.setAdapter(fragmentRvAdopter);

            if (from == 2) {
                FirebaseFirestore.getInstance().collection("global_items").whereEqualTo("shop", brand)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                models.add(new fragment_rv_model(documentSnapshot.get("price").toString(),
                                        documentSnapshot.get("mrp").toString(),
                                        documentSnapshot.get("icon").toString(),
                                        documentSnapshot.get("name").toString(),
                                        documentSnapshot.get("unit").toString(),
                                        documentSnapshot.get("id").toString()));


                            }

                            fragmentRvAdopter.notifyDataSetChanged();
                            dashboard_activity.loader.dismiss();


                        }
                    }
                });

            } else {
                FirebaseFirestore.getInstance().collection("global_items").whereEqualTo("category", brand)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                models.add(new fragment_rv_model(documentSnapshot.get("price").toString(),
                                        documentSnapshot.get("mrp").toString(),
                                        documentSnapshot.get("icon").toString(),
                                        documentSnapshot.get("name").toString(),
                                        documentSnapshot.get("unit").toString(),
                                        documentSnapshot.get("id").toString()));


                            }

                            fragmentRvAdopter.notifyDataSetChanged();
                            dashboard_activity.loader.dismiss();


                        }
                    }
                });
            }
                dashboard_activity.dash_layout.setVisibility(View.INVISIBLE);

                dashboard_activity.search_items_rv.setVisibility(View.VISIBLE);

            }


    }

    @Override
    public int getItemCount() {
        return brands_modalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        TextView t1,t2;
        ImageView i1,i2;
        ConstraintLayout l1,l2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.t1_cat);
            t2 = itemView.findViewById(R.id.t2_cat);
            i1 = itemView.findViewById(R.id.i1_cat);
            i2 = itemView.findViewById(R.id.i2_cat);
            l1 = itemView.findViewById(R.id.brand1_layout_cat);
            l2 = itemView.findViewById(R.id.bramd2_layout_cat);
        }

        private void setT1(String text){
            t1.setText(text);
        }
        private void setT2(String text){
            t2.setText(text);
        }


        private void  setI1(String url){
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(i1);

        }
        private void  setI2(String url){
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(i2);

        }

    }
}
