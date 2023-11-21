package com.haikalzain.inventorypro.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikalzain.inventorypro.common.FieldHeader
import com.haikalzain.inventorypro.common.Item
import com.haikalzain.inventorypro.common.Spreadsheet
import com.haikalzain.inventorypro.common.conditions.Condition
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SpreadViewModel(
    savedStateHandle: SavedStateHandle,
    private val context: Context,
):ViewModel() {

    val response = MutableStateFlow<ApiState>(ApiState.Loading)
    var spreadsheet: Spreadsheet?=null

    val fileName:String=savedStateHandle.get<String>("fileName")?:""
    val file = File(FileUtils.getSpreadsheetsDirectory(context), "$fileName.xls")
    init {

        Log.i("123321", "initing: ")
        val isNew:Boolean=savedStateHandle.get<Boolean>("isNew")?:true

        val templateName:String=savedStateHandle.get<String>("templateName")?:""
        viewModelScope.launch {



                    try {

                        spreadsheet = Spreadsheet.createFromExcelFile(file)

                        val sortedFilteredItemList = spreadsheet!!.sortedFilteredItemList

                    response.value = ApiState.Success(sortedFilteredItemList)

            } catch (e: Exception) {
                response.value = ApiState.Failure(e.message!!)
            }
        }

    }


    fun sort(selectedField: FieldHeader, ascending: Boolean) {
        spreadsheet?.setSortBy(
            selectedField,
           ascending
        )
        val sortedFilteredItemList = spreadsheet!!.sortedFilteredItemList

        response.value = ApiState.Success(sortedFilteredItemList)
    }

    fun setFilters(list: ArrayList<Condition>, item: ArrayList<String>) {
        spreadsheet?.setFilters(list, item)
        val sortedFilteredItemList = spreadsheet!!.sortedFilteredItemList

        response.value = ApiState.Success(sortedFilteredItemList)
    }

    fun delete(item: Item) {
        spreadsheet?.deleteItem(item)

        spreadsheet?.exportExcelToFile(file)
        val sortedFilteredItemList = spreadsheet!!.sortedFilteredItemList
        response.value = ApiState.Success(sortedFilteredItemList)
    }

    fun deleteSelected(selectedItem: MutableList<Item>) {
        selectedItem.forEach {

            spreadsheet?.deleteItem(it)

            spreadsheet?.exportExcelToFile(file)
            val sortedFilteredItemList = spreadsheet!!.sortedFilteredItemList
            response.value = ApiState.Success(sortedFilteredItemList)
        }

    }

}