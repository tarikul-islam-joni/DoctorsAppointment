package com.tarikulislamjoni95.doctorsappointment.PatientPart;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorFilterDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHospitalList extends Fragment {

    private DoctorFilterDB doctorFilterDB;

    private String ExpandedHeadingText;

    private Activity activity;
    private  View view;

    private EditText editText;
    private Button button;
    private ListView listView;
    public FragmentHospitalList() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.patient_fragment_hospital_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InitializationDB();
        InitializationUI();
        CallDBForHospitalList();
    }
    private void InitializationUI()
    {
        editText=view.findViewById(R.id.edit_text_0);
        button=view.findViewById(R.id.button_0);
        listView=view.findViewById(R.id.list_view_0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!editText.getText().toString().isEmpty())
                {
                    CallDBForSearchHospital(editText.getText().toString());
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                {
                    CallDBForSearchHospital(editText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void CallDBForSearchHospital(String SearchHospitalString)
    {
        HospitalArrayAdapter.getFilter().filter(SearchHospitalString);
    }

    private void InitializationDB()
    {
        doctorFilterDB=new DoctorFilterDB(activity);
    }

    private void CallDBForHospitalList()
    {
        DoctorFilterDB doctorFilterDB=new DoctorFilterDB(activity);
        doctorFilterDB.GetDoctorCategoryAndUIDListFrom(DBConst.HospitalList,DBConst.GetHospitalListWithData);
    }

    private HashMap<String,Object> PatientAccountInfoHashMap;
    public void GetDataFromActivity(String WhichMethod,Object object)
    {
        //InitializationUI();

        switch (WhichMethod)
        {
            case DBConst.GetPatientAccountInformation:
                PatientAccountInfoHashMap=(HashMap<String, Object>)object;
                break;
            case DBConst.GetHospitalListWithData:
                SetHospitalListAndGetUIDList((HashMap<String,Object>)object);
                break;
            case DBConst.GetDoctorListByHospital:
                SetDoctorListByExpandingFragment((ArrayList<HashMap<String,Object>>)object);
                break;
        }
    }

    private ArrayAdapter<String> HospitalArrayAdapter;
    ArrayList<String> HospitalArrayList;
    ArrayList<ArrayList<String>> HospitalContainUIDArrayList;
    private void SetHospitalListAndGetUIDList(HashMap<String, Object> hashMap)
    {
        HospitalArrayList=new ArrayList<>();
        HospitalContainUIDArrayList=new ArrayList<>();
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataParseModel dataParseModel=new DataParseForDoctorFiltration().GetTypeListAndUIDList(DBConst.HospitalList,(DataSnapshot)hashMap.get(DBConst.DataSnapshot));
            HospitalArrayList=dataParseModel.getArrayList1();
            HospitalContainUIDArrayList=dataParseModel.getArrayList2();
        }

        HospitalArrayAdapter=new ArrayAdapter<>(activity,android.R.layout.simple_list_item_1,HospitalArrayList);
        listView.setAdapter(HospitalArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                ExpandedHeadingText=HospitalArrayList.get(i);
                CallDBForGetDoctorListByHospital(HospitalContainUIDArrayList.get(i));
            }
        });
    }

    private void CallDBForGetDoctorListByHospital(ArrayList<String> UIDArrayList)
    {
        doctorFilterDB.GetDoctorList(DBConst.GetDoctorListByHospital,UIDArrayList);
    }

    private void SetDoctorListByExpandingFragment(ArrayList<HashMap<String,Object>> DoctorArrayList)
    {
        getFragmentManager().beginTransaction().replace(R.id.hospital_fragment_container,new ExpandedFragmentForDoctorList(activity,ExpandedHeadingText,DoctorArrayList,PatientAccountInfoHashMap)).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
