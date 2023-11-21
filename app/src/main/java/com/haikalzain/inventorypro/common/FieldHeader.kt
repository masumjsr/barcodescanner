package com.haikalzain.inventorypro.common

import java.io.Serializable

/**
 * Created by haikalzain on 9/01/15.
 */
class FieldHeader(@JvmField val type: FieldType, @JvmField val name: String) : Serializable {

    override fun toString(): String {
        return name
    }

    companion object {
        @JvmField
        val NULL = FieldHeader(FieldType.TEXT, "")
    }
}