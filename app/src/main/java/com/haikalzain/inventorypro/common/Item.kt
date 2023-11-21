package com.haikalzain.inventorypro.common

import java.io.Serializable

/**
 * Created by haikalzain on 9/01/15.
 */
class Item(data: List<Field>?) : Iterable<Field?>, Serializable {
    private val data: MutableList<Field>

    init {
        this.data = ArrayList(data)
    }

    fun getField(position: Int): Field {
        return data[position]
    }

    fun getField(name: String): Field? {
        for (field in data) {
            if (field.name == name) {
                return field
            }
        }
        return null
    }

    val fieldCount: Int
        get() = data.size

    override fun iterator(): MutableIterator<Field> {
        return data.iterator()
    }

    override fun toString(): String {
        return "Item{" +
                "data=" + data +
                '}'
    }
}