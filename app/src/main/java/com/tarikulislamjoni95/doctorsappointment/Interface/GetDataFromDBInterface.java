package com.tarikulislamjoni95.doctorsappointment.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface GetDataFromDBInterface
{
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String,Object> DataHashMap);
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList);
}
