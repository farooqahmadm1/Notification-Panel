package com.example.farooq.notificationpanel;

import android.app.NotificationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.farooq.notificationpanel.fragments.NotificationFragment;
import com.example.farooq.notificationpanel.fragments.ProfileFragment;
import com.example.farooq.notificationpanel.fragments.UsersFragment;

public class PagerViewAdapter extends FragmentPagerAdapter {
    private static final int SIZE = 3;

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        ProfileFragment profileFragment= new ProfileFragment();
        UsersFragment usersFragment= new UsersFragment();
        NotificationFragment notificationFragment= new NotificationFragment();
        switch (position){
            case 0:
                return profileFragment;
            case 1:
                return usersFragment;
            case 2:
                return notificationFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return SIZE;
    }
}
