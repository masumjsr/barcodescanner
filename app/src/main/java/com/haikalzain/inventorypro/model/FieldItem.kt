package com.haikalzain.inventorypro.model

import com.haikalzain.inventorypro.common.FieldType

data class FieldItem(
    val fieldType: FieldType,
    val name: String,
    val deletable: Boolean,
    val value: String=""
)