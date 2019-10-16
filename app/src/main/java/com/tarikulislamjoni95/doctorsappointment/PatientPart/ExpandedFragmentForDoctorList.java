package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandedFragmentForDoctorList extends Fragment
{
    private DoctorListViewAdapter doctorListViewAdapter;

    private TextView textView;
    private RecyclerView recyclerView;

    private View view;
    private Activity activity;
    private ArrayList<HashMap<String,Object>> arrayList;
    private String HeadingText;
    private  HashMap<String ,Object> PatientAccountInformationHashMap;
    public ExpandedFragmentForDoctorList(Activity activity,String HeadingText, ArrayList<HashMap<String,Object>> arrayList,HashMap<String,Object> PatientAccountInformationHashMap)
    {
        this.activity=activity;
        this.arrayList=arrayList;
        this.HeadingText=HeadingText;
        this.PatientAccountInformationHashMap=PatientAccountInformationHashMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.patient_expanded_layout_for_doctor_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initialization();
        InitializationClass();
        InitializationUI();
    }

    private void Initialization()
    {

    }
    private void InitializationClass()
    {

    }

    private void InitializationUI()
    {
        textView=view.findViewById(R.id.text_view_0);
        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        doctorListViewAdapter=new DoctorListViewAdapter((Activity) view.getContext(),arrayList,PatientAccountInformationHashMap);
        recyclerView.setAdapter(doctorListViewAdapter);

        textView.setText(HeadingText);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ExpandedFragmentForDoctorList.this).commit();
            }
        });
    }
}
