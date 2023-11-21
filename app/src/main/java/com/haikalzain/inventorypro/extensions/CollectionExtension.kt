package com.haikalzain.inventorypro.extensions

fun <E>MutableList<E>.setAll(list: List<E>){
  clear()
    addAll(list)
}