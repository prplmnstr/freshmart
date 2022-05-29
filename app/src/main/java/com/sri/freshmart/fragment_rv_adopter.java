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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class fragment_rv_adopter extends RecyclerView.Adapter<fragment_rv_adopter.ViewHolder> {

    private List<fragment_rv_model> fragment_rv_modelList;
    private int discount_D;

    Context context;
    String homeid = dashboard_activity.id;
    String price,mrp,icon,name,unit,id_,number;

    DatabaseReference upadate_FB = FirebaseDatabase.getInstance().getReference("carts") ;
    DatabaseReference check_item_in_cart;

    public fragment_rv_adopter(List<fragment_rv_model> fragment_rv_modelList) {
        this.fragment_rv_modelList = fragment_rv_modelList;

    }

    public List<fragment_rv_model> getFragment_rv_modelList() {
        return fragment_rv_modelList;
    }

    public void setFragment_rv_modelList(List<fragment_rv_model> fragment_rv_modelList) {
        this.fragment_rv_modelList = fragment_rv_modelList;
    }







    @NonNull
    @Override
    public fragment_rv_adopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragments_rv_items,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final fragment_rv_adopter.ViewHolder holder, int position) {
         icon = fragment_rv_modelList.get(position).getIcon();
        price = fragment_rv_modelList.get(position).getPrice();
        mrp = fragment_rv_modelList.get(position).getMrp();
         name = fragment_rv_modelList.get(position).getName_();
        unit = fragment_rv_modelList.get(position).getUnit();
        id_ = fragment_rv_modelList.get(position).getItem_id();






       int m,p;
       m= Integer.parseInt(mrp);
       p = Integer.parseInt(price);
       discount_D = (m-p)*100/m;


        holder.seticon_(icon);
        holder.setPrice("₹"+price);
        holder.setMrp("₹"+mrp);
        holder.setName_(name);
        holder.setItem_id(id_);
        holder.setRv_icon(icon);
       holder.setUnit(unit);

        holder.setDiscount_(String.valueOf(discount_D)+"% off");

        if(!holder.mrp.getText().toString().equals(holder.price.getText().toString())){
            holder.mrp.setVisibility(View.VISIBLE);
            holder.discount_.setVisibility(View.VISIBLE);
        }






       check_item_in_cart1(holder.item_id.getText().toString(),holder);


     
        //ADD button click //

      /*
*/
       holder.add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(dashboard_activity.id.equals("CKDA00")) {
                   Toast toast = Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT);
                   toast.show();
                   Intent home = new Intent(context, login_activity.class);
                  context.startActivity(home);

                   ((AppCompatActivity)context).finish();



               }else{
                   if(holder.item_id.getText().toString().charAt(0)!='g'){
                       holder.plus.setEnabled(false);
                   }
                   Toast toast = Toast.makeText(context,"item added to cart",Toast.LENGTH_SHORT);
                   toast.show();
                   holder.add.setVisibility(View.INVISIBLE);
                   holder.plus.setVisibility(View.VISIBLE);
                   holder.minus.setVisibility(View.VISIBLE);
                   holder.unit_number.setText("1");
                   holder.unit_number.setVisibility(View.VISIBLE);
                   update_to_cart(holder.mrp.getText().toString().replaceAll("₹","").trim(),
                           holder.price.getText().toString().replaceAll("₹","").trim(),
                           holder.name_.getText().toString(),
                           holder.unit.getText().toString(),
                           holder.unit_number.getText().toString(),
                           holder.item_id.getText().toString(),
                           holder.rv_icon.getText().toString());
               }


           }
       });

        //ADD button click //

        // plus button click //
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(holder.unit_number.getText().toString());
                holder.unit_number.setText(String.valueOf(x+1));
                update_to_cart(holder.mrp.getText().toString().replaceAll("₹","").trim(),
                        holder.price.getText().toString().replaceAll("₹","").trim(),
                        holder.name_.getText().toString(),
                        holder.unit.getText().toString(),
                        holder.unit_number.getText().toString(),
                        holder.item_id.getText().toString(),
                        holder.rv_icon.getText().toString());
            }
        });

        // plus button click //

        //minus button click  //

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(holder.unit_number.getText().toString());
                if(x==1) {
                    remove_from_cart(holder.item_id.getText().toString());
                    Toast toast = Toast.makeText(context,"item removed from cart",Toast.LENGTH_SHORT);
                    toast.show();
                    holder.add.setVisibility(View.VISIBLE);
                    holder.plus.setVisibility(View.INVISIBLE);
                    holder.minus.setVisibility(View.INVISIBLE);
                    holder.unit_number.setVisibility(View.INVISIBLE);
                }
                else{
                    
                    holder.unit_number.setText(String.valueOf(x-1));
                    update_to_cart(holder.mrp.getText().toString().replaceAll("₹","").trim(),
                            holder.price.getText().toString().replaceAll("₹","").trim(),
                            holder.name_.getText().toString(),
                            holder.unit.getText().toString(),
                            holder.unit_number.getText().toString(),
                            holder.item_id.getText().toString(),
                            holder.rv_icon.getText().toString());


                }
            }
        });

        //minus button click  //

        //items layout click //


       holder.items_layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               ///////

               Intent intent = new Intent(context,product_detail_activity.class);
               intent.putExtra("id", holder.item_id.getText().toString());
               context.startActivity(intent);

////////




               ////delete

          //    getdata(holder);  //////



            //   change_cat(holder);

           }
       });

        //items layout click //

    }

    private void change_cat(ViewHolder holder) {
        FirebaseFirestore.getInstance().collection("global_items").document(holder.item_id.getText().toString()).update("category","Insecticides");
        Toast.makeText(context,"updated",Toast.LENGTH_SHORT).show();
    }

    private void getdata(ViewHolder h) {




        DatabaseReference db = FirebaseDatabase.getInstance().getReference("global_items").child(h.item_id.getText().toString());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                add_to_cat(snapshot.child("name").getValue().toString(),
                        snapshot.child("id").getValue().toString(),
                        snapshot.child("icon1").getValue().toString(),
                        snapshot.child("mrp").getValue().toString(),
                        snapshot.child("price").getValue().toString(),
                        snapshot.child("unit").getValue().toString()
                        );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void add_to_cat(String name, String id, String icon, String mrp, String price, String unit) {


       DatabaseReference add_to_cat = FirebaseDatabase.getInstance().getReference("offers_items").child("4");
        add_to_category new_cat_ = new add_to_category(icon,id,mrp,name,price,unit);
        add_to_cat.child(id).setValue(new_cat_);
        Toast.makeText(context, "ADDED Catrgory", Toast.LENGTH_SHORT).show();
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

    private void remove_from_cart(String itemid){

        upadate_FB.child(homeid).child(itemid).removeValue();
    }

    private void update_to_cart(String mrp_,String price_,String name_,String unit_,String unit_n,String itemid,String rv_icon) {


        add_to_cart item = new add_to_cart(mrp_,price_,name_,rv_icon,unit_n,unit_,itemid);
        upadate_FB.child(homeid).child(itemid).setValue(item);

    }

    @Override
    public int getItemCount() {
        return fragment_rv_modelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView price, mrp, unit, name_, discount_,unit_number,item_id,rv_icon;
        ImageView icon_;
        ConstraintLayout items_layout;
        Button add,plus,minus;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price_text);
            mrp = itemView.findViewById(R.id.mrp_text);
            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            icon_ = itemView.findViewById(R.id.item_icon);
            unit = itemView.findViewById(R.id.unit_text);
            name_ = itemView.findViewById(R.id.item_name_text);
            discount_ = itemView.findViewById(R.id.discount_text);
            items_layout = itemView.findViewById(R.id.items_layout);
            add = itemView.findViewById(R.id.ADD_Button);
            plus = itemView.findViewById(R.id.plus_button);
            minus = itemView.findViewById(R.id.minus_button);
            unit_number = itemView.findViewById(R.id.unit_number);
            item_id = itemView.findViewById(R.id.item_id_category);
            rv_icon = itemView.findViewById(R.id.rv_icon);

        }

        public void setPrice(String price1) {
            price.setText(price1);

        }

        public void setMrp(String mrp1) {
            mrp.setText(mrp1);

        }

        public void seticon_(String url) {
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder)).into(icon_);

        }

       public void setName_(String name_1){
            name_.setText(name_1);
       }



        public void setUnit(String unit1) {
            unit.setText(unit1);

        }

        public void setDiscount_(String discount_1) {
            if(discount_1.equals("0% off")){
                discount_.setVisibility(View.INVISIBLE);
                mrp.setVisibility(View.INVISIBLE);
            }

                discount_.setText(discount_1);

        }

        public void setItem_id(String item_id1){
            item_id.setText(item_id1);
        }

        public void setRv_icon(String rv_icon1){
            rv_icon.setText(rv_icon1);
        }

    }




}
