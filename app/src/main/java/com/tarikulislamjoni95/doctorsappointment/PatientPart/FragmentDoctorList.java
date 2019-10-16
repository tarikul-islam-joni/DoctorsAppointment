package com.tarikulislamjoni95.doctorsappointment.PatientPart;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorFilterDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentDoctorList extends Fragment {

    private DoctorFilterDB doctorFilterDB;

    private DoctorListViewAdapter doctorListViewAdapter;

    private AlertDialog division_list_dialog;

    private MyToastClass myToastClass;
    private InitializationUIHelperClass initializationUIHelperClass;
    private MyLocationClass myLocationClass;

    private Activity activity;
    private View view;

    private TextView[] textViews;
    private EditText[] editTexts;
    private Button[] buttons;
    private RecyclerView recyclerView;
    public FragmentDoctorList()
    {
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.patient_fragment_doctor_list,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        GetUserLocationToGetDoctorList();
        CallDBForGetDivisionList();
    }

    private void Initialization()
    {

    }

    private void InitializationClass()
    {
        initializationUIHelperClass=new InitializationUIHelperClass(view);
        myLocationClass=new MyLocationClass(getActivity());
    }

    private void InitializationUI()
    {
        int[] editTexts_id={R.id.edit_text_0};
        int[] text_views_id={R.id.text_view_0};
        int[] buttons_id={R.id.button_0};
        editTexts=initializationUIHelperClass.setEditTexts(editTexts_id);
        textViews=initializationUIHelperClass.setTextViews(text_views_id);
        buttons=initializationUIHelperClass.setButtons(buttons_id);
        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        doctorListViewAdapter=new DoctorListViewAdapter(getActivity(),new ArrayList<HashMap<String, Object>>(),PatientAccountInfoHashMap);
        recyclerView.setAdapter(doctorListViewAdapter);

        buttons[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!editTexts[0].getText().toString().isEmpty())
                {
                    ////////////////////////////////////////////////////////////////////
                    CallDBForSearchDoctorFromEditText(editTexts[0].getText().toString());
                    ////////////////////////////////////////////////////////////////////
                }
            }
        });

        editTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                doctorListViewAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void GetUserLocationToGetDoctorList()
    {
        myLocationClass.GetCoOrdinateFromMaps();
    }
    private void CallDBForSearchDoctorFromEditText(String DoctorNameString)
    {
        doctorFilterDB.GetDoctorListByName(DoctorNameString,DBConst.GetDoctorListByDivision);
    }
    private HashMap<String ,Object> PatientAccountInfoHashMap;
    public void GetDataFromActivity(String WhichMethod,Object object)
    {

        switch (WhichMethod)
        {
            case DBConst.GetPatientAccountInformation:
                PatientAccountInfoHashMap=(HashMap<String, Object>)object;
                break;
            case VARConst.GET_LOCATION:
                Map<Integer,String> map=(Map<Integer, String>) object;
                if (map.containsKey(3))
                {
                    if (!(map.get(3)==null))
                    {
                        textViews[0].setText(map.get(3));
                        ///////////////////////////////////////////
                        GetDoctorListByDivision(map.get(3));
                        ///////////////////////////////////////////
                    }
                }
                break;
            case DBConst.GetDivisionListWithData:
                SetDivisionList((HashMap<String,Object>)object);
                break;
            case DBConst.GetDoctorListByDivision:
                ArrayList<HashMap<String,Object>> doctor_list=(ArrayList<HashMap<String, Object>>) object;
                SetDoctorList(doctor_list);
                break;
        }
    }

    ArrayList<String> Division_ArrayList=new ArrayList<>();
    ArrayList<ArrayList<String>> Division_UID_ArrayList=new ArrayList<>();
    private void SetDivisionList(HashMap<String,Object> hashMap)
    {
        Division_ArrayList=new ArrayList<>();
        Division_UID_ArrayList=new ArrayList<>();
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataSnapshot dataSnapshop=(DataSnapshot) hashMap.get(DBConst.DataSnapshot);
            DataParseModel dataParseModel=new DataParseForDoctorFiltration().GetTypeListAndUIDList(DBConst.AvailableArea,dataSnapshop);
            Division_ArrayList=dataParseModel.getArrayList1();
            Division_UID_ArrayList=dataParseModel.getArrayList2();
        }
        textViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (division_list_dialog!=null)
                {
                    if (!division_list_dialog.isShowing())
                    {
                        division_list_dialog.show();
                    }
                }
            }
        });

        CreateDivisionListDialog();
    }
    private void CreateDivisionListDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final String[] DivisionStringArray=new String[Division_ArrayList.size()];
        for(int i=0; i<Division_ArrayList.size(); i++)
        {
            DivisionStringArray[i]=Division_ArrayList.get(i);
        }
        builder.setSingleChoiceItems(DivisionStringArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                division_list_dialog.dismiss();

                ////////////////////////////////////////
                GetDoctorListByDivision(DivisionStringArray[i]);
                ////////////////////////////////////////
            }
        });
        division_list_dialog=builder.create();
    }
    private void GetDoctorListByDivision(String DivisionString)
    {
        for(int i=0; i<Division_ArrayList.size(); i++)
        {
            if (Division_ArrayList.get(i).matches(DivisionString))
            {
                CallDBForGetDoctorList(Division_UID_ArrayList.get(i));
            }
        }
    }
    private void SetDoctorList(ArrayList<HashMap<String,Object>> arrayList)
    {
        doctorListViewAdapter=new DoctorListViewAdapter(getActivity(),arrayList,PatientAccountInfoHashMap);
        recyclerView.setAdapter(doctorListViewAdapter);
    }
    private void InitializationDB()
    {
        doctorFilterDB=new DoctorFilterDB(activity);
    }
    private void CallDBForGetDoctorList(ArrayList<String> arrayList)
    {
        doctorFilterDB.GetDoctorList(DBConst.GetDoctorListByDivision,arrayList);
    }
    private void CallDBForGetDivisionList()
    {
        DoctorFilterDB doctorFilterDB=new DoctorFilterDB(activity);
        doctorFilterDB.GetDoctorCategoryAndUIDListFrom(DBConst.AvailableArea,DBConst.GetDivisionListWithData);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
