package com.tarikulislamjoni95.doctorsappointment.PatientPart;

public class DoctorListModel
{
    private String BMDCRegNo;
    private String Degree;
    private String NoOfPracYear;
    private String Title;
    private String Name;
    private String Image;
    private String AvailableArea;
    private String StudiedCollege;
    private String Category;

    public DoctorListModel()
    {

    }
    public DoctorListModel(String BMDCRegNo, String degree, String noOfPracYear, String title, String name, String image, String availableArea, String studiedCollege, String category) {
        this.BMDCRegNo = BMDCRegNo;
        Degree = degree;
        NoOfPracYear = noOfPracYear;
        Title = title;
        Image = image;
        AvailableArea = availableArea;
        StudiedCollege = studiedCollege;
        Category = category;
        Name = name;
    }

    public String getBMDCRegNo() {
        return BMDCRegNo;
    }

    public void setBMDCRegNo(String BMDCRegNo) {
        this.BMDCRegNo = BMDCRegNo;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getNoOfPracYear() {
        return NoOfPracYear;
    }

    public void setNoOfPracYear(String noOfPracYear) {
        NoOfPracYear = noOfPracYear;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAvailableArea() {
        return AvailableArea;
    }

    public void setAvailableArea(String availableArea) {
        AvailableArea = availableArea;
    }

    public String getStudiedCollege() {
        return StudiedCollege;
    }

    public void setStudiedCollege(String studiedCollege) {
        StudiedCollege = studiedCollege;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
