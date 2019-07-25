package com.tarikulislamjoni95.doctorsappointment;

public class AppointmentDataModel {
    String UID;
    String HospitalName;
    String AvailableDay;
    String UnavaiableSDate;
    String AppointmentTime;
    String UnavaiableEDate;
    String AppointmentFee;

    public AppointmentDataModel(String UID,String HospitalName,String availableDay,String AppointmentFee, String unavaiableSDate, String AppointmentTime, String unavaiableEDate) {
        this.UID = UID;
        this.HospitalName = HospitalName;
        this.AppointmentTime = AppointmentTime;
        AvailableDay = availableDay;
        UnavaiableSDate = unavaiableSDate;
        UnavaiableEDate = unavaiableEDate;
        this.AppointmentFee = AppointmentFee;
    }

    public String getAppointmentFee() {
        return AppointmentFee;
    }

    public void setAppointmentFee(String appointmentFee) {
        AppointmentFee = appointmentFee;
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

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String AppointmentTime) {
        this.AppointmentTime = AppointmentTime;
    }

    public String getAvailableDay() {
        return AvailableDay;
    }

    public void setAvailableDay(String availableDay) {
        AvailableDay = availableDay;
    }

    public String getUnavaiableSDate() {
        return UnavaiableSDate;
    }

    public void setUnavaiableSDate(String unavaiableSDate) {
        UnavaiableSDate = unavaiableSDate;
    }

    public String getUnavaiableEDate() {
        return UnavaiableEDate;
    }

    public void setUnavaiableEDate(String unavaiableEDate) {
        UnavaiableEDate = unavaiableEDate;
    }
}