package com.tarikulislamjoni95.doctorsappointment.PatientPart;

public class PatientDataModel
{
    String From;
    String UID;
    String Name;
    String HospitalName;
    String AppointmentDate;
    String AppointmentTime;
    String AppointmentValidity;
    String AppointmentFee;

    String AvailableDay;
    String AppointmentOffStart;
    String AppointmentOffEnd;


    //Constructor for show appointment list
    public PatientDataModel(String From, String UID, String name, String hospitalName, String appointmentDate, String appointmentTime, String appointmentValidity, String appointmentFee) {
        this.From=From;
        this.UID = UID;
        Name = name;
        HospitalName = hospitalName;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        AppointmentValidity = appointmentValidity;
        AppointmentFee = appointmentFee;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getAppointmentValidity() {
        return AppointmentValidity;
    }

    public void setAppointmentValidity(String appointmentValidity) {
        AppointmentValidity = appointmentValidity;
    }

    public String getAppointmentFee() {
        return AppointmentFee;
    }

    public void setAppointmentFee(String appointmentFee) {
        AppointmentFee = appointmentFee;
    }
}
