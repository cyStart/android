package com.example.fetchfinale;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            case 1:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            default:
              //  OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
