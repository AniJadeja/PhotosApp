package com.example.collapsingtoolbar.utils;


//This class helps user to create and save
// their data into files as well as read the data from the files.


import android.content.Context;
import android.util.Log;

import com.example.collapsingtoolbar.Model.ImageModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private String dirName, fileName;
    private String TAG = "FileManager";
    private boolean isLogEnabled = false;
    private Context context;

    /*===============================================================   CONSTRUCTORS   ===============================================================*/

    public FileManager(String dirName, String fileName, Context context) {
        this.dirName = dirName;
        this.fileName = fileName;
        this.context = context;
    }

    public FileManager(String dirName, Context context) {
        this.dirName = dirName;
        this.context = context;
    }

    public FileManager(Context context) {
        this.context = context;
    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/


    public void writeToFile(ArrayList<?> arrayList) {

        //Create a Dir if it doesn't exists

        File Dir = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + dirName);
        if (!Dir.exists()) {
            boolean y = Dir.mkdirs();
            if (isLogEnabled) {
                if (y)
                    Log.d(TAG, "writeToFile: directory creation successful ");
                else
                    Log.d(TAG, "writeToFile: directory creation unsuccessful ");
            }
        }

        //Create a new file every time so, no data will be appended and all the data will be overwritten.

        File file = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + Dir.getName() + "/" + fileName);

        try {

            //Write Data to the file.

            FileOutputStream outputStreamWriter = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outputStreamWriter);
            oos.writeObject(arrayList);
            oos.close();
            outputStreamWriter.close();
            if (isLogEnabled)
                Log.d(TAG, "writeToFile: file Write successful ");
        } catch (IOException e) {
            if (isLogEnabled)
                Log.e(TAG, "Exception : File write failed : " + e.toString());
        }
    }

    public ArrayList<?> readFromFile() {

        try {
            FileInputStream fis = new FileInputStream(new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + dirName + "/" + fileName));
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (ArrayList<?>) ois.readObject();
        } catch (Exception ex) {
            if (isLogEnabled)
                Log.d(TAG, "readFromFile: exception " + ex.toString());
            return null;
        }
    }

    public void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }
}
