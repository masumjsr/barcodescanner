package com.haikalzain.inventorypro

import android.app.Application
import android.util.Log
import com.haikalzain.inventorypro.di.appModule
import com.haikalzain.inventorypro.utils.FileUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by haikalzain on 7/01/15.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }


        val templatesDirectory = FileUtils.getTemplatesDirectory(this)
        if (templatesDirectory != null) {
            if (!templatesDirectory.exists()) {
                templatesDirectory.mkdir()
            }
        }
        val spreadsheetsDirectory = FileUtils.getSpreadsheetsDirectory(this)
        if (spreadsheetsDirectory != null) {
            if (!spreadsheetsDirectory.exists()) {
                spreadsheetsDirectory.mkdir()
            }
        }

        // Copy in spreadsheets and templates if empty
        val assetManager = assets
        if (FileUtils.getSpreadsheetFiles(this).isEmpty()) {
            try {
                for (fileName in assetManager.list("spreadsheets")!!) {
                    Log.v(TAG, fileName)
                    val `in` = assetManager.open("spreadsheets/$fileName")
                    val out: OutputStream = FileOutputStream(File(spreadsheetsDirectory, fileName))
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    `in`.close()
                    out.close()
                }
            } catch (_: IOException) {
            }
        }
        if (FileUtils.getTemplateFiles(this).isEmpty()) {
            try {
                for (fileName in assetManager.list("templates")!!) {
                    val `in` = assetManager.open("templates/$fileName")
                    val out: OutputStream = FileOutputStream(File(templatesDirectory, fileName))
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    `in`.close()
                    out.close()
                }
            } catch (_: IOException) {
            }
        }
    }

    companion object {
        private const val TAG = "Application"
    }
}