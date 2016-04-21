package com.restaurantfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.restaurantfinder.common.BaseActivity;
import com.restaurantfinder.fragments.BreakfastFragment;
import com.restaurantfinder.fragments.LunchFragment;
import com.restaurantfinder.fragments.SnacksFragment;
import com.restaurantfinder.manager.DownloadManager;
import com.restaurantfinder.manager.LocationManager;
import com.restaurantfinder.model.Restaurant;
import com.restaurantfinder.model.RestaurantList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements LocationManager.LocationAddressListener,BreakfastFragment.RestaurantItemClickListener {

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.locationAddress)TextView textView;
    @Bind(R.id.progressbar)ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.content_main);
        ButterKnife.bind(this);
        tabLayout.setVisibility(View.VISIBLE);
        LocationManager.getInstance().setContext(this);
        mProgressbar.setVisibility(View.VISIBLE);
        //buildGoogleApiClient();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BreakfastFragment(this), "BREAKFAST");
        adapter.addFrag(new BreakfastFragment(this), "LUNCH");
        adapter.addFrag(new BreakfastFragment(this), "SNACKS");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationManager.getInstance().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager.getInstance().stop();
    }

    @Override
    public void locationFound(String address) {
        textView.setText(address);
        mProgressbar.setVisibility(View.GONE);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void itemClicked(Restaurant item) {
        Intent intent = new Intent(getBaseContext(), MapDirectionActivity.class);
        intent.putExtra("lat", item.latitude);
        intent.putExtra("long", item.longitude);
        startActivity(intent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
