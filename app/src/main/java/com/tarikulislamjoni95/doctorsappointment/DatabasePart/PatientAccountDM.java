package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

public class PatientAccountDM
{
    private String UID;
    private String ProfileImageUrl;
    private String Name;
    private String FatherName;
    private String MotherName;
    private String PhoneNumber;
    private String Gender;
    private String DateOfBirth;
    private String BloodGroup;
    private String Address;
    private String BirthNumber;
    private String BirthNumberImageUrl;
    private String AnotherDocumentImageUrl;

    public PatientAccountDM() { }

    public PatientAccountDM(String UID, String profileImageUrl, String name, String fatherName, String motherName, String phoneNumber, String gender, String dateOfBirth, String bloodGroup, String address, String birthNumber, String birthNumberImageUrl, String anotherDocumentImageUrl)
    {
        this.UID = UID;
        ProfileImageUrl = profileImageUrl;
        Name = name;
        FatherName = fatherName;
        MotherName = motherName;
        PhoneNumber = phoneNumber;
        Gender = gender;
        DateOfBirth = dateOfBirth;
        BloodGroup = bloodGroup;
        Address = address;
        BirthNumber = birthNumber;
        BirthNumberImageUrl = birthNumberImageUrl;
        AnotherDocumentImageUrl = anotherDocumentImageUrl;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBirthNumber() {
        return BirthNumber;
    }

    public void setBirthNumber(String birthNumber) {
        BirthNumber = birthNumber;
    }

    public String getBirthNumberImageUrl() {
        return BirthNumberImageUrl;
    }

    public void setBirthNumberImageUrl(String birthNumberImageUrl) {
        BirthNumberImageUrl = birthNumberImageUrl;
    }

    public String getAnotherDocumentImageUrl() {
        return AnotherDocumentImageUrl;
    }

    public void setAnotherDocumentImageUrl(String anotherDocumentImageUrl) {
        AnotherDocumentImageUrl = anotherDocumentImageUrl;
    }
}
