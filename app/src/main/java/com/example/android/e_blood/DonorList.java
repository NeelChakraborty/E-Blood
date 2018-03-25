package com.example.android.e_blood;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

public class DonorList extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DonorListAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new DonorListAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new DonorListOPos(), "O+");
        viewPagerAdapter.addFragments(new DonorListONeg(), "O-");
        viewPagerAdapter.addFragments(new DonorListAPos(), "A+");
        viewPagerAdapter.addFragments(new DonorListANeg(), "A-");
        viewPagerAdapter.addFragments(new DonorListBPos(), "B+");
        viewPagerAdapter.addFragments(new DonorListBNeg(), "B-");
        viewPagerAdapter.addFragments(new DonorListABPos(), "AB+");
        viewPagerAdapter.addFragments(new DonorListABNeg(), "AB-");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void refreshNow (){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

}
