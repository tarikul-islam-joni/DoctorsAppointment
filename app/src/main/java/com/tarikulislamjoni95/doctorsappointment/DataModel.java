package com.tarikulislamjoni95.doctorsappointment;

public class DataModel
{
    String From;
    String Name;
    String UID;
    String HospitalName;
    String AppointmentDate;
    String AppointmentTime;
    String AppointmentFee;
    String AppointmentValidityTime;

    public DataModel()
    {

    }

    public DataModel(String name, String hospitalName, String appointmentDate, String appointmentTime) {
        Name = name;
        HospitalName = hospitalName;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
    }

    public DataModel(String From, String UID, String name, String hospitalName, String appointmentDate, String appointmentTime, String appointmentFee, String ValidityTime) {
        this.From=From;
        Name = name;
        this.UID = UID;
        HospitalName = hospitalName;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        AppointmentFee = appointmentFee;
        AppointmentValidityTime=ValidityTime;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
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

    public String getAppointmentValidityTime() {
        return AppointmentValidityTime;
    }

    public void setAppointmentValidityTime(String appointmentValidityTime) {
        AppointmentValidityTime = appointmentValidityTime;
    }
}
