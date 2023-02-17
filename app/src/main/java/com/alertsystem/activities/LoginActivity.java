package com.alertsystem.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alertsystem.R;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityLoginBinding;
import com.alertsystem.models.LoginResponse;
import com.alertsystem.models.UserData;
import com.alertsystem.utils.Constants;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.SharedPreferences;
import com.alertsystem.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    String userType = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        init();
        addListener();
    }

    private void init() {
        SharedPreferences.getInstance().initializeSharePreferences(LoginActivity.this);
    }

    private void addListener() {
        binding.btnLogin.setOnClickListener(this);

        binding.btnLinkToRegister.setOnClickListener(this);
        binding.forgotPasswordLogin.setOnClickListener(this);

        binding.userSelected.setOnClickListener(this);
        binding.policeSelected.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:

                if (binding.emailLogin.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Email id cannot be Blank!", Toast.LENGTH_LONG).show();
                    binding.emailLogin.setError("Email cannot be Blank!");

                    return;

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        binding.emailLogin.getText().toString()).matches()) {

                    Toast.makeText(getApplicationContext(), "Invalid Email!",
                            Toast.LENGTH_LONG).show();
                    binding.emailLogin.setError("Invalid Email!");

                } else if (binding.passwordLogin.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Password cannot be Blank!", Toast.LENGTH_LONG).show();
                    binding.passwordLogin.setError("Password cannot be Blank!");

                    return;
                } else if (userType.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please Select User Type", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    login();
                }

                break;

            case R.id.btnLinkToRegister:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                finish();
                break;

            case R.id.forgotPasswordLogin:
                Intent fp = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(fp);
                overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                finish();
                break;

            case R.id.policeSelected:
                userType = "Police";
                binding.policeSelected.setBackgroundResource(R.drawable.button_drawable);
                binding.userSelected.setBackgroundResource(R.drawable.empty_border);
                break;

            case R.id.userSelected:
                userType = "User";
                binding.userSelected.setBackgroundResource(R.drawable.button_drawable);
                binding.policeSelected.setBackgroundResource(R.drawable.empty_border);
                break;

            default:
                break;
        }

    }

    public void login() {

        if (InternetConnection.checkConnection(LoginActivity.this)) {

            final String email = binding.emailLogin.getText().toString();
            final String password = binding.passwordLogin.getText().toString();

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            //Creating an object of our api interface
            ApiInterface api = ApiClient.getApiService();

            Call<LoginResponse> call = api.loginUser(email, password, userType);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    //Dismiss Dialog
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            Log.d("onSuccess===", response.body().getData().get(0).toString());

                            List<UserData> loginResponse = response.body().getData();
                            SharedPreferences.getInstance().saveSharedPreferencesBoolean(Constants.PREF_USER_LOGGED_IN, true);
                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_USER_TYPE, loginResponse.get(0).getUserType());

                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_ID, loginResponse.get(0).getId());
                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_NAME, loginResponse.get(0).getName());
                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_MOBILE_NO, loginResponse.get(0).getMobile());
                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_EMAIL_ADDRESS, loginResponse.get(0).getEmail());
                            SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_ADDRESS, loginResponse.get(0).getAddress());

                            if (userType.equals("Police")) {
                                SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_STATION_NAME, loginResponse.get(0).getStationName());
                                SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_STATION_MOBILE_NO, loginResponse.get(0).getStationMobile());
                                SharedPreferences.getInstance().saveSharedPreferencesString(Constants.PREF_STATION_ADDRESS, loginResponse.get(0).getStationAddress());
                            }

                            Toast.makeText(LoginActivity.this, "Login Successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("onEmptyResponse===", "Returned empty response");
                        Toast.makeText(LoginActivity.this,"Something went Wrong!",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }
}
