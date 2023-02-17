package com.alertsystem.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alertsystem.R;
import com.alertsystem.adapters.HouseAdapter;
import com.alertsystem.clickListeners.ClickListener;
import com.alertsystem.clickListeners.RecyclerTouchListener;
import com.alertsystem.databinding.ActivityHouseDetailsBinding;
import com.alertsystem.models.House;

import java.util.ArrayList;

public class HouseDetailsActivity extends AppCompatActivity {

    ActivityHouseDetailsBinding binding;
    HouseAdapter houseAdapter;
    private ArrayList<House> houseDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HouseDetailsActivity.this, R.layout.activity_house_details);

        bindView();
        addListener();
    }

    private void bindView() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setData();
    }

    private void setData() {
        String[] name = getResources().getStringArray
                (R.array.name_list);
        TypedArray image = getResources().obtainTypedArray
                (R.array.image_list);
        String[] mobile = getResources().getStringArray
                (R.array.contact_list);

        houseDetailsList = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            houseDetailsList.add(new House(name[i], mobile[i],
                    image.getResourceId(i, -1)));
        }

        image.recycle();
        GridLayoutManager gridLayoutManagerVertical =
                new GridLayoutManager(this,
                        1,
                        RecyclerView.VERTICAL,
                        false);

        binding.rvHouseDetails.setLayoutManager(new GridLayoutManager
                (this, 1));
        binding.rvHouseDetails.setLayoutManager(gridLayoutManagerVertical);

        /*StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvHouseDetails.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView*/

        houseAdapter = new HouseAdapter();
        binding.rvHouseDetails.setAdapter(houseAdapter);
        houseAdapter.setHouseList(houseDetailsList);
    }

    private void addListener() {
        binding.rvHouseDetails.addOnItemTouchListener(new RecyclerTouchListener(HouseDetailsActivity.this, binding.rvHouseDetails, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}