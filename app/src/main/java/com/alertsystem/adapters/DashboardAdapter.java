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
import com.alertsystem.databinding.RowItemDashboardBinding;
import com.alertsystem.models.BlockData;
import com.alertsystem.models.Dashboard;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private List<Dashboard> dashboardMenuItems;

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowItemDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_item_dashboard, parent, false);

        return new DashboardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DashboardAdapter.DashboardViewHolder holder, int position) {

        Dashboard dashboard = dashboardMenuItems.get(position);
        holder.binding.setDashboardItem(dashboard);

        /*holder.designItem(dashboardMenuItems.get(position));
        holder.ivMenuIcon.setImageResource(dashboardMenuItems.get(position).getIcon());
        holder.tvMenuName.setText(dashboardMenuItems.get(position).getName());*/
    }

    @Override
    public int getItemCount() {
        return dashboardMenuItems.size();
    }

    public void setDashboardItemList(List<Dashboard> dashboardMenuItems) {
        this.dashboardMenuItems = dashboardMenuItems;
        notifyDataSetChanged();
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder{

        private RowItemDashboardBinding binding;
        public DashboardViewHolder(RowItemDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /*public void designItem(Dashboard dashboard) {
            tvMenuName.setText(dashboard.getName());
            ivMenuIcon.setImageResource(dashboard.getIcon());
        }*/
    }
}
