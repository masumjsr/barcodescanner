package com.haikalzain.inventorypro.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import com.haikalzain.inventorypro.R
import java.io.File
import java.util.Arrays

/**
 * Created by haikalzain on 6/01/15.
 */
object FileUtils {
    fun isFileNameValid(fileName: String): Boolean { //Not perfect but will do
        //Dot also banned to make things easy
        val ReservedChars = arrayOf("|", "\\", "?", "*", "<", "\"", ":", ">", ".")
        for (c in ReservedChars) {
            if (fileName.contains(c)) {
                return false
            }
        }
        return true
    }

    fun getTemplatesDirectory(context: Context): File? {
        val FolderName = context.getString(R.string.app_name)
        var dir: File? = null
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + File.separator + FolderName + "/templates"
            )
        } else {
            File(
                Environment.getExternalStorageDirectory().toString() + FolderName + "/templates"
            )
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

    fun getSpreadsheetsDirectory(context: Context): File? {
        val FolderName = context.getString(R.string.app_name)
        var dir: File? = null
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + File.separator + FolderName + "/spreadsheets"
            )
        } else {
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + FolderName + "/spreadsheets"
            )
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

    fun getTemplateFiles(context: Context): List<File> {
        return ArrayList(
            Arrays.asList(
                *getTemplatesDirectory(context)!!
                    .listFiles()
            )
        )
    }

    fun getSpreadsheetFiles(context: Context): List<File> {
        return ArrayList(
            Arrays.asList(
                *getSpreadsheetsDirectory(context)!!
                    .listFiles()
            )
        )
    }

    fun deleteSpreadsheet(context: Context, fileName: String?): Boolean {
        return File(getSpreadsheetsDirectory(context), fileName).delete()
    }

    fun deleteTemplate(context: Context, fileName: String?): Boolean {
        return File(getTemplatesDirectory(context), fileName).delete()
    }

    fun getFileNamesWithoutExt(files: List<File>): List<String> {
        val names: MutableList<String> = ArrayList()
        for (f in files) {
            names.add(getFileNameWithoutExt(f))
        }
        return names
    }

    fun getFileNames(files: List<File>): List<String> {
        val names: MutableList<String> = ArrayList()
        for (f in files) {
            names.add(f.name)
        }
        return names
    }

    fun getFileNameWithoutExt(file: File): String {
        val last = file.name.lastIndexOf(".")
        return file.name.substring(0, last)
    }

    fun getFileNameWithoutExt(fileName: String): String {
        val last = fileName.lastIndexOf(".")
        return fileName.substring(0, last)
    }
}