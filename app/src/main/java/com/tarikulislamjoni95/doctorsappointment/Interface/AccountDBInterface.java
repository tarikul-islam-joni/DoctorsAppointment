package com.tarikulislamjoni95.doctorsappointment.Interface;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;

import java.util.ArrayList;

public interface AccountDBInterface
{
    public void GetAccount(boolean result,ArrayList<AccountDataModel> arrayList);
    public void AccountSavingResult(boolean result);
}
