package com.haikalzain.inventorypro.common.conditions

import java.io.Serializable

/**
 * Created by haikalzain on 10/01/15.
 * Objects MUST IMPLEMENT COMPARABLE!!!
 */
abstract class Condition : Serializable {
    abstract fun evaluate(a: Any?, b: Any?): Boolean
    abstract override fun toString(): String
    override fun equals(o: Any?): Boolean {
        return if (o !is Condition) false else toString() == o.toString()
    }

    companion object {
        val NULL: Condition = object : Condition() {
            override fun evaluate(a: Any?, b: Any?): Boolean {
                return true
            }

            override fun toString(): String {
                return "None"
            }
        }
    }
}