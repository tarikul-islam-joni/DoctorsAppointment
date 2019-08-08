package com.tarikulislamjoni95.doctorsappointment.Interface;

import java.util.ArrayList;

public interface AccountDBInterface
{
    public void GetAccount(boolean result,ArrayList<PatientAccountDataModel> arrayList);
    public void AccountSavingResult(boolean result);
}
