package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.DBHelperInterface;

public class DBHelper
{
    private DBHelperInterface dbHelperInterface;
    private Activity activity;
    public DBHelper(Activity activity)
    {
        this.activity=activity;
        dbHelperInterface=(DBHelperInterface)activity;
    }
    public void GetUID()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            dbHelperInterface.GetUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        else
        {
            dbHelperInterface.GetUID(VARConst.UNKNOWN);
        }
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    private void DeleteUID()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }
    }
}
