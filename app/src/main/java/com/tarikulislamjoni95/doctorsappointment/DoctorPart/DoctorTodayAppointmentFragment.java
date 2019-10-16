package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DoctorTodayAppointmentFragment extends Fragment
{

    private View view;
    private RecyclerView recyclerView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=LayoutInflater.from(getActivity()).inflate(R.layout.doctor_scheduled_appointment_list_show,container,false);
        return view;
    }

    private String CurrentDateFormat;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1,RecyclerView.VERTICAL,false));

        textView=view.findViewById(R.id.text_view_0);

        Calendar calendar=Calendar.getInstance();
        calendar.add(calendar.DATE,+0);
        String CurrentDate=String.valueOf(calendar.get(Calendar.DATE));
        String CurrentMonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String CurrentYear=String.valueOf(calendar.get(Calendar.YEAR));
        CurrentDateFormat=CurrentDate+"-"+CurrentMonth+"-"+CurrentYear;
        CallDBForTodayAppointmentList();
    }
    public void GetDataFromActivity(String WhichField,Object object)
    {
        switch (WhichField)
        {
            case DBConst.GetTodayDoctorScheduledAppointmentList:
                ProcessAndShowAppointmentListIntoRecyclerListView((HashMap<String,Object>)object);
                break;
        }
    }

    private void ProcessAndShowAppointmentListIntoRecyclerListView(HashMap<String,Object> DataHashMap)
    {
        ArrayList<HashMap<String,Object>> arrayList=new DataParseForShowAppointmentList().GetSelectedKeyBasedAppointmentList(DataHashMap,DBConst.AppointmentDate,CurrentDateFormat);
        textView.setText("Total Appointment No : "+arrayList.size());
        ShowScheduledAppointmentListAdapter showScheduledAppointmentListAdapter=new ShowScheduledAppointmentListAdapter("DoctorYesterdayAppointmentFragment",getActivity(),arrayList);
        recyclerView.setAdapter(showScheduledAppointmentListAdapter);
    }

    private void CallDBForTodayAppointmentList()
    {
        DoctorAccountDB doctorAccountDB=new DoctorAccountDB(getActivity());
        doctorAccountDB.GetDoctorScheduledAppointmentListFromDoctorAccount(DBConst.SELF,DBConst.AppointmentDate,CurrentDateFormat,DBConst.GetTodayDoctorScheduledAppointmentList);
    }
}