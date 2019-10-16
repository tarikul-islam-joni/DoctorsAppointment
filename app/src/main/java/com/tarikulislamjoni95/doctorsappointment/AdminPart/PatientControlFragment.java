package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PatientControlFragment extends Fragment
{
    private ArrayList<String> arrayListLL=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> hashMapArrayListLL=new ArrayList<>();
    private RadioGroup radioGroup;
    private RadioButton[] radioButtons;
    private EditText editText;
    private Button button;
    private ListView listView;
    private View PatientControlFragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PatientControlFragmentView=LayoutInflater.from(getActivity()).inflate(R.layout.admin_doctor_control_fragment,container,false);
        return PatientControlFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializationUI();
        InitializationDB();
    }

    private void InitializationUI()
    {
        radioGroup=PatientControlFragmentView.findViewById(R.id.radio_group_0);
        radioButtons=new InitializationUIHelperClass(PatientControlFragmentView).setRadioButton(new int[]{R.id.radio_button_0,R.id.radio_button_1,R.id.radio_button_2});
        radioButtons[0].setText("Locked Account");
        radioButtons[1].setText("Multiple Account");
        radioButtons[2].setVisibility(View.GONE);
        editText=PatientControlFragmentView.findViewById(R.id.edit_text_0);
        button=PatientControlFragmentView.findViewById(R.id.button_0);
        listView=PatientControlFragmentView.findViewById(R.id.list_view_0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.radio_button_0:
                        RadioButtonEffect(0);
                        GetLockedAccount();
                        break;
                    case R.id.radio_button_1:
                        RadioButtonEffect(1);
                        GetMultipleAccount();
                        break;
                }
            }
        });
    }

    private void InitializationDB()
    {
        AccountStatusDB accountStatusDB=new AccountStatusDB(getActivity());
        accountStatusDB.GetLockedAccountList(DBConst.GetLockedPatientAccountListFromDB);
    }

    private void RadioButtonEffect(int WhichRadioButton)
    {
        for(int i=0; i<radioButtons.length-1; i++)
        {
            if (i==WhichRadioButton)
            {
                radioButtons[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_background_2));
            }
            else
            {
                radioButtons[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_background_1));
            }
        }
    }

    private void GetLockedAccount()
    {
        AccountStatusDB accountStatusDB=new AccountStatusDB(getActivity());
        accountStatusDB.GetLockedAccountList(DBConst.GetLockedPatientAccountListFromDB);
    }

    private void GetMultipleAccount()
    {
        AccountMultiplicityDB accountMultiplicityDB=new AccountMultiplicityDB(getActivity());
        accountMultiplicityDB.GetAccountMultiplicityListByKey(DBConst.Patient,DBConst.GetAccountMultiplicityPatientAccountListFromDB);
    }

    public void GetDataFromActivity(String WhichDB,Object object)
    {
        switch (WhichDB)
        {
            case DBConst.GetLockedPatientAccountListFromDB:
                ArrayList<String> arrayList=new ParseDoctorListAccountMultiplicityHashMap().ProcessDoctorList((HashMap<String, Object>)object,DBConst.GetLockedPatientAccountListFromDB,false).getArrayList();
                ArrayList<HashMap<String,Object>> hashMapArrayList=new ArrayList<>();
                SetUpListView(DBConst.GetLockedPatientAccountListFromDB,arrayList,hashMapArrayList);
                break;
            case DBConst.GetAccountMultiplicityPatientAccountListFromDB:
                ArrayList<String> arrayList1=new ParseDoctorListAccountMultiplicityHashMap().ProcessDoctorList((HashMap<String, Object>)object,DBConst.Patient,false).getArrayList();
                ArrayList<HashMap<String,Object>> hashMapArrayList1=new ParseDoctorListAccountMultiplicityHashMap().ProcessDoctorList((HashMap<String, Object>)object,DBConst.Patient,false).getHashMapArrayList();
                SetUpListView(DBConst.GetAccountMultiplicityPatientAccountListFromDB,arrayList1,hashMapArrayList1);
                break;

        }
    }
    private void SetUpListView(final String WhichType,ArrayList<String> arrayList, ArrayList<HashMap<String,Object>> hashMapArrayList)
    {
        arrayListLL=arrayList;
        hashMapArrayListLL=hashMapArrayList;
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (WhichType.matches(DBConst.GetLockedPatientAccountListFromDB))
                {
                    Intent intent=new Intent(getActivity(), AdminControlActivity.class);
                    intent.putExtra(DBConst.BackActivity,DBConst.Patient);
                    intent.putExtra(DBConst.RESULT,"Locked Patient List");
                    ArrayList<String> arrayList1=new ArrayList<>();
                    arrayList1.add(arrayListLL.get(i));
                    intent.putExtra(DBConst.DataSnapshot,arrayList1);
                    startActivity(intent);
                }
                else if (WhichType.matches(DBConst.GetAccountMultiplicityPatientAccountListFromDB))
                {
                    Log.d("myERRRR",hashMapArrayListLL.get(i).toString());
                    Intent intent=new Intent(getActivity(), AdminControlActivity.class);
                    intent.putExtra(DBConst.BackActivity,DBConst.Patient);
                    intent.putExtra(DBConst.RESULT,"Multiple Account UID List ");
                    intent.putExtra(DBConst.DataSnapshot,GetUIDList(hashMapArrayListLL.get(i)));
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<String> GetUIDList(HashMap<String,Object> hashMap)
    {
        Log.d("myERRRR 1 ",hashMap.toString());
        ArrayList<String> arrayList1=new ArrayList<>();
        Set keySet= hashMap.keySet();
        Iterator<String> iterator=keySet.iterator();
        while (iterator.hasNext())
        {
            arrayList1.add(iterator.next());
        }

        Log.d("myERRRR 2 ",arrayList1.toString());
        return arrayList1;
    }
}
