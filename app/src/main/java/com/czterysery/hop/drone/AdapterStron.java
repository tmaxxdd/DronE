package com.czterysery.hop.drone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Tomasz Kądziołka on 23.04.2016.
 */
public class AdapterStron extends FragmentPagerAdapter {

    String[] naglowki = new String[]{"Sterowanie", "Statystyki", "Mapa"};

    List<Fragment> strony;

    public AdapterStron(FragmentManager fm, List<Fragment> strony){
        super(fm);
        this.strony = strony;
    }

    @Override
    public Fragment getItem(int position) {
        return this.strony.get(position);
    }

    @Override
    public int getCount() {
        return this.strony.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return naglowki[position];
    }
}
