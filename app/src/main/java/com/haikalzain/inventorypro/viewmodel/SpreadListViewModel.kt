package com.haikalzain.inventorypro.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikalzain.inventorypro.common.Spreadsheet
import com.haikalzain.inventorypro.model.TemplateItem
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SpreadListViewModel (val context: Context): ViewModel(){

    val spreadList = MutableStateFlow<ApiState>(ApiState.Empty)
    val templateList = MutableStateFlow<ApiState>(ApiState.Empty)
    val isComplete = MutableStateFlow<ApiState>(ApiState.Empty)
    init {
        viewModelScope.launch {
            spreadList.value=ApiState.Loading
            try {
                val directory =  FileUtils.getSpreadsheetsDirectory(context).listFiles()
                val list=ArrayList<TemplateItem>()

                directory?.let { files ->
                    files.forEach {
                        list.add(TemplateItem(it.nameWithoutExtension))
                    }

                    Log.i("123321", "spreadList=:$list ")
                    spreadList.value=ApiState.Success(list)
                }
        } catch (e: Exception) {
           e.message?.let {
               spreadList.value=ApiState.Failure(it)
           }
        }
        }
        viewModelScope.launch {
            templateList.value=ApiState.Loading
            try {
                val directory =  FileUtils.getTemplatesDirectory(context).listFiles()
                val list=ArrayList<String>()

                directory?.let { files ->
                    files.forEach {
                        list.add(it.nameWithoutExtension)
                    }
                    templateList.value=ApiState.Success(list.reversed())
                }
        } catch (e: Exception) {
           e.message?.let {
               templateList.value=ApiState.Failure(it)
           }
        }
        }

    }

     fun createFile(templateName: String, outFileName: String) {
        val templateFile = File(
            FileUtils.getTemplatesDirectory(context),
            "$templateName.xls"
        )
        val outFile = File(
            FileUtils.getSpreadsheetsDirectory(context),
            "$outFileName.xls"
        )

        try {

        } catch (e: IOException) {
            Log.i("123321", "createFile: ${e.message}")


        }
        try {
            val spreadsheet = Spreadsheet.createFromExcelFile(templateFile)
            spreadsheet!!.exportExcelToFile(outFile)

            try {
                val directory = FileUtils.getSpreadsheetsDirectory(context).listFiles()
                val list = ArrayList<TemplateItem>()

                directory?.let { files ->
                    files.forEach {
                        list.add(TemplateItem(it.nameWithoutExtension))
                    }

                    spreadList.value = ApiState.Success(list)
                    isComplete.value = ApiState.Success(outFileName)

                }
            } catch (e: Exception) {
                e.message?.let {
                    spreadList.value = ApiState.Failure(it)
                }
            }


        } catch (e: IOException) {
            Log.i("123321", "createFile: ${e.message}")

        }
    }

    fun refresh() {
        viewModelScope.launch {
            spreadList.value = ApiState.Loading
            try {
                val directory = FileUtils.getSpreadsheetsDirectory(context).listFiles()
                val list = ArrayList<TemplateItem>()

                directory?.let { files ->
                    files.forEach {
                        list.add(TemplateItem(it.nameWithoutExtension))
                    }

                    spreadList.value = ApiState.Success(list)
                }
            } catch (e: Exception) {
                e.message?.let {
                    spreadList.value = ApiState.Failure(it)
                }
            }
        }
    }

}