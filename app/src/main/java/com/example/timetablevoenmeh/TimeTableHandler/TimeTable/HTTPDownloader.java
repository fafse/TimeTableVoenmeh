package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HTTPDownloader {
    private String defaultUrl = "https://www.voenmeh.ru/templates/jd_atlanta/js/TimetableGroup33.xml";
    private Group myGroup;

    private String groupName="О719Б";
    private String groups;

    public void Download() throws IOException {
        groups= "";
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(defaultUrl);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line + "\n");
            }
            String body = xmlResult.toString();
            body=body.substring(body.indexOf("<Group Number=\"")+15,body.length());
            Group updatedGroup=null;
            for (String group :
                    List.of(body.split("<Group Number=\""))) {
                groups+=group.substring(0,group.indexOf("\""));
                groups+="\n";
                if(group.contains(groupName))
                    updatedGroup= new Group(group);

            }
            if(updatedGroup!=null)
            {
                myGroup=updatedGroup;
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

    public void setGroupName(String newGroupName)
    {
        this.groupName=newGroupName;
    }
}
