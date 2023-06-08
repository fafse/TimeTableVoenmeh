package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HTTPDownloader {
    private String defaultUrl = "https://www.voenmeh.ru/templates/jd_atlanta/js/TimetableGroup33.xml";
    private List<Group> groups;

    public void Download() throws IOException {
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
            groups = new ArrayList<>();
            body=body.substring(body.indexOf("<Group Number=\"")+15,body.length());
            for (String group :
                    List.of(body.split("<Group Number=\""))) {
                groups.add(new Group(group));
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

    public Group getCurrentGroup(String groupName) {
        for (Group resultGrop:
             groups) {
            if(resultGrop.getName().equals(groupName))
                return resultGrop;
        }
        return null;
    }
}
