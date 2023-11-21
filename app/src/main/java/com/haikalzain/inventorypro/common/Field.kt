package com.haikalzain.inventorypro.common

import java.io.Serializable

/**
 * Created by haikalzain on 9/01/15.
 */
class Field(private val fieldHeader: FieldHeader, @JvmField val value: String) : Serializable {

    val type: FieldType
        get() = fieldHeader.type
    val name: String
        get() = fieldHeader.name

    override fun toString(): String {
        return "Field{" +
                "name=" + fieldHeader.name +
                ", type='" + fieldHeader.type + '\'' +
                ", value='" + value + '\'' +
                '}'
    }
}