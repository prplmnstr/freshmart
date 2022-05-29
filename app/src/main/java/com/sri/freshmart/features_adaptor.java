package com.sri.freshmart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class features_adaptor extends RecyclerView.Adapter<features_adaptor.ViewHolder>{

    private List<features_model> features_modelList;

    public features_adaptor(List<features_model> features_modelList) {
        this.features_modelList = features_modelList;
    }


    @NonNull
    @Override
    public features_adaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.features_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull features_adaptor.ViewHolder holder, int position) {
       String feature = features_modelList.get(position).getFeatures();
       holder.setFeatures(feature);
    }

    @Override
    public int getItemCount() {
        return features_modelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView features;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            features = itemView.findViewById(R.id.features_text);
        }

        public void setFeatures(String features1){
            features.setText(features1);
        }
    }
}
