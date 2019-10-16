package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseDoctorListFromDataParseModel
{
    private ArrayList<String> arrayList;
    private ArrayList<HashMap<String,Object>> hashMapArrayList;

    public ParseDoctorListFromDataParseModel(ArrayList<String> arrayList, ArrayList<HashMap<String, Object>> hashMapArrayList) {
        this.arrayList = arrayList;
        this.hashMapArrayList = hashMapArrayList;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<HashMap<String, Object>> getHashMapArrayList() {
        return hashMapArrayList;
    }

    public void setHashMapArrayList(ArrayList<HashMap<String, Object>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
    }
}
