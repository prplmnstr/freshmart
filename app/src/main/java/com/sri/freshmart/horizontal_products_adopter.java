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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class horizontal_products_adopter extends RecyclerView.Adapter<horizontal_products_adopter.ViewHolder> {

    private List<fragment_rv_model> product_modal_list;

    int discount_D;

    Context context;
    String homeid = dashboard_activity.id;
    String price,mrp,icon,name,unit,id_,number;

    DatabaseReference upadate_FB = FirebaseDatabase.getInstance().getReference("carts") ;
    DatabaseReference item_count,check_item_in_cart;

    public horizontal_products_adopter(List<fragment_rv_model> product_modal_list) {
        this.product_modal_list = product_modal_list;
    }

    @NonNull
    @Override
    public horizontal_products_adopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_of_the_day_items,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final horizontal_products_adopter.ViewHolder holder, int position) {
        price = product_modal_list.get(position).getPrice();
        mrp = product_modal_list.get(position).getMrp();
        icon = product_modal_list.get(position).getIcon();
        name = product_modal_list.get(position).getName_();
        unit = product_modal_list.get(position).getUnit();
        id_ = product_modal_list.get(position).getItem_id();


       holder.setHorizontal_products_image(icon);



        int m,p;
        m= Integer.parseInt(mrp);
        p = Integer.parseInt(price);
        discount_D = (m-p)*100/m;

       holder.setHP_text1("₹"+price);
       holder.setHP_text2("₹"+mrp);
       holder.setDiscount_text(String.valueOf(discount_D)+"% off");
       holder.setItemid(id_);
       holder.setDod_icon(icon);
       holder.setHP_text3(name);
       holder.setHP_text4(unit);


       check_item_in_cart1(holder.itemid.getText().toString(),holder);



       holder.add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (dashboard_activity.id.equals("CKDA00")) {
                   Toast toast = Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT);
                   toast.show();
                   Intent home = new Intent(context, login_activity.class);
                   context.startActivity(home);


               } else {
                   Toast toast = Toast.makeText(context, "item added to cart", Toast.LENGTH_SHORT);
                   toast.show();
                   holder.add.setVisibility(View.INVISIBLE);
                   holder.plus.setVisibility(View.VISIBLE);
                   holder.minus.setVisibility(View.VISIBLE);
                   holder.unit_number.setText("1");
                   holder.unit_number.setVisibility(View.VISIBLE);
                   update_to_cart(holder.HP_text2.getText().toString().replaceAll("₹", "").trim(),
                           holder.HP_text1.getText().toString().replaceAll("₹", "").trim(),
                           holder.HP_text3.getText().toString(),
                           holder.HP_text4.getText().toString(),
                           holder.unit_number.getText().toString(),
                           holder.itemid.getText().toString(),
                           holder.dod_icon.getText().toString());
               }
           }
       });

       holder.plus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int x = Integer.parseInt(holder.unit_number.getText().toString());
               holder.unit_number.setText(String.valueOf(x+1));
               update_to_cart(holder.HP_text2.getText().toString().replaceAll("₹","").trim(),
                       holder.HP_text1.getText().toString().replaceAll("₹","").trim(),
                       holder.HP_text3.getText().toString(),
                       holder.HP_text4.getText().toString(),
                       holder.unit_number.getText().toString(),
                       holder.itemid.getText().toString(),
                       holder.dod_icon.getText().toString());
           }
       });
       holder.minus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int x = Integer.parseInt(holder.unit_number.getText().toString());
               if(x==1) {
                   remove_from_cart(holder.itemid.getText().toString());
                   Toast toast = Toast.makeText(context,"item removed from cart",Toast.LENGTH_SHORT);
                   toast.show();
                   holder.add.setVisibility(View.VISIBLE);
                   holder.plus.setVisibility(View.INVISIBLE);
                   holder.minus.setVisibility(View.INVISIBLE);
                   holder.unit_number.setVisibility(View.INVISIBLE);
               }
               else{

                   holder.unit_number.setText(String.valueOf(x-1));
                   update_to_cart(holder.HP_text2.getText().toString().replaceAll("₹","").trim(),
                           holder.HP_text1.getText().toString().replaceAll("₹","").trim(),
                           holder.HP_text3.getText().toString(),
                           holder.HP_text4.getText().toString(),
                           holder.unit_number.getText().toString(),
                           holder.itemid.getText().toString(),
                           holder.dod_icon.getText().toString());
               }
           }
       });

       holder.item_layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context,product_detail_activity.class);
               intent.putExtra("id", holder.itemid.getText().toString() );
               context.startActivity(intent);
           }
       });

    }
    private void check_item_in_cart1(final String item_id, final ViewHolder holder) {
        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts");
        Query query = check_item_in_cart.orderByKey().equalTo(homeid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check_item_in_cart2(item_id,holder);
                }
                else {
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void check_item_in_cart2(final String itemid, final ViewHolder holder) {
        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeid);
        Query query = check_item_in_cart.orderByKey().equalTo(itemid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check_item_in_cart3(itemid,holder);
                    holder.add.setVisibility(View.INVISIBLE);
                    holder.plus.setVisibility(View.VISIBLE);
                    holder.minus.setVisibility(View.VISIBLE);
                    holder.unit_number.setVisibility(View.VISIBLE);
                }
                else {
                    holder.add.setVisibility(View.VISIBLE);
                    holder.plus.setVisibility(View.INVISIBLE);
                    holder.minus.setVisibility(View.INVISIBLE);
                    holder.unit_number.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check_item_in_cart3(String itemid, final ViewHolder holder) {

        check_item_in_cart = FirebaseDatabase.getInstance().getReference("carts").child(homeid).child(itemid);
        check_item_in_cart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number = snapshot.child("unit_number").getValue().toString();
                holder.unit_number.setText(number);





            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }

        });

    }

    private void remove_from_cart(String itemid) {
        upadate_FB.child(homeid).child(itemid).removeValue();
    }

    private void update_to_cart(String mrp_,String price_,String name_,String unit_,String unit_n,String itemid,String dod_icon) {


        add_to_cart item = new add_to_cart(mrp_,price_,name_,dod_icon,unit_n,unit_,itemid);
        upadate_FB.child(homeid).child(itemid).setValue(item);

    }


    @Override
    public int getItemCount() {
        return product_modal_list.size()-1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView Horizontal_products_image;
        private TextView HP_text1, HP_text2, HP_text3,HP_text4,discount_text,unit_number,itemid,dod_icon;

        private Button add,plus,minus;
        private ConstraintLayout item_layout;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            Horizontal_products_image = itemView.findViewById(R.id.dod_image);
           HP_text1 = itemView.findViewById(R.id.dod_text1);
           HP_text2 = itemView.findViewById(R.id.dod_text2);
           HP_text2.setPaintFlags(HP_text2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           HP_text3 = itemView.findViewById(R.id.dod_text3);
           HP_text4 = itemView.findViewById(R.id.dod_text4);
           discount_text = itemView.findViewById(R.id.discount_text_DOD);
           unit_number = itemView.findViewById(R.id.unit_num_DOD);
           add = itemView.findViewById(R.id.add_button_DOD);
           plus = itemView.findViewById(R.id.plus_DOD);
           minus = itemView.findViewById(R.id.minus_DOD);
           item_layout = itemView.findViewById(R.id.DOD_layout);
           itemid = itemView.findViewById(R.id.item_id_DOD);
           dod_icon = itemView.findViewById(R.id.dod_icon_dummy);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(itemView.getContext(),category_activity.class);
                   intent.putExtra("i", "offers");

                   itemView.getContext().startActivity(intent);
               }
           });


        }
        private void setHorizontal_products_image(String url){
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(Horizontal_products_image);
        }
        private void setHP_text1(String text){
            HP_text1.setText(text);
        }

        private void setHP_text2(String text){
            HP_text2.setText(text);
        }
        private void setHP_text3(String text){
            HP_text3.setText(text);
        }
        private void setHP_text4(String text){
            HP_text4.setText(text);
        }
        private void setDiscount_text(String discount_text1){
            discount_text.setText(discount_text1);
        }
        private void setItemid(String itemid1){
            itemid.setText(itemid1);
        }

        private void setDod_icon(String dod_icon1){
            dod_icon.setText(dod_icon1);
        }
    }

}
