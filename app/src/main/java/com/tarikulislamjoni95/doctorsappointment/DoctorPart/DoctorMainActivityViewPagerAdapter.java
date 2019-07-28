package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DoctorMainActivityViewPagerAdapter extends FragmentPagerAdapter
{


    public DoctorMainActivityViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                TodayAppointmentListFragment todayAppointmentListFragment1=new TodayAppointmentListFragment();
                return todayAppointmentListFragment1;
            case 1:
                TodayAppointmentListFragment todayAppointmentListFragment2=new TodayAppointmentListFragment();
                return todayAppointmentListFragment2;
            case 2:
                TodayAppointmentListFragment todayAppointmentListFragment3=new TodayAppointmentListFragment();
                return todayAppointmentListFragment3;
                default:
                    return null;
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
                return "Previous Appointment List";
            case 1:
                return "Today Appointment List";
            case 2:
                return "Upcoming Appointment List";
                default:
                    return null;
        }
    }
}
