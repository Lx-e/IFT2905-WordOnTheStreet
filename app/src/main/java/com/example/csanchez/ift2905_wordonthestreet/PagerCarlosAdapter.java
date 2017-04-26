package com.example.csanchez.ift2905_wordonthestreet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class PagerCarlosAdapter extends FragmentStatePagerAdapter {
    String[] categories =  {"business", "entertainment", "gaming", "general", "music", "politics", "science-and-nature", "sport", "technology"};
    public PagerCarlosAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PagerCarlosFragment();
        Bundle args = new Bundle();
        args.putString("Category", categories[i]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }
}