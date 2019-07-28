package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PatientMainActivityViewPagerAdapter extends FragmentPagerAdapter
{

    Fragment fragment;
    public PatientMainActivityViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                fragment=new DoctorListFragment();
                return fragment;
            case 1:
                fragment=new DoctorListFragment();
                return fragment;
            case 2:
                fragment=new DoctorListFragment();
                return fragment;
            default:
                return new DoctorListFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Doctor List";
            case 1:
                return "Category List";
            case 2:
                return "Hospital List";
            default:
                return null;

        }
    }
}
