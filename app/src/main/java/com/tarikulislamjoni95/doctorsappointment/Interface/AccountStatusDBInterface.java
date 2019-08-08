package com.tarikulislamjoni95.doctorsappointment.Interface;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;

import java.util.ArrayList;

public interface AccountStatusDBInterface
{
    public void GetAccountStatus(boolean result,ArrayList<AccountStatusDM> arrayList);
    public void AccountStatusSavingResult(boolean result);
}
