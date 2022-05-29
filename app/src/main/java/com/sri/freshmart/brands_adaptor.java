package com.sri.freshmart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class brands_adaptor extends RecyclerView.Adapter<brands_adaptor.ViewHolder>   {

    List<brands_modal> brands_modalList;
    String Brand;

    private List<fragment_rv_model> models;
    private fragment_rv_adopter fragmentRvAdopter;


    public brands_adaptor(List<brands_modal> brands_modalList) {
        this.brands_modalList = brands_modalList;
    }

    @NonNull
    @Override
    public brands_adaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final brands_adaptor.ViewHolder holder, int position) {
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
        Brand = holder.t1.getText().toString();
        search_activity.searchView.setVisibility(View.INVISIBLE);
        search_activity.brand_title_text.setText(Brand);
        search_activity.brand_title_text.setVisibility(View.VISIBLE);

        bring_brand_items(Brand);

    }
});

        holder.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Brand = holder.t2.getText().toString();
                search_activity.searchView.setVisibility(View.INVISIBLE);
                search_activity.brand_title_text.setText(Brand);
                search_activity.brand_title_text.setVisibility(View.VISIBLE);


                bring_brand_items(Brand);

            }
        });


    }

    private void bring_brand_items(String brand) {
        search_activity.loader.show();
        models = new ArrayList<fragment_rv_model>();
        fragmentRvAdopter = new fragment_rv_adopter(models);
        search_activity.search_items_rv.setAdapter(fragmentRvAdopter);


     FirebaseFirestore.getInstance().collection("global_items").whereEqualTo("brand",brand)
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

    }

    @Override
    public int getItemCount() {
        return brands_modalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView t1,t2;
        ImageView i1,i2;
        ConstraintLayout l1,l2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            i1 = itemView.findViewById(R.id.i1);
            i2 = itemView.findViewById(R.id.i2);
            l1 = itemView.findViewById(R.id.brand1_layout);
            l2 = itemView.findViewById(R.id.bramd2_layout);
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
