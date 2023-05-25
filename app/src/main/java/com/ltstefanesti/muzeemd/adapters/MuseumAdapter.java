package com.ltstefanesti.muzeemd.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltstefanesti.muzeemd.R;
import com.ltstefanesti.muzeemd.activities.DetailActivity;
import com.ltstefanesti.muzeemd.models.MuseumDataClass;

import java.util.ArrayList;
import java.util.List;

public class MuseumAdapter extends RecyclerView.Adapter<MuseumViewHolder> {
    private final Context context;
    private List<MuseumDataClass> dataList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MuseumAdapter(Context context, List<MuseumDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MuseumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MuseumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuseumViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageOne()).into(holder.imgList);
        holder.tvName.setText(dataList.get(position).getName());
        holder.tvAddress.setText(dataList.get(position).getAddress());
        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Title", dataList.get(holder.getBindingAdapterPosition()).getName());
            intent.putExtra("Image", dataList.get(holder.getBindingAdapterPosition()).getImageOne());
            intent.putExtra("Image1", dataList.get(holder.getBindingAdapterPosition()).getImageTwo());
            intent.putExtra("Image2", dataList.get(holder.getBindingAdapterPosition()).getImageThree());
            intent.putExtra("Address", dataList.get(holder.getBindingAdapterPosition()).getAddress());
            intent.putExtra("Phone", dataList.get(holder.getBindingAdapterPosition()).getPhoneNumber());
            intent.putExtra("Web", dataList.get(holder.getBindingAdapterPosition()).getWebsite());
            intent.putExtra("Email", dataList.get(holder.getBindingAdapterPosition()).getEmail());
            intent.putExtra("Program", dataList.get(holder.getBindingAdapterPosition()).getHoursOfOperation());
            intent.putExtra("Description", dataList.get(holder.getBindingAdapterPosition()).getDescription());
            intent.putExtra("Latitude", dataList.get(holder.getBindingAdapterPosition()).getLatitude());
            intent.putExtra("Longitude", dataList.get(holder.getBindingAdapterPosition()).getLongitude());
            intent.putExtra("Key", dataList.get(holder.getBindingAdapterPosition()).getKey());
            if (mListener != null) {
                mListener.onItemClick(position);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<MuseumDataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MuseumViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvAddress;
    ImageView imgList;
    CardView recCard;

    public MuseumViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name_list);
        tvAddress = itemView.findViewById(R.id.tv_address_list);
        imgList = itemView.findViewById(R.id.img_list);
        recCard = itemView.findViewById(R.id.rec_card);
    }
}