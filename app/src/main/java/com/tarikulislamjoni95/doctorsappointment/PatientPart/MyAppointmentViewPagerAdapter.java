package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAppointmentViewPagerAdapter extends FragmentPagerAdapter
{
    public MyAppointmentViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                MyTemporaryAppointmentFragment myTemporaryAppointmentFragment=new MyTemporaryAppointmentFragment();
                return myTemporaryAppointmentFragment;
            case 1:
                MyCurrentAppointmentFragment myCurrentAppointmentFragment=new MyCurrentAppointmentFragment();
                return myCurrentAppointmentFragment;
            case 2:
                MyPastAppointmentFragment myPastAppointmentFragment=new MyPastAppointmentFragment();
                return myPastAppointmentFragment;
                default:
                    return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Temporary Appointment";
            case 1:
                return "Current Appointment";
            case 2:
                return "Past Appointment";
                default:
                    return null;
        }
    }
}