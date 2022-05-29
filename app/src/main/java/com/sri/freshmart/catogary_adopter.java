package com.sri.freshmart;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Objects;




public class catogary_adopter extends RecyclerView.Adapter<catogary_adopter.ViewHolder> {
// list
    private List<catogary_model> model_list;
    LinearLayout cat_layout_item;
    Context context;


    //constructor

    public catogary_adopter(List<catogary_model> model_list) {

        this.model_list = model_list;
    }


    @NonNull
    @Override
    public catogary_adopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      // create view here and inflate layout//
         context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull catogary_adopter.ViewHolder holder, final int position) {
      // bind data and variables//
        String icon = model_list.get(position).getCat_icon_link();
        String name = model_list.get(position).getCat_text_name();
        holder.setCat_name(name);
        holder.setCat_icon(icon);

        holder.cat_layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,category_activity.class);
                intent.putExtra("i", "category");
               intent.putExtra("pos",String.valueOf(position) );

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView cat_icon;
        private TextView cat_name;
       private   LinearLayout cat_layout_item;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cat_icon = itemView.findViewById(R.id.cat_image);
            cat_name = itemView.findViewById(R.id.cat_text);
            cat_layout_item = itemView.findViewById(R.id.Item_layout);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(itemView.getContext(),category_activity.class);
                    intent.putExtra("i", "category");
                    itemView.getContext().startActivity(intent);
                }
            });*/

        }

        private void setCat_icon(String icon_url){

            Glide.with(itemView.getContext()).load(icon_url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(cat_icon);

        }

        private void setCat_name(String name){
            cat_name.setText(name);
        }
    }
}
