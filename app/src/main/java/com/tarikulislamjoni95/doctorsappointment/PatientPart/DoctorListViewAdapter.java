package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListViewAdapter extends RecyclerView.Adapter<DoctorListViewAdapter.ViewHolder> implements View.OnClickListener , Filterable
{
    private Activity activity;
    private ArrayList<HashMap<String,Object>> arrayList;
    private ArrayList<HashMap<String,Object>> arrayList1;
    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout;
        TextView[] textViews;
        CircleImageView[] circleImageViews;
        Button[] buttons;

        public ViewHolder(View itemView)
        {
            super(itemView);
            InitializationUIHelperClass initializationUIHelperClass=new InitializationUIHelperClass(itemView);
            int[] text_views_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2};
            int[] button_id={R.id.button_0};
            int[] circular_image_view_id={R.id.image_civ_0};
            textViews=initializationUIHelperClass.setTextViews(text_views_id);
            buttons=initializationUIHelperClass.setButtons(button_id);
            circleImageViews=initializationUIHelperClass.setCircleImageViews(circular_image_view_id);
            linearLayout=itemView.findViewById(R.id.linear_layout_0);
        }
    }

    @Override
    public void onClick(View view)
    {
        int Position=(Integer)view.getTag();
        if(arrayList.get(Position).containsKey(DBConst.UID))
        {
            String UID=arrayList.get(Position).get(DBConst.UID).toString();
            Intent intent=new Intent(activity,DoctorProfileVisitActivity.class);
            intent.putExtra(DBConst.UID,UID);
            intent.putExtra(DBConst.PatientAccount,PatientAccountInformationHashMap);
            activity.startActivity(intent);
        }
    }

    private HashMap<String,Object> PatientAccountInformationHashMap;
    public DoctorListViewAdapter(Activity activity,ArrayList<HashMap<String,Object>> arrayList,HashMap<String,Object> PatientAccountInformationHashMap)
    {
        this.activity=activity;
        this.arrayList=arrayList;
        this.arrayList1=arrayList;
        this.PatientAccountInformationHashMap=PatientAccountInformationHashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_doctor_show_model,parent,false));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int Position)
    {
        viewHolder.buttons[0].setTag(Position);

        viewHolder.linearLayout.setTag(Position);
        viewHolder.linearLayout.setOnClickListener(this);

        viewHolder.buttons[0].setOnClickListener(this);

        HashMap<String,Object> hashMap_1=new HashMap<>();
        hashMap_1=arrayList.get(Position);
        String[] Data=GetData(hashMap_1);
        for(int i=0;i<Data.length; i++)
        {
            if(i==0)
            {
                if (!Data[i].matches(DBConst.UNKNOWN))
                {
                    Picasso.get().load(Data[i]).into(viewHolder.circleImageViews[0]);
                }
            }
            else
            {
                viewHolder.textViews[i-1].setText(Data[i]);
            }
        }

    }
    private String[] GetData(HashMap<String, Object> hashMap)
    {
        String[] DataKey={DBConst.ProfileImageUrl,DBConst.Name,DBConst.Specialization,DBConst.AvailableArea};
        String[] Data=new String[DataKey.length];
        for(int i=0; i<DataKey.length; i++)
        {
            if (hashMap.containsKey(DataKey[i]))
            {
                Data[i]=hashMap.get(DataKey[i]).toString();
            }
            else
            {
                Data[i]=DBConst.UNKNOWN;
            }
        }
        return Data;
    }


    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String SearchString=charSequence.toString();
                if (SearchString.isEmpty())
                {
                    arrayList=arrayList1;
                }
                else
                {
                    ArrayList<HashMap<String,Object>> filter_list=new ArrayList<>();
                    for (HashMap<String,Object> hashMap : arrayList1)
                    {
                        if (hashMap.get(DBConst.Name).toString().toLowerCase().contains(SearchString.toLowerCase()))
                        {
                            filter_list.add(hashMap);
                        }
                    }
                    arrayList=filter_list;
                }

                FilterResults filterResults=new FilterResults();
                filterResults.values=arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                arrayList=(ArrayList<HashMap<String, Object>>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
