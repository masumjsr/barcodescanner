package com.haikalzain.inventorypro.model

import com.haikalzain.inventorypro.R

data class HomeItem(
    val id:Int,
    val image:Int,
    val title:String,

)

val homeItemList= listOf(
    HomeItem(id=0,R.drawable.excel, "Spreadsheets"),
    HomeItem(id=1,R.drawable.template, "Templates"),
    HomeItem(id=2,R.drawable.about, "About")
)