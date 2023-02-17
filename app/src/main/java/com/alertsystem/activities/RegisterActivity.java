package com.alertsystem.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alertsystem.R;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityRegisterBinding;
import com.alertsystem.models.CommonResponse;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterBinding binding;
    String userType = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);

        addListener();
    }

    private void addListener() {
        binding.btnRegister.setOnClickListener(this);
        binding.linkToLogin.setOnClickListener(this);
        binding.userRadioButton.setOnClickListener(this);
        binding.policeRadioButton.setOnClickListener(this);

        binding.userRadioButton.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:

                if (binding.nameRegister.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Name cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.nameRegister.setError("Name cannot be Blank");

                    return;

                } else if (binding.mobileRegister.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Mobile cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.mobileRegister.setError("Mobile cannot be Blank");

                    return;

                } else if (binding.emailRegister.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Email cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.emailRegister.setError("Email cannot be Blank");

                    return;

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        binding.emailRegister.getText().toString()).matches()) {
                    // Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email",
                            Toast.LENGTH_LONG).show();
                    binding.emailRegister.setError("Invalid Email");

                    return;

                } else if (binding.addressRegister.getText().toString().length() == 0) {
                    // Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Address cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.addressRegister.setError("Address cannot be Blank");

                    return;

                } else if (binding.passwordRegister.getText().length() <= 5) {

                    Toast.makeText(getApplicationContext(),
                            "Password must be 6 characters above",
                            Toast.LENGTH_LONG).show();
                    binding.passwordRegister.setError("Password must be 6 characters above");

                    return;

                } else if (!binding.passwordRegister.getText().toString()
                        .equals(binding.passwordRegister.getText().toString())) {

                    Toast.makeText(getApplicationContext(),
                            "Passwords does not match.", Toast.LENGTH_LONG).show();
                    binding.passwordRegister.setError("Passwords does not match.");

                    return;

                } else {
                    if (userType.equals("User")) {
                        registerUser();
                    } else {
                        registerPolice();
                    }
                }

                break;

            case R.id.linkToLogin:

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                finish();

                break;

            case R.id.userRadioButton:
                //Toast.makeText(RegisterActivity.this, userRadioButton.getText() + " Selected!", Toast.LENGTH_SHORT).show();
                binding.stationDetails.setVisibility(View.GONE);
                userType = "User";
                break;

            case R.id.policeRadioButton:
                //Toast.makeText(RegisterActivity.this, policeRadioButton.getText() + " Selected!", Toast.LENGTH_SHORT).show();
                binding.stationDetails.setVisibility(View.VISIBLE);
                userType = "Police";
                break;

            default:
                break;
        }

    }

    private void registerUser() {

        if (InternetConnection.checkConnection(getApplicationContext())) {

            final String name = binding.nameRegister.getText().toString();
            final String mobile = binding.mobileRegister.getText().toString();
            final String email = binding.emailRegister.getText().toString();
            final String address = binding.addressRegister.getText().toString();
            final String password = binding.passwordRegister.getText().toString();

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            ApiInterface api = ApiClient.getApiService();
            Call<CommonResponse> call = api.registerUser(name, mobile, email, address, password, userType);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("onEmptyResponse", "Returned empty response");
                        Toast.makeText(RegisterActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                    //Dismiss Dialog
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerPolice() {

        if (InternetConnection.checkConnection(getApplicationContext())) {

            final String name = binding.nameRegister.getText().toString();
            final String mobile = binding.mobileRegister.getText().toString();
            final String email = binding.emailRegister.getText().toString();
            final String address = binding.addressRegister.getText().toString();
            final String password = binding.passwordRegister.getText().toString();
            final String stationName = binding.stationNameRegister.getText().toString();
            final String stationMobile = binding.stationMobileRegister.getText().toString();
            final String stationAddress = binding.stationAddressRegister.getText().toString();

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            ApiInterface api = ApiClient.getApiService();
            Call<CommonResponse> call = api.registerPolice(name, mobile, email, address, password, userType, stationName, stationMobile, stationAddress);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(RegisterActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }
}