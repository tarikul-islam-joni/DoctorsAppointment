package com.tarikulislamjoni95.doctorsappointment.PatientPart;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorFilterDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDiseaseList extends Fragment {


    private String ExpandedHeadingText;

    private Activity activity;
    private View view;

    private ArrayList<String> DiseaseArrayList;
    private ArrayList<ArrayList<String>> DiseaseTypeUIDArrayList;

    private String[] DiseaseTypeShowList;
    private String[] DiseaseTypeList;
    private int[] DiseaseImageResuource;

    private DiseaseAdapter diseaseAdapter;
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;

    public FragmentDiseaseList() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.patient_fragment_disease_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initialization();
        InitializationClass();
        InitializationUI();

        CallDBForGetDiseaseList();
    }

    private void Initialization()
    {

        DiseaseTypeList= getResources().getStringArray(R.array.doctor_category_array);
        DiseaseTypeShowList=getResources().getStringArray(R.array.doctor_category_array_for_patient);
        DiseaseImageResuource=new int[]{R.drawable.cardiac,R.drawable.chest,R.drawable.dentist,R.drawable.dermatology,R.drawable.glucometer,R.drawable.diet,R.drawable.spinner,R.drawable.ent,R.drawable.gastrointestinal,R.drawable.pregnant,R.drawable.spinner,R.drawable.spinner,R.drawable.medical_history,R.drawable.kidney,R.drawable.urology,R.drawable.urology,R.drawable.urology,R.drawable.oncology,R.drawable.spinner,R.drawable.spinner,R.drawable.paeditic,R.drawable.bipolar_disord,R.drawable.pulmonary,R.drawable.physiotherapy,R.drawable.spinner,R.drawable.urology,R.drawable.cardiac,R.drawable.dentist,R.drawable.ent,R.drawable.ophthalmology,R.drawable.general_surgery,R.drawable.pregnant,R.drawable.spinner,R.drawable.spinner,R.drawable.spinner,R.drawable.paeditic,R.drawable.spinner,R.drawable.spinner,R.drawable.urology,R.drawable.spinner};
    }

    private void InitializationClass()
    {

    }

    private void InitializationUI()
    {
        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        editText=view.findViewById(R.id.edit_text_0);
        button=view.findViewById(R.id.button_0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                diseaseAdapter.getFilter().filter(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void CallDBForGetDiseaseList()
    {
        DoctorFilterDB doctorFilterDB=new DoctorFilterDB(activity);
        doctorFilterDB.GetDoctorCategoryAndUIDListFrom(DBConst.Specialization,DBConst.GetDiseaseTypeWithData);
    }

    private HashMap<String,Object> PatientAccountInfoHashMap;

    public void GetDataFromActivity(String WhichMethod,Object object)
    {
        switch (WhichMethod)
        {
            case DBConst.GetPatientAccountInformation:
                PatientAccountInfoHashMap=(HashMap<String, Object>)object;
                break;
            case DBConst.GetDiseaseTypeWithData:
                SetDiseaseListAndGetUIDList((HashMap<String,Object>)object);
                break;
            case DBConst.GetDoctorListByDisease:
                SetDoctorListByDisease((ArrayList<HashMap<String,Object>>)object);
                break;

        }
    }
    private void SetDoctorListByDisease(ArrayList<HashMap<String,Object>> DoctorListArrayList)
    {
        getFragmentManager().beginTransaction().replace(R.id.disease_fragment_container,new ExpandedFragmentForDoctorList(activity,ExpandedHeadingText,DoctorListArrayList,PatientAccountInfoHashMap)).commit();
    }
    private void SetDiseaseListAndGetUIDList(HashMap<String,Object> hashMap)
    {
        DiseaseArrayList=new ArrayList<>();
        DiseaseTypeUIDArrayList=new ArrayList<>();
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataParseModel dataParseModel=new DataParseForDoctorFiltration().GetTypeListAndUIDList(DBConst.Specialization,(DataSnapshot)hashMap.get(DBConst.DataSnapshot));
            DiseaseArrayList=dataParseModel.getArrayList1();
            DiseaseTypeUIDArrayList=dataParseModel.getArrayList2();

            ArrayList<Data> CustomDiseaseArrayList =new ArrayList<>();

            for (int i=0; i<DiseaseArrayList.size(); i++)
            {
                for(int k=0; k<DiseaseTypeList.length; k++)
                {
                    if (DiseaseTypeList[k].matches(DiseaseArrayList.get(i)))
                    {
                        CustomDiseaseArrayList.add(new Data(DiseaseImageResuource[k],DiseaseTypeShowList[k]));
                    }
                }
            }
            diseaseAdapter=new DiseaseAdapter((Activity) view.getContext(),CustomDiseaseArrayList);
            recyclerView.setAdapter(diseaseAdapter);
        }
    }

    class Data
    {
        int DiseaseTypeImage;

        public Data(int diseaseTypeImage, String diseaseType)
        {
            DiseaseTypeImage = diseaseTypeImage;
            DiseaseType = diseaseType;
        }

        String DiseaseType;

        public int getDiseaseTypeImage() {
            return DiseaseTypeImage;
        }

        public void setDiseaseTypeImage(int diseaseTypeImage) {
            DiseaseTypeImage = diseaseTypeImage;
        }

        public String getDiseaseType() {
            return DiseaseType;
        }

        public void setDiseaseType(String diseaseType) {
            DiseaseType = diseaseType;
        }
    }

    class DiseaseAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable
    {

        private Activity activity;
        private ArrayList<Data> arrayList=new ArrayList<>();
        private ArrayList<Data> arrayList1=new ArrayList<>();
        public DiseaseAdapter(Activity activity,ArrayList<Data> arrayList)
        {
            this.activity=activity;
            this.arrayList1=arrayList;
            this.arrayList=arrayList;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_disease_list_show_model,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
        {
            final Data data=arrayList.get(position);
            holder.getTextView().setText(data.getDiseaseType());
            holder.getCircleImageView().setImageResource(data.getDiseaseTypeImage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ExpandedHeadingText=arrayList.get(position).getDiseaseType();
                    CallDBForDoctorListByDiseaseType(DiseaseTypeUIDArrayList.get(position));
                }
            });
        }
        private void CallDBForDoctorListByDiseaseType(ArrayList<String> arrayList)
        {
            DoctorFilterDB doctorFilterDB=new DoctorFilterDB(activity);
            doctorFilterDB.GetDoctorList(DBConst.GetDoctorListByDisease,arrayList);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    if (charSequence.toString().isEmpty())
                    {
                        arrayList=arrayList1;
                    }
                    else
                    {
                        ArrayList<Data> arrayList2=new ArrayList<>();
                        for (Data data:arrayList1)
                        {
                            if (data.getDiseaseType().toLowerCase().contains(charSequence.toString().toLowerCase()))
                            {
                                arrayList2.add(data);
                            }
                        }

                        arrayList=arrayList2;
                    }
                    FilterResults filterResults=new FilterResults();
                    filterResults.values=arrayList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                {
                    arrayList=(ArrayList<Data>) filterResults.values;
                }
            };
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView circleImageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view_0);
            circleImageView=itemView.findViewById(R.id.image_civ_0);
        }

        public TextView getTextView() {
            return textView;
        }

        public CircleImageView getCircleImageView() {
            return circleImageView;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
