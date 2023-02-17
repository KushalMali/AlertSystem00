package com.alertsystem.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alertsystem.R;
import com.alertsystem.adapters.DashboardAdapter;
import com.alertsystem.clickListeners.ClickListener;
import com.alertsystem.clickListeners.RecyclerTouchListener;
import com.alertsystem.databinding.ActivityDashboardBinding;
import com.alertsystem.models.Dashboard;
import com.alertsystem.utils.Constants;
import com.alertsystem.utils.SharedPreferences;
import com.alertsystem.utils.SpacesItemDecoration;
import com.alertsystem.utils.Util;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    private long pressedTime;
    String username = "", usertype = "";
    private DashboardAdapter adapter;
    private ArrayList<Dashboard> dashboardMenuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);

        bindView();
        addListener();
    }

    private void bindView() {
        SharedPreferences.getInstance().initializeSharePreferences(DashboardActivity.this);

        username = SharedPreferences.getInstance().fetchSharedPreferenesString(Constants.PREF_NAME, "");
        usertype = SharedPreferences.getInstance().fetchSharedPreferenesString(Constants.PREF_USER_TYPE, "");
        binding.title.setText("Welcome " + username);

        if (usertype.equalsIgnoreCase("User")) {
            binding.usertypeImage.setImageResource(R.drawable.user);
        } else {
            binding.usertypeImage.setImageResource(R.drawable.police);
        }

        setMenus();
    }

    private void setMenus() {
        String[] menuName = getResources().getStringArray
                (R.array.menu_name_array);
        TypedArray menuIcon = getResources().obtainTypedArray
                (R.array.menu_icon_array);

        dashboardMenuList = new ArrayList<>();
        for (int i = 0; i < menuName.length; i++) {
            dashboardMenuList.add(new Dashboard(menuName[i], menuIcon
                    .getResourceId(i, -1)));
        }

        ArrayList<Dashboard> dashboardList = new ArrayList<>();
        if (usertype.equalsIgnoreCase("User")) {
            dashboardList.add(dashboardMenuList.get(0));
            dashboardList.add(dashboardMenuList.get(4));
        } else {
            dashboardList.add(dashboardMenuList.get(0));
            dashboardList.add(dashboardMenuList.get(1));
            dashboardList.add(dashboardMenuList.get(2));
        }
        dashboardList.add(dashboardMenuList.get(3));
        dashboardMenuList.clear();
        dashboardMenuList.addAll(dashboardList);

        menuIcon.recycle();
        GridLayoutManager gridLayoutManagerVertical =
                new GridLayoutManager(this,
                        1,
                        RecyclerView.VERTICAL,
                        false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager
                (this, 3));
        binding.recyclerView.setLayoutManager(gridLayoutManagerVertical);

        //recyclerView.setHasFixedSize(true);
        int spaceInPixels = 5;
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(spaceInPixels));

        adapter = new DashboardAdapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.setDashboardItemList(dashboardMenuList);
    }

    private void addListener() {
        binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(DashboardActivity.this, binding.recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (dashboardMenuList.get(position).getName()
                        .equalsIgnoreCase("Crime Details")) {
                    //Toast.makeText(DashboardActivity.this, "View Report Selected!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DashboardActivity.this, CrimeDetailsActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_in);
                }

                if (dashboardMenuList.get(position).getName()
                        .equalsIgnoreCase("House Details")) {
                    //Toast.makeText(DashboardActivity.this, "House Details Selected!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DashboardActivity.this, HouseDetailsActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_in);
                }

                if (dashboardMenuList.get(position).getName()
                        .equalsIgnoreCase("Add Report")) {
                    //Toast.makeText(DashboardActivity.this, "Add Report Selected!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DashboardActivity.this, AddReportActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_in);
                }

                if (dashboardMenuList.get(position).getName()
                        .equalsIgnoreCase("Check Report")) {
                    //Show Blockchains Blocks here
                    //Toast.makeText(DashboardActivity.this, "Check Report Selected!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DashboardActivity.this, CheckReportActivity.class);
                    startActivity(intent);
                }

                if (dashboardMenuList.get(position).getName()
                        .equalsIgnoreCase("Logout")) {
                    //Toast.makeText(DashboardActivity.this, "Logout Selected!", Toast.LENGTH_SHORT).show();
                    Util.showLogoutDialog(DashboardActivity.this);
                }
            }
        }));
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press Back Again To Exit!", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}