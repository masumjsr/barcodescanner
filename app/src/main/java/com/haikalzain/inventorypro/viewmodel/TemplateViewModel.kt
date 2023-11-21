package com.haikalzain.inventorypro.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikalzain.inventorypro.common.FieldHeader
import com.haikalzain.inventorypro.common.Spreadsheet
import com.haikalzain.inventorypro.common.SpreadsheetHeader
import com.haikalzain.inventorypro.model.FieldItem
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.Collections

class TemplateViewModel(
    private val savedStateHandle: SavedStateHandle,
   private val context: Context,
):ViewModel(){

    val response = MutableStateFlow<ApiState>(ApiState.Loading)
    val isComplete = MutableStateFlow(false)
    var header: SpreadsheetHeader?=null



    fun addField(fieldItem: FieldItem) {

        viewModelScope.launch {
            when(val v= response.value){
                is ApiState.Success->{
                    val previous=(v.data as ArrayList<FieldItem>)
                    val filtered=previous.filter{it.name == fieldItem.name}
                  if(filtered.isEmpty()) {
                      previous.add(fieldItem)
                  }
                    else {
                      Toast.makeText(context, "${filtered.first().name}  already exist", Toast.LENGTH_SHORT).show()
                  }
                    response.value = ApiState.Success(previous)
                }
                else ->{

                }
            }
        }
    }

    fun swapPosition(from:Int, to:Int){
        viewModelScope.launch {
            when(val v= response.value){
                is ApiState.Success->{
                    val previous=(v.data as ArrayList<FieldItem>)
                   Collections.swap(previous,from,to)
                    response.value = ApiState.Success(previous)
                }
                else ->{

                }
            }
        }

    }
    fun removeField(fieldItem: FieldItem) {


        viewModelScope.launch {
            when(val v=response.value){
                is ApiState.Success->{
                    val previous=(v.data as ArrayList<FieldItem>)
                    previous.remove(fieldItem)
                    response.value = ApiState.Success(previous)

                }
                else ->{

                }
            }
        }
    }
    init {
        viewModelScope.launch {
           if(savedStateHandle.get<Boolean>("isNew")!!){
               header = SpreadsheetHeader()
               val list=ArrayList<FieldItem>()
               for (i in 0 until header!!.getFieldHeaderCount()){
                   list.add(FieldItem(header!!.getFieldTypes()[i], header!!.getFieldNames()[i],i >= SpreadsheetHeader.getProtectedFieldsCount()))
               }

               response.value = ApiState.Success(list)
           }
            else{
               savedStateHandle.get<String>("file")?.let {
                   val file = File(
                       FileUtils.getTemplatesDirectory(context),it)
                   val spreadsheet = Spreadsheet.createFromExcelFile(file)
                    header = spreadsheet.spreadsheetHeader
                   val list=ArrayList<FieldItem>()
                   for (i in 0 until header!!.getFieldHeaderCount()){
                       list.add(FieldItem(header!!.getFieldTypes()[i],header!!.getFieldNames()[i],i >= SpreadsheetHeader.getProtectedFieldsCount()))
                   }

                   response.value = ApiState.Success(list)


               }
           }
        }


    }

    fun save(){
        val fileName= savedStateHandle.get<String>("file")
        if(fileName!=null){
            viewModelScope.launch {
                val fieldBuilder = SpreadsheetHeader()
                when(response.value){
                    is ApiState.Success->{
                        val data= (response.value as ApiState.Success).data as List<FieldItem>
                        data.forEachIndexed { index, it ->



                                if(index>2) {
                                    fieldBuilder.removeFieldHeader(it.name)
                                    fieldBuilder.addFieldHeader(FieldHeader(it.fieldType, it.name))
                                }
                        }

                       val template =Spreadsheet(fieldBuilder)
                        val outfile = File(FileUtils.getTemplatesDirectory(context).toString() + File.separator + fileName + if(fileName.endsWith(".xls"))"" else ".xls")
                        try {
                            template.exportExcelToFile(outfile)
                            isComplete.value=true
                        } catch (e: IOException) {
                            Toast.makeText(context, "Somethings want wrong !\n${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else->{

                    }
                }
            }
        }
    }
}