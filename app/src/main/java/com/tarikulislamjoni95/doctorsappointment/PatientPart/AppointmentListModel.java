package com.tarikulislamjoni95.doctorsappointment.PatientPart;

public class AppointmentListModel
{
    String AuthorityValidity;
    String UID;
    String HospitalName;
    String AvailableDay;
    String AppointmentTime;
    String AppointmentOffStart;
    String AppointmentOffEnd;
    String AppointmentFee;
    String Name;


    public AppointmentListModel() {
    }
    public AppointmentListModel(String AuthorityValidity,String UID,String Name, String hospitalName, String availableDay, String appointmentTime,String appointmentFee, String AppointmentOffStart,String AppointmentOffEnd) {

        this.AuthorityValidity=AuthorityValidity;
        this.Name=Name;
        this.UID = UID;
        HospitalName = hospitalName;
        AvailableDay = availableDay;
        AppointmentTime = appointmentTime;
        this.AppointmentOffStart = AppointmentOffStart;
        this.AppointmentOffEnd=AppointmentOffEnd;
        AppointmentFee = appointmentFee;
    }

    public String getAuthorityValidity() {
        return AuthorityValidity;
    }

    public void setAuthorityValidity(String authorityValidity) {
        AuthorityValidity = authorityValidity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAppointmentOffStart() {
        return AppointmentOffStart;
    }

    public void setAppointmentOffStart(String appointmentOffStart) {
        AppointmentOffStart = appointmentOffStart;
    }

    public String getAppointmentOffEnd() {
        return AppointmentOffEnd;
    }

    public void setAppointmentOffEnd(String appointmentOffEnd) {
        AppointmentOffEnd = appointmentOffEnd;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getAvailableDay() {
        return AvailableDay;
    }

    public void setAvailableDay(String availableDay) {
        AvailableDay = availableDay;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getAppointmentFee() {
        return AppointmentFee;
    }

    public void setAppointmentFee(String appointmentFee) {
        AppointmentFee = appointmentFee;
    }
}
