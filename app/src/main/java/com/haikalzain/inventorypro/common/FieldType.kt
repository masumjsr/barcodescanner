package com.haikalzain.inventorypro.common

import java.io.Serializable

/**
 * Created by haikalzain on 7/01/15.
 */
enum class FieldType( val fieldName: String) : Serializable {
    TEXT("Text"), LONG_TEXT("Long Text"),  //i.e. multiline
    DATE("Date"), TIME("Time"), RATING("Rating"), YES_NO("Yes/No"), DAY("Day"), NUMBER("Number"), POSITIVE_NUMBER(
        "Positive Number"
    ),
    DECIMAL("Decimal"), PRICE("Price");

    override fun toString(): String {
        return fieldName
    }

    companion object {
        @JvmStatic
        fun getFieldTypeFromString(name: String): FieldType? {
            for (type in values()) {
                if (type.fieldName == name) return type
            }
            return null
        }
    }
}