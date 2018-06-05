package com.technology.team.rahmaapp.adapters.donaterAdapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.AdressListModel;
import com.technology.team.rahmaapp.models.MyDonationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyDonationAdapter extends RecyclerView.Adapter<MyDonationAdapter.MyHolder> {
    Context context;
    ArrayList<MyDonationModel> arrayList;
    private Dialog d;

    public MyDonationAdapter(Context context, ArrayList<MyDonationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_donation_item, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopUpDetails(holder.getAdapterPosition());
            }
        });
        holder.tvTitle.setText(arrayList.get(position).getType());
        holder.tvAddress.setText(arrayList.get(position).getQuanitity());
        if (arrayList.get(position).getType().equals(context.getString(R.string.food))){
            holder.donationType.setImageResource(R.mipmap.food);
        }else {
            holder.donationType.setImageResource(R.mipmap.accessories);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardDetails;
        TextView tvTitle,tvAddress;
        ImageView donationType;

        public MyHolder(View itemView) {
            super(itemView);
            cardDetails = itemView.findViewById(R.id.cardDetails);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            donationType=itemView.findViewById(R.id.donationType);
        }
    }
}
