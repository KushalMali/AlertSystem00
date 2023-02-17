package com.alertsystem.activities;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alertsystem.R;
import com.alertsystem.adapters.BlockAdapter;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityCheckReportBinding;
import com.alertsystem.models.BlockData;
import com.alertsystem.models.BlockResponse;
import com.alertsystem.utils.Constants;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.SharedPreferences;
import com.alertsystem.utils.SpacesItemDecoration;
import com.alertsystem.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityCheckReportBinding binding;
    String selectedArea = "", uid = "";
    ArrayList<String> areaList = new ArrayList<>();
    List<BlockData> blockResponseList = new ArrayList<>();
    BlockAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CheckReportActivity.this, R.layout.activity_check_report);

        bindView();
        addListener();
    }

    private void bindView() {

        SharedPreferences.getInstance().initializeSharePreferences(CheckReportActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        uid = SharedPreferences.getInstance().fetchSharedPreferenesString(Constants.PREF_ID, "");

        String[] areaName = getResources().getStringArray(R.array.area_array);
        areaList = new ArrayList<>();
        areaList.add("Select Area");
        areaList.addAll(Arrays.asList(areaName));

        Util.setAdapter(binding.areaSpinner, areaList);

        GridLayoutManager gridLayoutManagerVertical =
                new GridLayoutManager(this,
                        2,
                        RecyclerView.VERTICAL,
                        false);

        binding.rvBlockData.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvBlockData.setLayoutManager(gridLayoutManagerVertical);
        int spaceInPixels = 5;
        binding.rvBlockData.addItemDecoration(new SpacesItemDecoration(spaceInPixels));
    }

    private void addListener() {
        binding.areaSpinner.setOnItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> v, View view, int position, long id) {

        switch (v.getId()) {

            case R.id.areaSpinner:
                if (position > 0) {
                    selectedArea = v.getItemAtPosition(position).toString();
                    getBlockData();
                    break;
                } else {
                    Toast.makeText(CheckReportActivity.this, "Please Select Area!", Toast.LENGTH_SHORT).show();
                    clearData();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBlockData() {

        if (InternetConnection.checkConnection(CheckReportActivity.this)) {

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            ApiInterface api = ApiClient.getApiService();

            Call<BlockResponse> call = api.getBlockData(selectedArea, uid);
            call.enqueue(new Callback<BlockResponse>() {
                @Override
                public void onResponse(@NonNull Call<BlockResponse> call, @NonNull Response<BlockResponse> response) {
                    //Dismiss Dialog
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            Toast.makeText(CheckReportActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //Log.e("onSuccess===", blockResponseList.toString());

                            blockResponseList = response.body().getData();
                            adapter = new BlockAdapter();
                            binding.rvBlockData.setAdapter(adapter);
                            adapter.setBlockList(blockResponseList);

                        } else {
                            Toast.makeText(CheckReportActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            clearData();
                            //Log.e("Response===", response.body().getMessage());

                            /*blockResponseList = response.body().getData();
                            adapter = new BlockAdapter(CheckReportActivity.this, blockResponseList);
                            rvBlockData.setAdapter(adapter);*/
                        }
                    } else {
                        Log.e("onEmptyResponse===", "Returned empty response");
                        Toast.makeText(CheckReportActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BlockResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(CheckReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(CheckReportActivity.this, "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData() {
        if (blockResponseList.size() != 0) {
            blockResponseList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(CheckReportActivity.this, "Please Select Area!", Toast.LENGTH_SHORT).show();
    }

}