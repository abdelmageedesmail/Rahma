package com.technology.team.rahmaapp.adapters.donaterAdapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.team.rahmaapp.R;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyHolder>{
    Context context;
    public AddressAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.address_item,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpDetails();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

       private void showPopUpDetails(){
            Dialog d=new Dialog(context);
            d.setContentView(R.layout.popup_address_details);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
            d.show();
        }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardDetails;
        public MyHolder(View itemView) {
            super(itemView);
            cardDetails=itemView.findViewById(R.id.cardDetails);
        }
    }
}
