package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

public class AccountStatusDataModel
{
    private String UID;
    private boolean AccountCompletion;
    private String AccountType;
    private boolean AccountValidity;
    private boolean AuthorityValidity;

    public AccountStatusDataModel()
    {

    }
    public AccountStatusDataModel(String UID, boolean accountCompletion, String accountType, boolean accountValidity, boolean authorityValidity) {
        this.UID=UID;
        AccountCompletion = accountCompletion;
        AccountType = accountType;
        AccountValidity = accountValidity;
        AuthorityValidity = authorityValidity;
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

    public boolean isAuthorityValidity() {
        return AuthorityValidity;
    }

    public void setAuthorityValidity(boolean authorityValidity) {
        AuthorityValidity = authorityValidity;
    }
}
