package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PatientMainActivityViewPagerAdapter extends FragmentPagerAdapter
{
    public PatientMainActivityViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                DoctorListFragment doctorListFragment=new DoctorListFragment();
                return doctorListFragment;
            case 1:
                HospitalListFragment hospitalListFragment=new HospitalListFragment();
                return hospitalListFragment;
            case 2:
                HospitalListFragment hospitalListFragment1=new HospitalListFragment();
                return hospitalListFragment1;
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
                return "Hospital List";
            case 2:
                return "Hospital List";
            default:
                return null;

        }
    }
}
