package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters;

import android.os.Environment;
import android.util.Log;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.HomeWork;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing.Group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class FileWorker  implements Serializable {
    private String fileName;
    private File path;

    public FileWorker(String fileName) {
        this.fileName = fileName;
        path = new File(Environment.getExternalStorageDirectory(), "TimeTableVoenmeh");
        if (!path.exists())
            path.mkdir();
        path = new File(path.getAbsolutePath(), fileName);
        if (!path.exists())
            try {
                path.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public boolean isExists() {
        return path.exists();
    }

    // сохранение файла
    public void saveText(String text) {
        if (text.equals("")) return;
        FileOutputStream fos = null;
        try {
            Log.d("FILEWORKER", "saveText: " + path.toString());
            fos = new FileOutputStream(path);
            text = text.toUpperCase();
            fos.write(text.getBytes());
        } catch (IOException ex) {
            Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            }
        }
    }

    // открытие файла
    public String openText() {
        String text = "";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(path);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String(bytes);
            if (text.equals("")) return null;
        } catch (IOException ex) {

            Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {

                Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            }
        }
        return text;
    }

    public void saveGroup(Group group) {
        if (group == null) return;
        Log.d("SAVEGROUP", "saveGroup: SAVING " + group.getName());
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = new FileOutputStream(path);
            os = new ObjectOutputStream(fos);
            os.writeObject(group);
        } catch (IOException ex) {
            Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            Log.d("EXCEPTION SAVING", "saveGroup: " + ex);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            }
        }
    }

    // открытие файла
    public Group readGroup() {
        Log.d("READGROUP", "saveGroup: READING ");

        FileInputStream fin = null;
        ObjectInput is = null;
        Group group = null;
        try {
            fin = new FileInputStream(path);
            is = new ObjectInputStream(fin);
            group = (Group) is.readObject();
            if (group != null)
                Log.d("READGROUP", "readGroup: " + group.getName());
            else
                Log.d("READ", "readGroup: ");
        } catch (IOException ex) {

            Log.d("EXCEPTION", "FILEWORKEREXCEPTION writing file: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (is != null)
                    is.close();
                if (fin != null)
                    fin.close();

            } catch (IOException ex) {

                Log.d("EXCEPTION", "FILEWORKEREXCEPTION reading file: " + ex.getMessage());
            }
        }
        return group;
    }
    public void saveHomeWorks(ArrayList<HomeWork> homeWorks) {
        if (homeWorks == null||homeWorks.size()==0) return;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = new FileOutputStream(path);
            os = new ObjectOutputStream(fos);
            os.writeObject(homeWorks);
        } catch (IOException ex) {
            Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            Log.d("EXCEPTION SAVING", "saveGroup: " + ex);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Log.d("EXCEPTION", "FILEWORKEREXCEPTION: " + ex.getMessage());
            }
        }
    }

    // открытие файла
    public ArrayList<HomeWork> readHomeWorks() {

        FileInputStream fin = null;
        ObjectInput is = null;
        ArrayList<HomeWork> homeWorks = null;
        try {
            fin = new FileInputStream(path);
            is = new ObjectInputStream(fin);
            homeWorks = (ArrayList<HomeWork>) is.readObject();
        } catch (IOException ex) {

            Log.d("EXCEPTION", "FILEWORKEREXCEPTION writing file: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (is != null)
                    is.close();
                if (fin != null)
                    fin.close();

            } catch (IOException ex) {

                Log.d("EXCEPTION", "FILEWORKEREXCEPTION reading file: " + ex.getMessage());
            }
        }
        return homeWorks;
    }
}
