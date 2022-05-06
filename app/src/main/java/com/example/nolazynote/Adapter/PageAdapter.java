package com.example.nolazynote.Adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nolazynote.fragments.CalendarFragment;
import com.example.nolazynote.fragments.TaskFragment;
import com.example.nolazynote.fragments.ToDoFragment;

import java.util.HashMap;

public class PageAdapter extends FragmentPagerAdapter {

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public PageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {

        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new TaskFragment();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new ToDoFragment();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new CalendarFragment();
                    Log.i("fragment", "fragment3");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}
