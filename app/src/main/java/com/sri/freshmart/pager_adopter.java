package com.sri.freshmart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class pager_adopter  extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String>  fragmentTitle = new ArrayList<>();

    String x;


    public pager_adopter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        items_fragment itemsFragment = new items_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("m", String.valueOf(position));
        bundle.putString("a",x);

        position= position+1;

        itemsFragment.setArguments(bundle);

        //sent position to fragment so it can recycle items from FB
        //m carries int, from 0,so 0 will be my first catogary


        return itemsFragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }




    public void addFragment(Fragment fragment, String title, String i){
        fragmentList.add(fragment);
        fragmentTitle.add(title);
        x=i;



    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
