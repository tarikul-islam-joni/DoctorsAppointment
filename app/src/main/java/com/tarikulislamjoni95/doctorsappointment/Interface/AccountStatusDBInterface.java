package com.tarikulislamjoni95.doctorsappointment.Interface;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;

import java.util.ArrayList;

public interface AccountStatusDBInterface
{
    public void GetAccountStatus(boolean result,ArrayList<AccountStatusDataModel> arrayList);
    public void AccountStatusSavingResult(boolean result);
}
