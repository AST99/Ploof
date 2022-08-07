package com.astdev.ploof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(AppCompatActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment frag_new = null;
        if (position==0) frag_new= new AujourdHuiFragment();
        if (position==1) frag_new= new HierFragment();
        if (position==2) frag_new= new CetteSemaineFragment();

        assert frag_new != null;
        return frag_new;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
