package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import java.util.Set;

public class DoctorControlFragment extends Fragment
{
    private AccountMultiplicityDB accountMultiplicityDB;
    private AccountStatusDB accountStatusDB;

    private ArrayList<String> VerifiedDoctorBMDCList;
    private ArrayList<HashMap<String,Object>> VerifiedDoctorUIDList;
    private ArrayList<String> UnverifiedDoctorBMDCList;
    private ArrayList<HashMap<String,Object>> UnverifiedDoctorUIDList;
    private ArrayList<String> LockedDoctorUIDList;

    private ArrayAdapter<String> DoctorBMDCAdapter;

    private TextView textViewF;
    private EditText editTextF;
    private Button buttonF;
    private RadioGroup radioGroupF;
    private RadioButton[] radioButtonsF;
    private ListView listViewF;
    private View DoctorControlView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DoctorControlView=LayoutInflater.from(getActivity()).inflate(R.layout.admin_doctor_control_fragment,container,false);
        return DoctorControlView;
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
        VerifiedDoctorBMDCList=new ArrayList<>();
        VerifiedDoctorUIDList=new ArrayList<>();
        UnverifiedDoctorBMDCList=new ArrayList<>();
        UnverifiedDoctorUIDList=new ArrayList<>();
        LockedDoctorUIDList=new ArrayList<>();
    }
    private void InitializationClass()
    {

    }

    private void InitializationUI()
    {
        radioGroupF=DoctorControlView.findViewById(R.id.radio_group_0);
        radioButtonsF=new InitializationUIHelperClass(DoctorControlView).setRadioButton(new int[]{R.id.radio_button_0,R.id.radio_button_1,R.id.radio_button_2});
        textViewF=DoctorControlView.findViewById(R.id.text_view_0);
        editTextF=DoctorControlView.findViewById(R.id.edit_text_0);
        listViewF=DoctorControlView.findViewById(R.id.list_view_0);
        buttonF=DoctorControlView.findViewById(R.id.button_0);
        buttonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextF.getText().toString().isEmpty())
                {
                    //CallDBForSearchDoctorBMDC(editTextF.getText().toString());
                }
            }
        });

        radioGroupF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.radio_button_0:
                        RadioButtonEffect(0);
                        textViewF.setText("Unverified Doctor List : ");
                        SetListView(DBConst.UNVERIFIED, UnverifiedDoctorBMDCList);
                        break;
                    case R.id.radio_button_1:
                        RadioButtonEffect(1);
                        textViewF.setText("Verified Doctor List : ");
                        SetListView(DBConst.VERIFIED,VerifiedDoctorBMDCList);
                        break;
                    case R.id.radio_button_2:
                        RadioButtonEffect(2);
                        textViewF.setText("Locked Doctor List : ");
                        SetListView(DBConst.GetLockedDoctorAccountListFromDB,LockedDoctorUIDList);
                        break;
                }
            }
        });

        editTextF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                listViewF.setFilterText(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void SetListView(final String WhichType, ArrayList<String> myArrayList)
    {
        DoctorBMDCAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,myArrayList);
        listViewF.setAdapter(DoctorBMDCAdapter);
        listViewF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (WhichType.matches(DBConst.UNVERIFIED))
                {
                    Intent intent=new Intent(getActivity(), AdminControlActivity.class);
                    intent.putExtra(DBConst.BackActivity,DBConst.Doctor);
                    intent.putExtra(DBConst.RESULT,"Unverified Doctor UID Control");
                    intent.putExtra(DBConst.DataSnapshot,GetUIDList(UnverifiedDoctorUIDList.get(i)));
                    startActivity(intent);
                }
                else if (WhichType.matches(DBConst.VERIFIED))
                {
                    Intent intent=new Intent(getActivity(), AdminControlActivity.class);
                    intent.putExtra(DBConst.BackActivity,DBConst.Doctor);
                    intent.putExtra(DBConst.RESULT,"Verified Doctor UID Control");
                    intent.putExtra(DBConst.DataSnapshot,GetUIDList(VerifiedDoctorUIDList.get(i)));
                    startActivity(intent);
                }
                else if (WhichType.matches(DBConst.GetLockedDoctorAccountListFromDB))
                {
                    ArrayList<String> arrayList=new ArrayList<>();
                    arrayList.add(LockedDoctorUIDList.get(i));
                    Intent intent=new Intent(getActivity(), AdminControlActivity.class);
                    intent.putExtra(DBConst.BackActivity,DBConst.Patient);
                    intent.putExtra(DBConst.RESULT,"Locked Doctor UID Control");
                    intent.putExtra(DBConst.DataSnapshot,arrayList);
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<String> GetUIDList(HashMap<String,Object> hashMap)
    {
        ArrayList<String> arrayList1=new ArrayList<>();
        Set keySet= hashMap.keySet();
        Iterator<String> iterator=keySet.iterator();
        while (iterator.hasNext())
        {
            arrayList1.add(iterator.next());
        }
        return arrayList1;
    }

    private void RadioButtonEffect(int k)
    {
        for(int i=0; i<radioButtonsF.length; i++)
        {
            if (i==k)
            {
                radioButtonsF[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_background_2));
            }
            else
            {
                radioButtonsF[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_background_1));
            }
        }
    }

    private void InitializationDB()
    {
        accountMultiplicityDB=new AccountMultiplicityDB(getActivity());
        accountMultiplicityDB.GetAccountMultiplicityListByKey(DBConst.Doctor,DBConst.GetAccountMultiplicityDoctorAccountListFromDB);
        accountStatusDB=new AccountStatusDB(getActivity());
        accountStatusDB.GetLockedAccountList(DBConst.GetLockedDoctorAccountListFromDB);
    }
    public void GetDataFromActivity(String WhichMethod,Object object)
    {
        switch (WhichMethod)
        {
            case DBConst.GetAccountMultiplicityDoctorAccountListFromDB:
                GetAccountMultiplicityDataFromDataParserClass((HashMap<String, Object>) object);
                break;
            case DBConst.GetLockedDoctorAccountListFromDB:
                GetLockedAccountDataFromDataParserClass((HashMap<String, Object>) object);
                break;
        }
    }
    private void GetAccountMultiplicityDataFromDataParserClass(HashMap<String,Object> hashMap)
    {
        ParseDoctorListAccountMultiplicityHashMap parseDoctorListAccountMultiplicityHashMap0=new ParseDoctorListAccountMultiplicityHashMap();
        VerifiedDoctorBMDCList=parseDoctorListAccountMultiplicityHashMap0.ProcessDoctorList(hashMap,DBConst.Doctor,DBConst.VERIFIED).getArrayList();
        VerifiedDoctorUIDList=parseDoctorListAccountMultiplicityHashMap0.ProcessDoctorList(hashMap,DBConst.Doctor,DBConst.VERIFIED).getHashMapArrayList();
        ParseDoctorListAccountMultiplicityHashMap parseDoctorListAccountMultiplicityHashMap1=new ParseDoctorListAccountMultiplicityHashMap();
        UnverifiedDoctorBMDCList=parseDoctorListAccountMultiplicityHashMap1.ProcessDoctorList(hashMap,DBConst.Doctor,DBConst.UNVERIFIED).getArrayList();
        UnverifiedDoctorUIDList=parseDoctorListAccountMultiplicityHashMap1.ProcessDoctorList(hashMap,DBConst.Doctor,DBConst.UNVERIFIED).getHashMapArrayList();
    }

    private void GetLockedAccountDataFromDataParserClass(HashMap<String,Object> hashMap)
    {
        ParseDoctorListAccountMultiplicityHashMap parseDoctorListAccountMultiplicityHashMap1=new ParseDoctorListAccountMultiplicityHashMap();
        LockedDoctorUIDList=parseDoctorListAccountMultiplicityHashMap1.ProcessDoctorList(hashMap,DBConst.GetLockedDoctorAccountListFromDB,false).getArrayList();
    }
}
