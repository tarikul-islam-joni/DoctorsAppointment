package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import java.util.ArrayList;

public class DataParseModel
{
    ArrayList<String> arrayList1;
    ArrayList<ArrayList<String>> arrayList2;

    public DataParseModel(ArrayList<String> arrayList1, ArrayList<ArrayList<String>> arrayList2) {
        this.arrayList1 = arrayList1;
        this.arrayList2 = arrayList2;
    }

    public ArrayList<String> getArrayList1() {
        return arrayList1;
    }

    public void setArrayList1(ArrayList<String> arrayList1) {
        this.arrayList1 = arrayList1;
    }

    public ArrayList<ArrayList<String>> getArrayList2() {
        return arrayList2;
    }

    public void setArrayList2(ArrayList<ArrayList<String>> arrayList2) {
        this.arrayList2 = arrayList2;
    }
}
