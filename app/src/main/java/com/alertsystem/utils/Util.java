package com.alertsystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.alertsystem.R;
import com.alertsystem.activities.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static void setTitle(TextView tvHeader, String title) {
        tvHeader.setText(title);
    }

    public static String checkString(String myString) {

        String data = myString.trim();
        if (data.length() == 0
                || data.equalsIgnoreCase("")
                || data.equalsIgnoreCase(null)
                || data.equalsIgnoreCase("null")
                || data.equalsIgnoreCase("N/A")) {
            data = "";
        } else {
            data = myString.trim();
        }
        return data;
    }

    public static String checkString2(String myString) {

        String data = myString.trim();
        if (data.equalsIgnoreCase(null) ||
                data.equalsIgnoreCase("") ||
                data.length() == 0 ||
                data.equalsIgnoreCase("null")) {
            data = "";
        } else {
            data = myString.trim();
        }
        return data;
    }

    public static String dateSelected() {
        Date dateSelected = new Date();
        String DATEFORMAT = "dd-MM-yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        final String utcTime = sdf.format(dateSelected);

        return utcTime;
    }

    public static boolean isValidMobileNumber(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean validateString(String userName) {
        boolean isEmpty = true;
        if (userName.length() > 0 && !userName.equalsIgnoreCase("")) {
            isEmpty = false;
        } else {
            isEmpty = true;
        }
        return isEmpty;
    }

    public static boolean isValidMobile(String phone2) {
        boolean check = false;
        CharSequence text = "";

        if (!Pattern.matches("[a-zA-Z]+", text)) {
            if (phone2.length() > 0 && !phone2.equalsIgnoreCase("") && (phone2.length() == 10)) {
                check = false;
                //  txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static void setupUI(final Activity activityContext, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard(activityContext);
                    return false;
                }
            });
        }
    }

    public static void showInternetDialog(final Context ctx, String msg) {
        final Activity activity = (Activity) ctx;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.app_name);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void alertDialog(final Context ctx, String msg) {
        final Activity activity = (Activity) ctx;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.app_name);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*public static void showCustomDialog(Context context,
                                        String dialogTitle,
                                        String dialogMessage,
                                        boolean isCancelable,
                                        boolean isPositiveButton, String positiveButtonTitle,
                                        View.OnClickListener positiveDialogInterfaceClickListener,
                                        boolean isNegativeButton, String negativeButtonTitle,
                                        View.OnClickListener negativeDialogInterfaceClickListener) {
        // custom dialog
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(isCancelable);
        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.dialogTitle);
        TextView txtMessage = dialog.findViewById(R.id.dialogMessage);
        text.setText(dialogTitle);
        txtMessage.setText(dialogMessage);
//        ImageView image =  dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_launcher);
        Button dialogButtonNo = dialog.findViewById(R.id.dialogButtonNo);
        Button dialogButtonYes = dialog.findViewById(R.id.dialogButtonYes);
        if (isPositiveButton) {
            dialogButtonYes.setText(positiveButtonTitle);
            dialogButtonYes.setOnClickListener(
                    (positiveDialogInterfaceClickListener != null) ?
                            positiveDialogInterfaceClickListener :
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
        }

        if (isNegativeButton) {
            dialogButtonNo.setText(negativeButtonTitle);
            dialogButtonNo.setOnClickListener(
                    (negativeDialogInterfaceClickListener != null) ?
                            negativeDialogInterfaceClickListener :
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
        }
        dialog.show();

    }*/

    public static boolean isInternetAvailable(Context mContext) {
        if (haveNetworkConnection(mContext)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean haveNetworkConnection(Context contxt) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    return haveConnectedWifi;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    return haveConnectedMobile;
                }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void validateStringAndSetTextView(TextView textView, String string) {
        if (string.equalsIgnoreCase("") || string.equalsIgnoreCase("null") || string.equalsIgnoreCase(null)) {
            textView.setText("N/A");
        } else {
            textView.setText(string);
        }
    }

    public static void displayNormalSnackbar(CoordinatorLayout cdSnackbar, int intOfString) {
        Snackbar snackbar = Snackbar.make(cdSnackbar, intOfString, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showLogoutDialog(final Context context) {
        final Activity activity = (Activity) context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setTitle("Alert!");
        builder.setIcon(R.mipmap.ic_logo);
        builder.setMessage("Are you sure you want to logout?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.getInstance().clearPreferences();
                Toast.makeText(context, "Logout Successfully..", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
                activity.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Dialog progressDialog(Context context) {
        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress_layout);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return mDialog;
    }

    public static void setAdapter(Spinner spinner, ArrayList<String> dataList) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(spinner.getContext(), R.layout.spinner_row_item, dataList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
