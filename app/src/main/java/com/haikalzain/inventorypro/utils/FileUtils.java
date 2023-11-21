package com.haikalzain.inventorypro.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.haikalzain.inventorypro.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by haikalzain on 6/01/15.
 */
public class FileUtils {
    public static boolean isFileNameValid(String fileName){ //Not perfect but will do
        //Dot also banned to make things easy
        final String[] ReservedChars = {"|", "\\", "?", "*", "<", "\"", ":", ">", "."};


        for(String c :ReservedChars){
            if(fileName.contains(c)){
                return false;
            }
        }

        return true;
    }

    public static File getTemplatesDirectory(Context context){
        String FolderName=context.getString(R.string.app_name);

        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + FolderName +"/templates");
        }
        else
        {
            dir = new File(Environment.getExternalStorageDirectory() + FolderName +"/templates");
        }

        // Make sure the path directory exists.
        if (!dir.exists())
        {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success)
            {
                dir = null;
            }
        }
        return dir;
    }

    public static File getSpreadsheetsDirectory(Context context){
        String FolderName=context.getString(R.string.app_name);

        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + FolderName +"/spreadsheets");
        }
        else
        {
            dir = new File(Environment.getExternalStorageDirectory() + File.separator + FolderName +"/spreadsheets");
        }

        // Make sure the path directory exists.
        if (!dir.exists())
        {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success)
            {
                dir = null;
            }
        }
        return dir;
    }

    public static List<File> getTemplateFiles(Context context){
        return new ArrayList<>(Arrays.asList(getTemplatesDirectory(context).listFiles()));
    }

    public static List<File> getSpreadsheetFiles(Context context){
        return new ArrayList<>(Arrays.asList(getSpreadsheetsDirectory(context).listFiles()));
    }

    public static boolean deleteSpreadsheet(Context context, String fileName){
        return new File(getSpreadsheetsDirectory(context), fileName).delete();
    }

    public static boolean deleteTemplate(Context context, String fileName){
        return new File(getTemplatesDirectory(context), fileName).delete();
    }

    public static List<String> getFileNamesWithoutExt(List<File> files){
        List<String> names = new ArrayList<>();
        for(File f: files){
            names.add(getFileNameWithoutExt(f));
        }
        return names;
    }

    public static List<String> getFileNames(List<File> files){
        List<String> names = new ArrayList<>();
        for(File f: files){
            names.add(f.getName());
        }
        return names;
    }

    public static String getFileNameWithoutExt(File file){
        int last = file.getName().lastIndexOf(".");
        return file.getName().substring(0, last);
    }

    public static String getFileNameWithoutExt(String fileName){
        int last = fileName.lastIndexOf(".");
        return fileName.substring(0, last);
    }



}
