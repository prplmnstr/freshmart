package com.sri.freshmart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class order_details_rv_adopter  extends RecyclerView.Adapter<order_details_rv_adopter.ViewHolder> {

    private List<order_details_model> orderDetailsModelList;


    String icon_,unit_,name_,price_,homeid = dashboard_activity.id;
    int sum_s;


    public order_details_rv_adopter(List<order_details_model> orderDetailsModelList) {
        this.orderDetailsModelList = orderDetailsModelList;
    }

    @NonNull
    @Override
    public order_details_rv_adopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull order_details_rv_adopter.ViewHolder holder, int position) {

        icon_   = orderDetailsModelList.get(position).getIcon();
        price_   = orderDetailsModelList.get(position).getPrice();
       name_   = orderDetailsModelList.get(position).getName();
        unit_  = orderDetailsModelList.get(position).getUnit();

        sum_s =  Integer.parseInt(price_)*Integer.parseInt(unit_);

        holder.setIcon(icon_);
        holder.setName(name_);
        holder.setPrice("₹"+price_);
        holder.setUnit(unit_);
        holder.setSum("₹"+String.valueOf(sum_s));

    }

    @Override
    public int getItemCount() {
        return orderDetailsModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,unit,sum;
        ImageView icon;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.order_title);
            price = itemView.findViewById(R.id.price_detail);
            unit = itemView.findViewById(R.id.qty_details);
            sum = itemView.findViewById(R.id.total_details);
            icon = itemView.findViewById(R.id.order_image_);
        }

        private void setName(String name1){
            name.setText(name1);
        }
        private void setPrice(String price1){
            price.setText(price1);
        }
        private void setUnit(String unit1){
            unit.setText(unit1);
        }
        private void setSum(String sum1){
            sum.setText(sum1);
        }
        private void setIcon(String url) {
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(icon);

        }
    }

}
