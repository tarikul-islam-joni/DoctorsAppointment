package com.tarikulislamjoni95.doctorsappointment.Interface;

import java.util.Map;

public interface MyCommunicator
{
    public void Communicator(String FromWhichMethod, boolean result);
    public void GetDataFromCommunicator(String FromWhichMethod, Map<Integer, String> map);
}
