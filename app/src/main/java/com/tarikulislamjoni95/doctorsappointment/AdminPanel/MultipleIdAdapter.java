package com.tarikulislamjoni95.doctorsappointment.AdminPanel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class MultipleIdAdapter extends ArrayAdapter<MultipleIdDataModel> implements View.OnClickListener
{
    private MyLoadingDailog myLoadingDailog;

    ViewHolder viewHolder;
    MultipleIdDataModel multipleIdDataModel;
    String BirthCertificateNo;
    String UID;
    private ArrayList<MultipleIdDataModel> arrayList;
    Context context;

    public MultipleIdAdapter(String BirthCertificateNo,ArrayList<MultipleIdDataModel> arrayList, Context context) {
        super(context, R.layout.multiple_id_row_model,arrayList);
        this.BirthCertificateNo=BirthCertificateNo;
        this.arrayList=arrayList;
        this.context=context;
        myLoadingDailog=new MyLoadingDailog((Activity) context,R.drawable.spinner);
    }

    MultipleIdDataModel dataModel;
    @Override
    public void onClick(View view)
    {
        myLoadingDailog.show();
        dataModel=(MultipleIdDataModel) getItem((int)((Integer) view.getTag()));
        switch (view.getId())
        {
            case R.id.delete_data_btn:
                DeleteBNDataMethod();
                break;
        }
    }

    private void DeleteBNDataMethod()
    {
        final String UID=dataModel.getUID();
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.BirthNoMultiplicity);
        ref.child(BirthCertificateNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(BirthCertificateNo).getChildrenCount()==2)
                {
                    ref.child(BirthCertificateNo).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            InvalidAndUnAuthorizingMethod(UID);
                        }
                    });
                }
                else
                {
                   ref.child(BirthCertificateNo).child(UID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete())
                            {
                                InvalidAndUnAuthorizingMethod(UID);
                                ref.child(BirthCertificateNo).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.getChildrenCount()==2)
                                        {
                                            ref.child(BirthCertificateNo).child(DBConst.MultipleCheck).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        if (myLoadingDailog.isShowing())
                                        {
                                            myLoadingDailog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
            }
        });
    }

    private void InvalidAndUnAuthorizingMethod(final String UID)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
        ref.child(DBConst.AuthorityValidity).setValue(false);
        ref.child(DBConst.AccountValidity).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    if(myLoadingDailog.isShowing())
                    {
                        myLoadingDailog.dismiss();
                    }
                    //DeleteDataFromStorage(BirthCertificateNo,UID);
                }
            }
        });

    }


    private void DeleteDataFromStorage(String BirthCertificateNo, final String UID)
    {
        final String BirthCertificateImageName= UID+"BirthCertificate"+".jpg";
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData);
        ref.child(BirthCertificateImageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ref.child(BirthCertificateImageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DeleteSecondData(UID);
                    }
                });
            }
        });
    }
    private void DeleteSecondData(final String UID)
    {
        final String AnotherDocumentImageName=UID+"AnotherDocument"+".jpg";
        final StorageReference ref=FirebaseStorage.getInstance().getReference().child(DBConst.SecureData);
        ref.child(AnotherDocumentImageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri)
            {
                ref.child(AnotherDocumentImageName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            UpdateAccountInfo(UID);
                        }
                    }
                });
            }
        });
    }
    private void UpdateAccountInfo(String UID)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(UID);
        reference.child(DBConst.BirthCertificateNo).setValue("Wrong Birth Info");
        reference.child(DBConst.BirthCertificateImageUrl).setValue("null");
        reference.child(DBConst.AnotherDocumentImageUrl).setValue("null")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (myLoadingDailog.isShowing())
                        {
                            myLoadingDailog.dismiss();
                        }
                        Toast.makeText(context,"Deleted successfully",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private class ViewHolder
    {
        TextView NameTv,BirthDateTv,ContactNoTv,MultipleCountTv;
        ImageView BirthIV,AnotherDocIV;
        Button InvalidBtn,DeleteBirthInfoBtn,UnAthorizationBtn;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        multipleIdDataModel=getItem(position);
        View result;
        if (convertView==null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.multiple_id_row_model,parent,false);

            viewHolder.MultipleCountTv=convertView.findViewById(R.id.multiple_count_tv);
            viewHolder.NameTv=convertView.findViewById(R.id.name_tv);
            viewHolder.BirthDateTv=convertView.findViewById(R.id.birth_tv);
            viewHolder.ContactNoTv=convertView.findViewById(R.id.contact_no_tv);
            viewHolder.BirthIV=convertView.findViewById(R.id.birth_doc_iv);
            viewHolder.BirthIV.setOnClickListener(this);
            viewHolder.BirthIV.setTag(position);
            viewHolder.AnotherDocIV=convertView.findViewById(R.id.another_doc_iv);
            viewHolder.AnotherDocIV.setOnClickListener(this);
            viewHolder.AnotherDocIV.setTag(position);
            viewHolder.InvalidBtn=convertView.findViewById(R.id.invalid_btn);
            viewHolder.InvalidBtn.setOnClickListener(this);
            viewHolder.InvalidBtn.setTag(position);
            viewHolder.DeleteBirthInfoBtn=convertView.findViewById(R.id.delete_data_btn);
            viewHolder.DeleteBirthInfoBtn.setOnClickListener(this);
            viewHolder.DeleteBirthInfoBtn.setTag(position);
            viewHolder.UnAthorizationBtn=convertView.findViewById(R.id.unathorize_btn);
            viewHolder.UnAthorizationBtn.setOnClickListener(this);
            viewHolder.UnAthorizationBtn.setTag(position);

            result=convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.MultipleCountTv.setText("Multiple ID : "+String.valueOf(position+1));
        viewHolder.NameTv.setText(multipleIdDataModel.getName());
        viewHolder.BirthDateTv.setText(multipleIdDataModel.getBirthDate());
        viewHolder.ContactNoTv.setText(multipleIdDataModel.getContactNo());
        if (!multipleIdDataModel.getBirthImageUrl().matches("null"))
        {
            Picasso.get().load(multipleIdDataModel.getBirthImageUrl()).into(viewHolder.BirthIV);
        }
        if (!multipleIdDataModel.getAnotherDocImageUrl().matches("null"))
        {
            Picasso.get().load(multipleIdDataModel.getAnotherDocImageUrl()).into(viewHolder.AnotherDocIV);
        }


        return convertView;
    }
}
