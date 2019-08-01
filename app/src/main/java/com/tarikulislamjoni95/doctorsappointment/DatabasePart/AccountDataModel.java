package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;

public class AccountDataModel
{
    private  String UID;
    //Patient Variable
    private String ProfileImageUrl;
    private String Name;
    private String FatherName;
    private String MotherName;
    private String ContactNo;
    private String Gender;
    private String BloodGroup;
    private String BirthDate;
    private String Address;
    private String BirthCertificateNo;
    private String BirthCertificateImageUrl;
    private String AnotherDocumentImageUrl;

    //Doctor Variable
    //Image
    private String Title;
    //Name
    private String StudiedCollege;
    private String Degree;
    private String Category;
    private String NoOfPracYear;
    private String AvailableArea;
    //Contact No
    private String BMDCRegNo;
    private String NIDNo;
    private String BMDCRegImageUrl;
    private String NIDImageUrl;

    public AccountDataModel()
    {

    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    //Doctor Model Constructor
    public AccountDataModel(String UID,String ProfileImageUrl,String Title,String Name,String StudiedCollege,String Degree,String Category,String NoOfPracYear,String AvailableArea,String ContactNo,String BMDCRegNo,String NIDNo,String BMDCRegImageUrl,String NIDImageUrl)
    {
        this.UID=UID;
        this.ProfileImageUrl=ProfileImageUrl;
        this.Title=Title;
        this.Name=Name;
        this.StudiedCollege=StudiedCollege;
        this.Degree=Degree;
        this.Category=Category;
        this.NoOfPracYear=NoOfPracYear;
        this.AvailableArea=AvailableArea;
        this.ContactNo=ContactNo;
        this.BMDCRegNo=BMDCRegNo;
        this.NIDNo=NIDNo;
        this.BMDCRegImageUrl=BMDCRegImageUrl;
        this.NIDImageUrl=NIDImageUrl;
    }
    public AccountDataModel(String ProfileImageUrl,String Title,String Name,String StudiedCollege,String Degree,String Category,String NoOfPracYear,String AvailableArea,String ContactNo,String BMDCRegNo,String NIDNo,String BMDCRegImageUrl,String NIDImageUrl)
    {
        this.ProfileImageUrl=ProfileImageUrl;
        this.Title=Title;
        this.Name=Name;
        this.StudiedCollege=StudiedCollege;
        this.Degree=Degree;
        this.Category=Category;
        this.NoOfPracYear=NoOfPracYear;
        this.AvailableArea=AvailableArea;
        this.ContactNo=ContactNo;
        this.BMDCRegNo=BMDCRegNo;
        this.NIDNo=NIDNo;
        this.BMDCRegImageUrl=BMDCRegImageUrl;
        this.NIDImageUrl=NIDImageUrl;
    }

    //Patient Model Constructor
    public AccountDataModel(String profileImageUrl, String name, String fatherName, String motherName, String contactNo, String gender, String bloodGroup, String birthDate, String address, String birthCertificateNo, String birthCertificateImageUrl, String anotherDocumentImageUrl) {
        ProfileImageUrl = profileImageUrl;
        Name = name;
        FatherName = fatherName;
        MotherName = motherName;
        ContactNo = contactNo;
        Gender = gender;
        BloodGroup = bloodGroup;
        BirthDate = birthDate;
        Address = address;
        BirthCertificateNo = birthCertificateNo;
        BirthCertificateImageUrl = birthCertificateImageUrl;
        AnotherDocumentImageUrl = anotherDocumentImageUrl;
    }

    //Patient Getter and Setter
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

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBirthCertificateNo() {
        return BirthCertificateNo;
    }

    public void setBirthCertificateNo(String birthCertificateNo) {
        BirthCertificateNo = birthCertificateNo;
    }

    public String getBirthCertificateImageUrl() {
        return BirthCertificateImageUrl;
    }

    public void setBirthCertificateImageUrl(String birthCertificateImageUrl) {
        BirthCertificateImageUrl = birthCertificateImageUrl;
    }

    public String getAnotherDocumentImageUrl() {
        return AnotherDocumentImageUrl;
    }

    public void setAnotherDocumentImageUrl(String anotherDocumentImageUrl) {
        AnotherDocumentImageUrl = anotherDocumentImageUrl;
    }

    //Doctor Getter and Setter

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStudiedCollege() {
        return StudiedCollege;
    }

    public void setStudiedCollege(String studiedCollege) {
        StudiedCollege = studiedCollege;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getNoOfPracYear() {
        return NoOfPracYear;
    }

    public void setNoOfPracYear(String noOfPracYear) {
        NoOfPracYear = noOfPracYear;
    }

    public String getAvailableArea() {
        return AvailableArea;
    }

    public void setAvailableArea(String availableArea) {
        AvailableArea = availableArea;
    }

    public String getBMDCRegNo() {
        return BMDCRegNo;
    }

    public void setBMDCRegNo(String BMDCRegNo) {
        this.BMDCRegNo = BMDCRegNo;
    }

    public String getNIDNo() {
        return NIDNo;
    }

    public void setNIDNo(String NIDNo) {
        this.NIDNo = NIDNo;
    }

    public String getBMDCRegImageUrl() {
        return BMDCRegImageUrl;
    }

    public void setBMDCRegImageUrl(String BMDCRegImageUrl) {
        this.BMDCRegImageUrl = BMDCRegImageUrl;
    }

    public String getNIDImageUrl() {
        return NIDImageUrl;
    }

    public void setNIDImageUrl(String NIDImageUrl) {
        this.NIDImageUrl = NIDImageUrl;
    }
}
