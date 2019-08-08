package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

public class AccountStatusDM
{
    private String UID;
    private String AccountType;
    private boolean AccountCompletion;
    private boolean AccountValidity;

    public AccountStatusDM()
    {

    }
    public AccountStatusDM(String UID, String accountType, boolean accountCompletion, boolean accountValidity) {
        this.UID=UID;
        AccountCompletion = accountCompletion;
        AccountType = accountType;
        AccountValidity = accountValidity;
    }



    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isAccountCompletion() {
        return AccountCompletion;
    }

    public void setAccountCompletion(boolean accountCompletion) {
        AccountCompletion = accountCompletion;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public boolean isAccountValidity() {
        return AccountValidity;
    }

    public void setAccountValidity(boolean accountValidity) {
        AccountValidity = accountValidity;
    }

}
