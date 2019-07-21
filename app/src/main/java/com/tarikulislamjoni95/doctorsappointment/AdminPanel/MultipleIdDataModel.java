package com.tarikulislamjoni95.doctorsappointment.AdminPanel;

public class MultipleIdDataModel
{
    private String UID;
    private String Name;
    private String BirthDate;
    private String ContactNo;
    private String BirthImageUrl;
    private String AnotherDocImageUrl;

    public MultipleIdDataModel(MultipleIdDataModel multipleIdDataModel)
    {

    }

    public MultipleIdDataModel(String UID,String name, String birthDate, String contactNo, String birthImageUrl, String anotherDocImageUrl) {
       this.UID=UID;
        Name = name;
        BirthDate = birthDate;
        ContactNo = contactNo;
        BirthImageUrl = birthImageUrl;
        AnotherDocImageUrl = anotherDocImageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getBirthImageUrl() {
        return BirthImageUrl;
    }

    public void setBirthImageUrl(String birthImageUrl) {
        BirthImageUrl = birthImageUrl;
    }

    public String getAnotherDocImageUrl() {
        return AnotherDocImageUrl;
    }

    public void setAnotherDocImageUrl(String anotherDocImageUrl) {
        AnotherDocImageUrl = anotherDocImageUrl;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
