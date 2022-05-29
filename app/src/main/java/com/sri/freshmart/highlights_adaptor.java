package com.sri.freshmart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class highlights_adaptor extends RecyclerView.Adapter<highlights_adaptor.ViewHolder> {

    private List<highlights_model> highlightsModelList;

    public highlights_adaptor(List<highlights_model> highlightsModelList) {
        this.highlightsModelList = highlightsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlights_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String tag_  = highlightsModelList.get(position).getTag();
       String ans_  =highlightsModelList.get(position).getAns();
       holder.setTag(tag_);
       holder.setAns(ans_);
    }

    @Override
    public int getItemCount() {
        return highlightsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tag,ans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.tag);
            ans = itemView.findViewById(R.id.ans);

        }

        public  void setTag(String tag1){
            tag.setText(tag1);
        }

        public void setAns(String ans1){
            ans.setText(ans1);
        }
    }
}
