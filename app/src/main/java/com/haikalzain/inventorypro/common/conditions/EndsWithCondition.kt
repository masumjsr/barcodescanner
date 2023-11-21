package com.haikalzain.inventorypro.common.conditions

/**
 * Created by haikalzain on 10/01/15.
 */
class EndsWithCondition : Condition() {
    override fun evaluate(a: Any?, b: Any?): Boolean {
        return (a as String?)!!.endsWith((b as String?)!!)
    }

    override fun toString(): String {
        return "Ends with"
    }
}