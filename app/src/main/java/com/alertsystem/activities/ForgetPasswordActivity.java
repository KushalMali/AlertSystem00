package com.alertsystem.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.alertsystem.R;
import com.alertsystem.api.ApiClient;
import com.alertsystem.api.ApiInterface;
import com.alertsystem.databinding.ActivityForgetPasswordBinding;
import com.alertsystem.models.LoginResponse;
import com.alertsystem.utils.InternetConnection;
import com.alertsystem.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityForgetPasswordBinding binding;
    String userType = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ForgetPasswordActivity.this, R.layout.activity_forget_password);

        addListener();
    }

    private void addListener() {
        binding.btnFP.setOnClickListener(this);
        binding.fpUser.setOnClickListener(this);
        binding.fpPolice.setOnClickListener(this);
        binding.fpUser.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnFP:

                if (binding.fpMobile.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Mobile cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.fpMobile.setError("Mobile cannot be Blank");

                    return;

                } else if (binding.fpEmail.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Email cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    binding.fpEmail.setError("Email cannot be Blank");

                    return;

                } else {

                    if(ActivityCompat.checkSelfPermission(ForgetPasswordActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ){
                        ActivityCompat.requestPermissions(ForgetPasswordActivity.this,new String[]{Manifest.permission.SEND_SMS},1);
                    } else {
                        forgetPassword();
                    }
                }

                break;

            case R.id.fpUser:
                //Toast.makeText(RegisterActivity.this, userRadioButton.getText() + " Selected!", Toast.LENGTH_SHORT).show();
                userType = "User";
                break;

            case R.id.fpPolice:
                //Toast.makeText(RegisterActivity.this, policeRadioButton.getText() + " Selected!", Toast.LENGTH_SHORT).show();
                userType = "Police";
                break;

            default:
                break;
        }

    }

    private void forgetPassword() {

        if (InternetConnection.checkConnection(getApplicationContext())) {

            final String mobile = binding.fpMobile.getText().toString();
            final String email = binding.fpEmail.getText().toString();

            Dialog progressDialog = Util.progressDialog(this);
            progressDialog.show();

            ApiInterface api = ApiClient.getApiService();
            Call<LoginResponse> call = api.forgetPassword(mobile, email, userType);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                            String mobile = response.body().getData().get(0).getMobile();
                            String password = response.body().getData().get(0).getPassword();
                            String user = response.body().getData().get(0).getUserType();

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(mobile,null,"Your Password For "+user+" Login Is : "+password,null,null);

                            Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("onEmptyResponse", "Returned empty response");
                        Toast.makeText(ForgetPasswordActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    //Dismiss Dialog
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

}