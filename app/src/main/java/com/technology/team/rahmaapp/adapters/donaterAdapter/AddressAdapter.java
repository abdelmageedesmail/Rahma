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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyHolder> {
    Context context;
    ArrayList<AdressListModel> arrayList;
    private Dialog d;

    public AddressAdapter(Context context, ArrayList<AdressListModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpDetails(holder.getAdapterPosition());
            }
        });
        holder.tvTitle.setText(arrayList.get(position).getTitle());
        holder.tvAddress.setText(context.getString(R.string.flatNumber)+" "+arrayList.get(position).getHouseNumber());
    }

    private void deleteOrder(final int position){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.uploading));
        progressDialog.show();
        AndroidNetworking.initialize(context);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.deleteUserAddress)
                .addBodyParameter("address_id",arrayList.get(position).getId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("status");
                            if (status.equals("1")){
                                d.dismiss();
                                arrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, arrayList.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void showPopUpDetails(final int position) {
        d = new Dialog(context);
        d.setContentView(R.layout.popup_address_details);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        d.getWindow().setLayout(width, height);
        TextView tvFloorNumber=d.findViewById(R.id.tvFloorNumber);
        TextView houseNumber=d.findViewById(R.id.houseNumber);
        TextView tvAddress=d.findViewById(R.id.tvAddress);
        ImageView delete=d.findViewById(R.id.delete);
        tvFloorNumber.setText(arrayList.get(position).getFloorNumber());
        tvAddress.setText(arrayList.get(position).getTitle());
        houseNumber.setText(arrayList.get(position).getHouseNumber());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOrder(position);
            }
        });
        d.show();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardDetails;
        TextView tvTitle,tvAddress;

        public MyHolder(View itemView) {
            super(itemView);
            cardDetails = itemView.findViewById(R.id.cardDetails);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvAddress=itemView.findViewById(R.id.tvAddress);
        }
    }
}
