package com.technology.team.rahmaapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.models.InnerModel;

import java.util.ArrayList;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.MyHolder>{
    Context context;
    ArrayList<InnerModel> arrayList;
    public InnerAdapter(Context context,ArrayList<InnerModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.inner_item,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.rdAnswer.setText(arrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        RadioButton rdAnswer;
        public MyHolder(View itemView) {
            super(itemView);
            rdAnswer=itemView.findViewById(R.id.rdAnswer);
        }
    }
}
