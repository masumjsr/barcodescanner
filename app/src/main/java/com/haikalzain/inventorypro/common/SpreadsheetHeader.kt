package com.haikalzain.inventorypro.common

import java.io.Serializable
import java.util.Arrays

/**
 * Created by haikalzain on 9/01/15.
 *
 * Mutable!!!
 */
class SpreadsheetHeader : Serializable, Iterable<FieldHeader?> {
    private var fieldHeaders: MutableList<FieldHeader>

    constructor() {
        fieldHeaders = ArrayList(PROTECTED_FIELDS)
    }

    constructor(fieldHeaders: List<FieldHeader>?) : this() {
        this.fieldHeaders.addAll(fieldHeaders!!)
    }

    constructor(header: SpreadsheetHeader) {
        fieldHeaders = ArrayList()
        for (field in header) {
            fieldHeaders.add(field)
        }
    }

    fun getFieldHeaderCount(): Int {
        return fieldHeaders.size
    }

    fun getFieldHeader(position: Int): FieldHeader {
        return fieldHeaders[position]
    }

    fun isFieldHeaderExists(name: String): Boolean {
        for (f in fieldHeaders) {
            if (f.name == name) {
                return true
            }
        }
        return false
    }

    fun addFieldHeader(fieldHeader: FieldHeader) {
        for (f in fieldHeaders) {
            require(f.name != fieldHeader.name) { "Field names must be unique" }
        }
        fieldHeaders.add(fieldHeader)
    }

    fun removeFieldHeader(name: String) {
        for (f in PROTECTED_FIELDS) {
            if (f.name == name) {
                throw RuntimeException("Can't delete a protected field")
            }
        }
        for (f in fieldHeaders) {
            if (f.name == name) {
                fieldHeaders.remove(f)
                return
            }
        }
    }

    fun getFields(): List<FieldHeader> {
        return ArrayList(fieldHeaders)
    }

    override fun iterator(): MutableIterator<FieldHeader> {
        return fieldHeaders.iterator()
    }

    fun getFieldTypes(): List<FieldType> {
        val list: MutableList<FieldType> = ArrayList()
        for (f in fieldHeaders) {
            list.add(f.type)
        }
        return list
    }

    fun getFieldNames(): List<String> {
        val list: MutableList<String> = ArrayList()
        for (f in fieldHeaders) {
            list.add(f.name)
        }
        return list
    }

    companion object {
        private val PROTECTED_FIELDS = Arrays.asList(
            FieldHeader(FieldType.TEXT, "Barcode"),
            FieldHeader(FieldType.TEXT, "Name"),
            FieldHeader(FieldType.POSITIVE_NUMBER, "Count")
        )
        val protectedFields: List<FieldHeader>
            get() = ArrayList(PROTECTED_FIELDS)

        fun getProtectedFieldsCount(): Int {
            return PROTECTED_FIELDS.size
        }
    }
}