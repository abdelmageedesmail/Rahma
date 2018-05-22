package com.technology.team.rahmaapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.models.QuestionModel;

import java.util.ArrayList;

public class ParentAdapter  extends RecyclerView.Adapter<ParentAdapter.MyHolder>{
    Context context;
    ArrayList<QuestionModel> arrayList;
    public ParentAdapter(Context context,ArrayList<QuestionModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.parent_model,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvQuestion.setText(arrayList.get(position).getTitle());
        holder.answerList.setAdapter(new InnerAdapter(context,arrayList.get(position).getArrayList()));
        holder.answerList.setLayoutManager(new GridLayoutManager(context,2));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        RecyclerView answerList;
        public MyHolder(View itemView) {
            super(itemView);
            tvQuestion=itemView.findViewById(R.id.tvQuestion);
            answerList=itemView.findViewById(R.id.answerList);
        }
    }
}
