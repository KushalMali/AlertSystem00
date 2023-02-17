package com.alertsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alertsystem.R;
import com.alertsystem.databinding.RowItemBlockBinding;
import com.alertsystem.databinding.RowItemHouseBinding;
import com.alertsystem.models.BlockData;
import com.alertsystem.models.House;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private List<House> houseList;

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowItemHouseBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_item_house, parent, false);

        return new HouseAdapter.HouseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {

        House house = houseList.get(position);
        holder.binding.setHouseItem(house);
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
        notifyDataSetChanged();
    }

    public static class HouseViewHolder extends RecyclerView.ViewHolder {

        RowItemHouseBinding binding;

        public HouseViewHolder(RowItemHouseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
