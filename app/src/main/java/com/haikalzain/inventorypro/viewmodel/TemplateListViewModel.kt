package com.haikalzain.inventorypro.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikalzain.inventorypro.model.TemplateItem
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TemplateListViewModel (val context: Context): ViewModel(){
    fun delete(title: String) {
        FileUtils.deleteTemplate(context, title)
        val directory = FileUtils.getTemplatesDirectory(context).listFiles()
        val list = ArrayList<TemplateItem>()

        directory?.let { files ->
            Log.i("123321", "file:${files.size} ")
            files.forEach {
                list.add(TemplateItem(it.name))
            }
            spreadList.value = ApiState.Success(list.reversed())

        }
    }

    val spreadList = MutableStateFlow<ApiState>(ApiState.Empty)
    init {
        viewModelScope.launch {
            spreadList.value=ApiState.Loading
            try {
                val directory =  FileUtils.getTemplatesDirectory(context).listFiles()
                val list=ArrayList<TemplateItem>()

                directory?.let { files ->
                    Log.i("123321", "file:${files.size} ")
                    files.forEach {
                        list.add(TemplateItem(it.name))
                    }
                    spreadList.value=ApiState.Success(list.reversed())
                }
        } catch (e: Exception) {
           e.message?.let {
               spreadList.value=ApiState.Failure(it)
           }
        }
        }

    }

}