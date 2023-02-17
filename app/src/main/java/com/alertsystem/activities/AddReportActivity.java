package com.alertsystem.activities;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alertsystem.R;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityAddReportBinding;
import com.alertsystem.models.CommonResponse;
import com.alertsystem.utils.Constants;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.SharedPreferences;
import com.alertsystem.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ActivityAddReportBinding binding;
    ArrayList<String> crimeList = null, areaList = null;
    String selectedArea = "", selectedCrime = "", userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AddReportActivity.this, R.layout.activity_add_report);

        bindView();
        addListener();
    }

    private void bindView() {
        SharedPreferences.getInstance().initializeSharePreferences(AddReportActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userid = SharedPreferences.getInstance().fetchSharedPreferenesString(Constants.PREF_ID, "");

        String[] areaName = getResources().getStringArray(R.array.area_array);
        areaList = new ArrayList<>();
        areaList.add("Select Area");
        areaList.addAll(Arrays.asList(areaName));

        Util.setAdapter(binding.areaSpinner, areaList);

        String[] crimeName = getResources().getStringArray(R.array.crime_type_array);
        crimeList = new ArrayList<>();
        crimeList.add("Select Crime Type");
        crimeList.addAll(Arrays.asList(crimeName));

        Util.setAdapter(binding.crimeSpinner, crimeList);
    }

    private void addListener() {
        binding.areaSpinner.setOnItemSelectedListener(this);
        binding.crimeSpinner.setOnItemSelectedListener(this);
        binding.btnAddCrime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddCrime:
                if (binding.edtCrimeRate.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Crime Rate cannot be Blank!", Toast.LENGTH_LONG).show();
                    binding.edtCrimeRate.setError("Crime Rate cannot be Blank!");

                    return;

                }else if (binding.edtdesinfo.getText().toString().length() == 0) {

                Toast.makeText(getApplicationContext(),
                        "Crime descrption cannot be Blank!", Toast.LENGTH_LONG).show();
                binding.edtCrimeRate.setError("Crime descrption cannot be Blank!");

                return;
                } else if (selectedArea.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please Select Crime Area!", Toast.LENGTH_LONG).show();
                } else if (selectedCrime.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please Select Crime Type!", Toast.LENGTH_LONG).show();
                } else {
                    addCrime();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> v, View view, int position, long id) {

        switch (v.getId()) {

            case R.id.areaSpinner:
                if (position > 0) {
                    selectedArea = v.getItemAtPosition(position).toString();
                    //Toast.makeText(AddReportActivity.this, selectedArea + " Selected", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.crimeSpinner:
                if (position > 0) {
                    selectedCrime = v.getItemAtPosition(position).toString();
                    //Toast.makeText(AddReportActivity.this, selectedCrime + " Selected", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(AddReportActivity.this, "Please Select!", Toast.LENGTH_SHORT).show();
    }

    public void addCrime() {

        if (InternetConnection.checkConnection(AddReportActivity.this)) {

            final String crimeRate = binding.edtCrimeRate.getText().toString();
            final String des = binding.edtdesinfo.getText().toString();

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            //Creating an object of our api interface
            ApiInterface api = ApiClient.getApiService();

            Call<CommonResponse> call = api.addCrime(userid, selectedArea, selectedCrime, crimeRate,des);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                    //Dismiss Dialog
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            Log.d("onSuccess===", response.body().toString());
                            Toast.makeText(AddReportActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            binding.edtCrimeRate.getText().clear();
                            binding.edtdesinfo.getText().clear();
                            Util.setAdapter(binding.areaSpinner, areaList);
                            Util.setAdapter(binding.crimeSpinner, crimeList);
                        } else {
                            Toast.makeText(AddReportActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("onEmptyResponse===", "Returned empty response");
                        Toast.makeText(AddReportActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AddReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(AddReportActivity.this, "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onBackPressed() {
        areaList.clear();
        crimeList.clear();

        Intent intent = new Intent(AddReportActivity.this, DashboardActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
    }*/
}