package com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl
{
    public String readUrl(String myUrl)
    {
        String data="";
        InputStream inputStream=null;
        HttpURLConnection httpURLConnection=null;
        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer=null;

        try {
            URL url=new URL(myUrl);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream=httpURLConnection.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            stringBuffer=new StringBuffer();
            String line="";
            while ((line=bufferedReader.readLine())!=null)
            {
                stringBuffer.append(line);
            }
            data=stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection!=null)
            {
                httpURLConnection.disconnect();
            }
            if (bufferedReader!=null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return data;
    }
}
