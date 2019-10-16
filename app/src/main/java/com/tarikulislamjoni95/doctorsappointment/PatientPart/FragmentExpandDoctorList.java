package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tarikulislamjoni95.doctorsappointment.R;

public class FragmentExpandDoctorList extends Fragment
{
    private View view;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.patient_expanded_layout_for_doctor_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
    }

    private void Initialization()
    {

    }

    private void InitializationClass()
    {

    }

    private void InitializationUI()
    {

    }

    private void InitializationDB()
    {

    }
}
