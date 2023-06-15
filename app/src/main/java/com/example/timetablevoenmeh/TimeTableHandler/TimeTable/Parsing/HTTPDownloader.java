package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HTTPDownloader implements Serializable {
    private String defaultUrl = "https://www.voenmeh.ru/templates/jd_atlanta/js/TimetableGroup33.xml";
    private Group myGroup;

    private String groupName="О719Б";
    private String groups;
    private String TAG="HTTPDOWNLOADER";

    public void Download(String groupName) throws IOException {
        groups= "";
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;

        try {
            Log.i(TAG, "Download: I TRY CONNECT");
            URL url = new URL(defaultUrl);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            Log.i(TAG, "Download: CONNECTED");
            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line + "\n");
            }
            Log.i(TAG, "Download: GOR BODY");
            String body = xmlResult.toString();
            Log.i(TAG, "Download: "+body.length());
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
