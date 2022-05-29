package com.sri.freshmart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class cart_item_adaptor extends RecyclerView.Adapter<cart_item_adaptor.ViewHolder> {

    private List<cart_item_model> cart_item_modelList;
    private int sum_S;
    String charge_instant,charge_normal;

    int price1 = 0,mrp1 = 0,saving = 0;


   DatabaseReference cart  = FirebaseDatabase.getInstance().getReference("carts");
   DatabaseReference checkCart,charge_db;



    Context context;

    String itemid, homeid ,icon_1,name_,mrp_,price_,unit_,unit_number_;


    public cart_item_adaptor(List<cart_item_model> cart_item_modelList) {
        this.cart_item_modelList = cart_item_modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        icon_1 = cart_item_modelList.get(position).getIcon1();
         name_ = cart_item_modelList.get(position).getName_cart();
         mrp_ = cart_item_modelList.get(position).getMrp_cart();
        price_ = cart_item_modelList.get(position).getPrice_cart();
         unit_ = cart_item_modelList.get(position).getUnit_cart();
        unit_number_ = cart_item_modelList.get(position).getUnit_number_cart();
        itemid = cart_item_modelList.get(position).getCart_id();
         homeid = dashboard_activity.id;


         mrp1 = mrp1+(Integer.parseInt(mrp_)*Integer.parseInt(unit_number_));
         price1 = price1+(Integer.parseInt(price_)*Integer.parseInt(unit_number_)); //total
         saving = mrp1-price1;


         check_charge();
         check_empty_cart();




        sum_S = Integer.parseInt(price_)*Integer.parseInt(unit_number_);


        holder.setIcon1(icon_1);
        holder.setName_cart(name_);
        holder.setMrp_cart(mrp_);
        holder.setDummy_icon(icon_1);
        holder.setPrice_cart(price_);
        holder.setUnit_cart(unit_);
        holder.setItem_id(itemid);
        holder.setUnit_number(unit_number_);
      holder.setSum("₹"+String.valueOf(sum_S));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x = Integer.parseInt(holder.unit_number.getText().toString());


                holder.unit_number.setText(String.valueOf(x+1));

                update_to_cart(holder.mrp.getText().toString(),
                        holder.price.getText().toString(),
                        holder.name.getText().toString(),
                        holder.dummy_icon.getText().toString(),
                        holder.unit_number.getText().toString(),
                        holder.unit.getText().toString(),
                        holder.item_id.getText().toString());


                mrp1 = mrp1+ Integer.parseInt(holder.mrp.getText().toString());
                price1 = price1 +  Integer.parseInt(holder.price.getText().toString());
                saving = mrp1 - price1;
                set_price_details();
                int s = Integer.parseInt(holder.price.getText().toString())*Integer.parseInt(holder.unit_number.getText().toString());
                holder.sum.setText("₹"+String.valueOf(s));






            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(holder.unit_number.getText().toString());

                if(x==1){
                    int minus_from_mrp1 = Integer.parseInt(holder.mrp.getText().toString());
                    int minus_from_price1 = Integer.parseInt(holder.price.getText().toString());

                    update_price_details(minus_from_mrp1,minus_from_price1);

                    remove_from_cart(holder.item_id.getText().toString(),holder.getAdapterPosition());


                }
                else {
                    holder.unit_number.setText(String.valueOf(x-1));

                    update_to_cart(holder.mrp.getText().toString(),
                            holder.price.getText().toString(),
                            holder.name.getText().toString(),
                            holder.dummy_icon.getText().toString(),
                            holder.unit_number.getText().toString(),
                            holder.unit.getText().toString(),
                            holder.item_id.getText().toString());


                    mrp1 = mrp1 -  Integer.parseInt(holder.mrp.getText().toString());
                    price1 = price1 -  Integer.parseInt(holder.price.getText().toString());
                    saving = mrp1 - price1;
                    set_price_details();
                    int s = Integer.parseInt(holder.price.getText().toString())*Integer.parseInt(holder.unit_number.getText().toString());
                    holder.sum.setText("₹"+String.valueOf(s));


                }
            }
        });

        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minus_from_mrp1 = Integer.parseInt(holder.mrp.getText().toString())*Integer.parseInt(holder.unit_number.getText().toString());
                int minus_from_price1 = Integer.parseInt(holder.price.getText().toString())*Integer.parseInt(holder.unit_number.getText().toString());
                update_price_details(minus_from_mrp1,minus_from_price1);
                remove_from_cart(holder.item_id.getText().toString(),holder.getAdapterPosition());


            }
        });

    }

    private void check_charge() {
        final String colony = homeid.replaceAll("[0-9]","").trim();
        charge_db = FirebaseDatabase.getInstance().getReference("charges");
        charge_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                charge_instant = snapshot.child("instant").getValue().toString();
                charge_normal = snapshot.child(colony).getValue().toString();
                set_price_details();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check_empty_cart() {
        checkCart = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
        checkCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long itemCount = snapshot.getChildrenCount();
                if(itemCount==0) {
                    cart_activity.empty_cart_layout.setVisibility(View.VISIBLE);
                    cart_activity.place_order_layout.setVisibility(View.INVISIBLE);
                    cart_activity.scrollView.setVisibility(View.INVISIBLE);
                }
                else {

                    cart_activity.place_order_layout.setVisibility(View.VISIBLE);
                    cart_activity.scrollView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update_price_details( int mrp11, int price11) {

        mrp1 = mrp1 - mrp11;
        price1 = price1 - price11;
        saving = mrp1 - price1;
        set_price_details();
    }

    public  void set_price_details() {

        if(cart_activity.normal.isChecked()){
            if(charge_normal.equals("0")){
                cart_activity.delivery_charge.setText("FREE");
                cart_activity.total_amt.setText("₹ "+String.valueOf(price1));

            }else {
                cart_activity.delivery_charge.setText("₹ "+charge_normal);
                cart_activity.total_amt.setText("₹ "+String.valueOf(price1+Integer.parseInt(charge_normal)));
            }

        }else if(cart_activity.instant.isChecked()){
            cart_activity.delivery_charge.setText("+ ₹"+charge_instant);
            cart_activity.total_amt.setText("₹"+String.valueOf(price1+Integer.parseInt(charge_instant)));
        }

        cart_activity.discount_amt.setText("- ₹"+String.valueOf(saving));
        cart_activity.price_amt.setText("₹"+mrp1);
        cart_activity.saving.setText("you save ₹ "+saving+" on this order");
    }

    private void remove_from_cart(String itemid,int position) {

        cart_item_modelList.remove(position);
        notifyItemRemoved(position);


        cart.child(homeid).child(itemid).removeValue();
        Toast.makeText(context,"item removed from cart",Toast.LENGTH_SHORT).show();
    }




    private void update_to_cart(String mrp,String price,String name,String icon,String unit_n,String unit,String itemid) {


        add_to_cart item = new add_to_cart(mrp,price,name,icon,unit_n,unit,itemid);
        cart.child(homeid).child(itemid).setValue(item);


    }

    @Override
    public int getItemCount() {
        return cart_item_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, mrp, price, unit, unit_number,item_id,sum,dummy_icon;
        ImageView remove_item, icon1;
        Button plus, minus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name_cart);
            mrp = itemView.findViewById(R.id.mrp_text_cart);

            price = itemView.findViewById(R.id.price_text_cart);
          sum = itemView.findViewById(R.id.sum_number);
            icon1 = itemView.findViewById(R.id.item_icon_cart);
            unit = itemView.findViewById(R.id.unit_text_cart);
            unit_number = itemView.findViewById(R.id.unit_number_cart);
            remove_item = itemView.findViewById(R.id.delete_image);
            plus = itemView.findViewById(R.id.plus_button_cart);
            minus = itemView.findViewById(R.id.minus_button_cart);
            item_id = itemView.findViewById(R.id.item_id_cart);
            dummy_icon = itemView.findViewById(R.id.dummy_icon_cart);


        }
        private void setDummy_icon(String dummy_icon1){
            dummy_icon.setText(dummy_icon1);
        }

       private void setItem_id(String item_id1){
            item_id.setText(item_id1);
        }

        private void setPrice_cart(String price1) {
            price.setText(price1);

        }

        private void setMrp_cart(String mrp1) {
            mrp.setText(mrp1);

        }

        private void setIcon1(String url) {
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(icon1);

        }

       private void setName_cart(String name_1) {
            name.setText(name_1);


        }


        private void setUnit_cart(String unit1) {
            unit.setText(unit1);
        }

        private void setUnit_number(String unit_number1){
            unit_number.setText(unit_number1);
        }

        private void setSum(String sum1){
            sum.setText(sum1);
        }
    }



}
