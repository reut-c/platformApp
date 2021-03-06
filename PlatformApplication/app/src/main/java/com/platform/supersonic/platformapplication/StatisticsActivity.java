package com.platform.supersonic.platformapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends BasicActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String token;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Bundle extras = getIntent().getExtras();
        this.token = this.getToken();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        this.logout = (Button) findViewById(R.id.bLogout);
        this.logout.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putString(BasicActivity.TOKEN, this.token);

        MonetizeStats monetizeStatsFragment = new MonetizeStats();
        monetizeStatsFragment.setArguments(args);
        PromoteStats promoteStatsFragment = new PromoteStats();
        promoteStatsFragment.setArguments(args);

        adapter.addFragment(monetizeStatsFragment, "Monetize");
        adapter.addFragment(promoteStatsFragment, "Promote");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bLogout:
                this.deleteToken();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
