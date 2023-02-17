package com.alertsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alertsystem.R;
import com.alertsystem.databinding.RowItemBlockBinding;
import com.alertsystem.models.BlockData;

import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    List<BlockData> blockDataList;

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowItemBlockBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_item_block, parent, false);

        return new BlockViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BlockAdapter.BlockViewHolder holder, int position) {

        BlockData blockData = blockDataList.get(position);

        if (blockData.getStatus().equalsIgnoreCase("0")) {
            holder.binding.flImageBg.setBackgroundResource(R.color.green);
        } else {
            holder.binding.flImageBg.setBackgroundResource(R.color.red);
        }

        holder.binding.setBlockData(blockData);
    }

    @Override
    public int getItemCount() {
        return blockDataList.size() > 0 ? blockDataList.size() : 0;
    }

    public void setBlockList(List<BlockData> blockDataList) {
        this.blockDataList = blockDataList;
        notifyDataSetChanged();
    }

    public static class BlockViewHolder extends RecyclerView.ViewHolder {

        private RowItemBlockBinding binding;

        public BlockViewHolder(@NonNull RowItemBlockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
