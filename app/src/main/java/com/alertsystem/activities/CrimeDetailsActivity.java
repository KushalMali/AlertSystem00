package com.alertsystem.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alertsystem.R;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityCrimeDetailsBinding;
import com.alertsystem.models.CrimeData;
import com.alertsystem.models.CrimeResponse;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.Util;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrimeDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityCrimeDetailsBinding binding;
    int size = 0;
    String selectedArea = "";
    ArrayList<String> areaList = new ArrayList<>();
    String[] colors = new String[]{"#FFA726", "#66BB6A", "#EF5350", "#29B6F6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CrimeDetailsActivity.this, R.layout.activity_crime_details);

        bindView();
        addListener();
    }

    private void bindView() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] areaName = getResources().getStringArray(R.array.area_array);
        areaList = new ArrayList<>();
        areaList.add("Select Area");
        areaList.addAll(Arrays.asList(areaName));

        Util.setAdapter(binding.areaSpinner, areaList);
    }

    private void addListener() {
        binding.areaSpinner.setOnItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> v, View view, int position, long id) {

        switch (v.getId()) {

            case R.id.areaSpinner:
                binding.pieChart.clearChart();
                if (position > 0) {
                    selectedArea = v.getItemAtPosition(position).toString();
                    getCrimeData(selectedArea);
                    break;
                } else {
                    Toast.makeText(CrimeDetailsActivity.this, "Please Select Area!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(CrimeDetailsActivity.this, "Please Select Area!", Toast.LENGTH_SHORT).show();
    }

    public void getCrimeData(String selectedArea) {

        binding.ll1.setVisibility(View.GONE);
        binding.ll2.setVisibility(View.GONE);
        binding.ll3.setVisibility(View.GONE);
        binding.ll4.setVisibility(View.GONE);

        if (InternetConnection.checkConnection(CrimeDetailsActivity.this)) {

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            ApiInterface api = ApiClient.getApiService();

            Call<CrimeResponse> call = api.getCrimeData(selectedArea);
            call.enqueue(new Callback<CrimeResponse>() {
                @Override
                public void onResponse(@NonNull Call<CrimeResponse> call, @NonNull Response<CrimeResponse> response) {
                    //Dismiss Dialog
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            List<CrimeData> crimeResponse = response.body().getData();

                            Log.e("onSuccess===", crimeResponse.get(0).toString());

                            size = crimeResponse.size();

                            for (int i = 0; i < crimeResponse.size(); i++) {

                                //Float val = Float.parseFloat(crimeResponse.get(i).getCcount()) / 100;
                                binding.pieChart.addPieSlice(
                                        new PieModel(
                                                ""+crimeResponse.get(i).getCtype(),
                                                Float.parseFloat(crimeResponse.get(i).getCcount()),
                                                Color.parseColor(colors[i])));
                            }

                            setData(crimeResponse);

                        } else {
                            Toast.makeText(CrimeDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Response===", response.body().getMessage());
                            binding.pieChart.clearChart();
                        }
                    } else {
                        Log.e("onEmptyResponse===", "Returned empty response");
                        Toast.makeText(CrimeDetailsActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CrimeResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(CrimeDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(CrimeDetailsActivity.this, "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(List<CrimeData> crimeResponse) {
        binding.crimeGraph.setVisibility(View.VISIBLE);

        /*pieChart.addPieSlice(
                new PieModel(
                        "Crime Type",
                        Float.parseFloat(cCount),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        10,
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        40,
                        Color.parseColor("#29B6F6")));*/

        if (size == 1) {
            binding.ll1.setVisibility(View.VISIBLE);
            binding.ctype1.setText(crimeResponse.get(0).getCtype() + " : " + crimeResponse.get(0).getCcount()+"\n Des:"+crimeResponse.get(0).getdes());
        } else if (size == 2) {
            binding.ll1.setVisibility(View.VISIBLE);
            binding.ll2.setVisibility(View.VISIBLE);
            binding.ctype1.setText(crimeResponse.get(0).getCtype() + " : " + crimeResponse.get(0).getCcount()+"\n Des:"+crimeResponse.get(0).getdes());
            binding.ctype2.setText(crimeResponse.get(1).getCtype() + " : " + crimeResponse.get(1).getCcount()+"\n Des:"+crimeResponse.get(1).getdes());
        } else if (size == 3) {
            binding.ll1.setVisibility(View.VISIBLE);
            binding.ll2.setVisibility(View.VISIBLE);
            binding.ll3.setVisibility(View.VISIBLE);
            binding.ctype1.setText(crimeResponse.get(0).getCtype() + " : " + crimeResponse.get(0).getCcount()+"\n Des:"+crimeResponse.get(0).getdes());
            binding.ctype2.setText(crimeResponse.get(1).getCtype() + " : " + crimeResponse.get(1).getCcount()+"\n Des:"+crimeResponse.get(1).getdes());
            binding.ctype3.setText(crimeResponse.get(2).getCtype() + " : " + crimeResponse.get(2).getCcount()+"\n Des:"+crimeResponse.get(2).getdes());
        } else if (size == 4) {
            binding.ll1.setVisibility(View.VISIBLE);
            binding.ll2.setVisibility(View.VISIBLE);
            binding.ll3.setVisibility(View.VISIBLE);
            binding.ll4.setVisibility(View.VISIBLE);
            binding.ctype1.setText(crimeResponse.get(0).getCtype() + " : " + crimeResponse.get(0).getCcount()+"\n Des:"+crimeResponse.get(0).getdes());
            binding.ctype2.setText(crimeResponse.get(1).getCtype() + " : " + crimeResponse.get(1).getCcount()+"\n Des:"+crimeResponse.get(1).getdes());
            binding.ctype3.setText(crimeResponse.get(2).getCtype() + " : " + crimeResponse.get(2).getCcount()+"\n Des:"+crimeResponse.get(2).getdes());
            binding.ctype4.setText(crimeResponse.get(3).getCtype() + " : " + crimeResponse.get(3).getCcount()+"\n Des:"+crimeResponse.get(3).getdes());
        }

        binding.pieChart.startAnimation();
    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(CrimeDetailsActivity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }*/
}