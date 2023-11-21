package com.haikalzain.inventorypro.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikalzain.inventorypro.common.Item
import com.haikalzain.inventorypro.common.Spreadsheet
import com.haikalzain.inventorypro.common.SpreadsheetHeader
import com.haikalzain.inventorypro.model.FieldItem
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class AddItemViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val context: Context,
):ViewModel() {

    val response = MutableStateFlow<ApiState>(ApiState.Loading)
    val isComplete = MutableStateFlow(false)

    val isNew = savedStateHandle.get<Boolean>("isNew")?:true
    val fileName=savedStateHandle.get<String>("fileName")?:""
    val barcode = savedStateHandle.get<String>("barcode")

    init {

        viewModelScope.launch {

                try {
                    savedStateHandle.get<String>("fileName")?.let { fileName ->
                        val file = File(
                            FileUtils.getSpreadsheetsDirectory(context),"$fileName.xls")
                        
                        val spreadsheet = Spreadsheet.createFromExcelFile(file)
                        val header = spreadsheet.spreadsheetHeader
                        val list=ArrayList<FieldItem>()
                        val item:Item? = barcode?.let { spreadsheet.getItem(it) }
                        val values = ArrayList<String>()

                        if (item != null) {
                            item.forEach {
                                if (it != null) {
                                    values.add(it.value)
                                }
                            }

                        }
                        else{
                            if (barcode != null) {
                                values.add(barcode)
                            }
                        }

                        for (i in 0 until header.getFieldHeaderCount()){
                            val value=if (values.size>i)values[i] else FieldViewFactory.getDefaultValue(header.getFieldTypes()[i])
                            list.add(FieldItem(header.getFieldTypes()[i],header.getFieldNames()[i],i >= SpreadsheetHeader.getProtectedFieldsCount(),value))
                        }



                        response.value = ApiState.Success(list)


                    }
                } catch (e: Exception) {
                    response.value = ApiState.Failure(e.message ?: "Error")

                    Log.i("123321", "error:${e.message} ")
                }
        }
        }




    fun save(list: ArrayList<String>) {
        val fileName= savedStateHandle.get<String>("fileName")?:""
        viewModelScope.launch {
            val file = File(
                FileUtils.getSpreadsheetsDirectory(context),"$fileName.xls")
            val spreadsheet = Spreadsheet.createFromExcelFile(file)

            spreadsheet.getItem(list[0])?.let { spreadsheet.deleteItem(it) }
            try {
                Log.i("123321", "save: input $list")
                spreadsheet.addItem(list)
                spreadsheet.exportExcelToFile(file)
                isComplete.value=true
            } catch (e: IOException) {
                Log.i("123321", "error:${e.message} ")
            }


        }
    }
}