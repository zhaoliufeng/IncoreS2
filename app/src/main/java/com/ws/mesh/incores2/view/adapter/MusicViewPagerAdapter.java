package com.ws.mesh.incores2.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ws.mesh.incores2.view.base.BaseFragment;

import java.util.List;

public class MusicViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mListFragments;

    public MusicViewPagerAdapter(FragmentManager fm, List<BaseFragment> listFragment) {
        super(fm);
        this.mListFragments = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }
}
