package com.sri.freshmart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class items_fragment extends Fragment {

   private RecyclerView recyclerView;

   private List<fragment_rv_model> fragment_rv_modelList;

   private fragment_rv_adopter f_rv_Adopter;

   DatabaseReference get_rv_items;

   String homeid = dashboard_activity.id;



    public items_fragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.items_fragment, container, false);
       recyclerView = view.findViewById(R.id.rv_fragment);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager2);

        fragment_rv_modelList = new ArrayList<fragment_rv_model>();
        f_rv_Adopter = new fragment_rv_adopter(fragment_rv_modelList);
        recyclerView.setAdapter(f_rv_Adopter);


        get_rv_items = FirebaseDatabase.getInstance().getReference( getArguments().getString("a")).child(getArguments().getString("m"));//got m from pager adopter to get data from firebase
        get_rv_items.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Data:snapshot.getChildren()){
                    fragment_rv_modelList.add(new fragment_rv_model(Data.child("price").getValue().toString(),Data.child("mrp").getValue().toString(),Data.child("icon").getValue().toString(),Data.child("name").getValue().toString(),Data.child("unit").getValue().toString(),Data.child("id").getValue().toString()));
                }
                f_rv_Adopter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







       return view;
    }
}