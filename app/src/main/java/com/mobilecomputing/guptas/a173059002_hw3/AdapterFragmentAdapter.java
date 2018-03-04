package com.mobilecomputing.guptas.a173059002_hw3;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
//import android.app.FragmentPagerAdapter;

/**
 * Created by guptas on 3/3/18.
 */

public class AdapterFragmentAdapter extends FragmentPagerAdapter {
    private List<String> tabTitlesList = new ArrayList<>();
    private List<Fragment> tabFragmentsList = new ArrayList<>();
    private Context context;


    public AdapterFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabFragmentsList.size();
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return tabFragmentsList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitlesList.get(position);
    }

    public void addFragment(Fragment fragment, String string)
    {
        tabFragmentsList.add(fragment);
        tabTitlesList.add(string);
    }
}
