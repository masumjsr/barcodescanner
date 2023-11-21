package com.haikalzain.inventorypro.common.conditions

/**
 * Created by haikalzain on 10/01/15.
 */
class LessThanCondition : Condition() {
    override fun evaluate(a: Any?, b: Any?): Boolean {


        return (b as Comparable<Any?>?)!!.compareTo(a as Comparable<Any?>?) > 0
    }

    override fun toString(): String {
        return "Less than"
    }
}