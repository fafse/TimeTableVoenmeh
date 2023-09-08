package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing;



import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.FileWorker;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Utf8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class HTTPDownloader implements Serializable {
    private String defaultUrl = "https://www.voenmeh.ru/templates/jd_atlanta/js/TimetableGroup";
    
    private Group myGroup;

    private String groupName="О719Б";
    private String groups;
    private String TAG="HTTPDOWNLOADER";

    public int findUrl()
    {
        Log.i(TAG, "Download: I TRY CONNECT");
        URL url = null;
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        int prefix=0;
        HttpsURLConnection connection = null;
        try {
            url = new URL("https://www.voenmeh.ru/templates/jd_atlanta/js");
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            Log.i(TAG, "findUrl: CONNECTED");
            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line + "\n");
            }
            Log.i(TAG, "findUrl: GOR BODY");
            String[] lines = xmlResult.toString().split("\n");
            for(String l:lines)
            {
                Pattern p = Pattern.compile("(.*)TimetableGroup([0-9]+)\\.xml(.*)");
                Matcher m = p.matcher(l);
                while(m.find()) {
                    Log.i(TAG, "findUrl: " + m.group(2));
                    prefix= Integer.parseInt(m.group(2))>prefix?Integer.parseInt(m.group(2)):prefix;
                }
            }
        } catch (MalformedURLException e) {
            prefix=32;
        } catch (IOException e) {
            prefix=32;
        }
        return prefix;
    }

    public void Download(String groupName) throws IOException {
        groups= "";
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;

        try {
            defaultUrl+=String.valueOf(findUrl())+".xml";
            Log.i(TAG, "Download: I TRY CONNECT");
            URL url = new URL(defaultUrl);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_16));
            Log.i(TAG, "Download: CONNECTED");
            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line + "\n");
            }
            Log.i(TAG, "Download: GOT BODY");
            String body = xmlResult.toString();
            body=body.substring(body.indexOf("<Group Number=\"")+15,body.length());
            Group updatedGroup=null;
            for (String group :
                    List.of(body.split("<Group Number=\""))) {
                groups+=group.substring(0,group.indexOf("\""));
                groups+="\n";
                if(group.contains(groupName)) {
                    updatedGroup = new Group(group);
                }

            }
            if(updatedGroup!=null)
            {
                Log.d("HTTPDOWNLOADER", "Download updatedGROUP: "+updatedGroup.getName());
                myGroup=updatedGroup;
                this.groupName = myGroup.getName();
                Log.d("HTTPDOWNLOADER", "Download mygroupAFTER: "+myGroup.getName());
            }

        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }


    }

    public String getGroups()
    {
        return groups;
    }

    public Group getCurrentGroup() {
        Group currentGroup=myGroup;
        return currentGroup;
    }

}
